package com.mecofarid.summy.features.game.presentation

import com.mecofarid.summy.app.appComponent
import com.mecofarid.summy.common.presentation.PlatformViewModel
import com.mecofarid.summy.features.game.data.query.GameplayQuery
import com.mecofarid.summy.features.game.domain.interactor.GetGameStateInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetGameplayInteractor
import com.mecofarid.summy.features.game.domain.model.GameProgress
import com.mecofarid.summy.features.game.domain.model.Gameplay
import com.mecofarid.summy.features.game.utils.elapsedTimeFlow
import com.mecofarid.summy.features.game.utils.randomInt
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource


private const val TICKER_PERIOD = 1000L // seconds
private val INITIAL_GAME_PROGRESS = GameProgress(moveCounter = 0, sum = 0)

@Suppress("MagicNumber")
private val GAMEPLAY_QUERY = GameplayQuery(
    addendCount = 3,
    addendSumMultiplier = randomInt(1, 10),
    minAddend = 1,
    maxAddend = 9
)

class GameViewModel(
    private val getGameplayInteractor: GetGameplayInteractor,
    private val getGameStateInteractor: GetGameStateInteractor
) : PlatformViewModel() {

    companion object {
        val Factory: () -> GameViewModel = {
            with(appComponent.gameComponent) {
                GameViewModel(
                    getGameplayInteractor(),
                    getGamepStateInteractor()
                )
            }
        }
    }

    private val internalGameplay = MutableStateFlow(Gameplay(addends = emptyList(), target = 0))
    val gameplay: StateFlow<Gameplay> = internalGameplay
    private val internalGameProgress = MutableStateFlow(INITIAL_GAME_PROGRESS)
    val gameProgress: StateFlow<GameProgress> = internalGameProgress
    private val internalTimeTicker = MutableStateFlow(0L)
    val timeTicker: StateFlow<Long> = internalTimeTicker
    private val internalScreenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    val screenState: StateFlow<ScreenState> = internalScreenState

    private var timeTickerJob: Job? = null
    private var gameJob: Job? = null

    @OptIn(ExperimentalTime::class)
    private lateinit var exactElapsedTimeMark: TimeMark

    init {
        startGame()
    }

    fun restartGame() {
        startGame()
    }

    fun onAdd(addend: Int) {
        if (!isGameRunning())
            return

        updateGameProgressAndState(addend)
    }

    private fun isGameRunning() = screenState.value.isGameRunning()

    private fun updateGameProgressAndState(addend: Int) {
        val gameplay = requireNotNull(internalGameplay.value)
        val gameProgress = requireNotNull(internalGameProgress.value)
        val gameState = getGameStateInteractor(gameProgress, addend, gameplay.target)

        internalGameProgress.value = gameState.gameProgress

        val screenState =
            when (gameState) {
                is GetGameStateInteractor.State.Success -> ScreenState.Completed.Succeeded(
                    gameState.gameProgress.moveCounter,
                    getExactElapsedTime()
                )
                is GetGameStateInteractor.State.Failure -> ScreenState.Completed.Failed(
                    gameState.gameProgress.moveCounter,
                    getExactElapsedTime()
                )
                is GetGameStateInteractor.State.Running -> ScreenState.Running
            }
        updateGameState(screenState)
    }

    private fun updateGameState(screenState: ScreenState) {
        if (screenState.isGameCompleted())
            stopTicker()

        internalScreenState.value = screenState
    }

    @Suppress("ForbiddenComment")
    private fun startGame() {
        updateGameState(ScreenState.Loading)
        gameJob?.cancel()
        gameJob = scope.launch {
            getGameplayInteractor(GAMEPLAY_QUERY)
                .onRight {
                    internalGameplay.value = it
                    internalGameProgress.value = INITIAL_GAME_PROGRESS

                    updateGameState(ScreenState.Running)
                    startExactElapsedTimer()

                    startTicker()
                }
                .onLeft {
                    // TODO: Display error
                }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun startTicker() {
        stopTicker()
        timeTickerJob = scope.launch {
            elapsedTimeFlow(tickPeriod = TICKER_PERIOD).collect {
                internalTimeTicker.value = it
            }
        }
    }

    private fun stopTicker() {
        timeTickerJob?.cancel()
    }

    @OptIn(ExperimentalTime::class)
    private fun startExactElapsedTimer() {
        exactElapsedTimeMark = TimeSource.Monotonic.markNow()
    }

    @OptIn(ExperimentalTime::class)
    private fun getExactElapsedTime() =
        exactElapsedTimeMark.elapsedNow().inWholeMilliseconds

    sealed class ScreenState {
        object Loading : ScreenState()
        object Running : ScreenState()
        sealed class Completed(open val moveCount: Int, open val elapsedTime: Long) : ScreenState() {
            class Failed(override val moveCount: Int, override val elapsedTime: Long) :
                Completed(moveCount, elapsedTime)

            class Succeeded(override val moveCount: Int, override val elapsedTime: Long) :
                Completed(moveCount, elapsedTime)
        }

        fun isGameCompleted() = this is Completed

        fun isGameRunning() = this == Running
    }
}
