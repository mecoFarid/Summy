package com.mecofarid.summy.features.game.presentation

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
): PlatformViewModel() {

  private val internalGameplay = MutableStateFlow(Gameplay(addends = emptyList(), target = 0))
  val gameplay: StateFlow<Gameplay> = internalGameplay
  private val internalGameProgess = MutableStateFlow(INITIAL_GAME_PROGRESS)
  val gameProgress: StateFlow<GameProgress> = internalGameProgess

  private val internalElapsedTime = MutableStateFlow(0L)
  val elapsedTime: StateFlow<Long> = internalElapsedTime
  private val internalScreenState = MutableStateFlow<ScreenState>(ScreenState.Running)
  val screenState: StateFlow<ScreenState> = internalScreenState

  private var elapsedTimeJob: Job? = null
  private var gameJob: Job? = null

  init {
    startGame()
  }

  fun restartGame() {
    startGame()
  }

  fun onAdd(addend: Int) {
    if (!isGameRunning())
      return

    val sumMoveCounter = updateSumMoveCounter(addend)
    val screenState = getScreenState(sumMoveCounter)
    updateScreeState(screenState)
  }

  private fun isGameRunning() = screenState.value == ScreenState.Running

  private fun updateSumMoveCounter(addend: Int): GameProgress {
    val gameProgress = requireNotNull(internalGameProgess.value)
    val newProgress = GameProgress(
      sum = gameProgress.sum + addend,
      moveCounter = gameProgress.moveCounter + 1
    )
    internalGameProgess.value = newProgress
    return newProgress
  }

  private fun getScreenState(gameProgress: GameProgress): ScreenState {
    val elapsedTime = requireNotNull(elapsedTime.value)
    val addendTarget = requireNotNull(internalGameplay.value)
    val screenState =
      when (getGameStateInteractor(gameProgress.sum, addendTarget.target)) {
        GetGameStateInteractor.CompletionResult.SUCCESS -> ScreenState.Succeeded(
          gameProgress.moveCounter,
          elapsedTime
        )
        GetGameStateInteractor.CompletionResult.FAILURE -> ScreenState.Failed(
          gameProgress.moveCounter,
          elapsedTime
        )
        GetGameStateInteractor.CompletionResult.RUNNING -> ScreenState.Running
      }
    return screenState
  }

  private fun updateScreeState(screenState: ScreenState) {
    if (screenState.isGameFinished())
      stopTicker()

    internalScreenState.value = screenState
  }

  @Suppress("ForbiddenComment")
  private fun startGame() {
    updateScreeState(ScreenState.Loading)
    gameJob?.cancel()
    gameJob = scope.launch {
      getGameplayInteractor(GAMEPLAY_QUERY)
        .onRight {
          internalGameplay.value = it
          internalElapsedTime.value = 0
          internalGameProgess.value = INITIAL_GAME_PROGRESS

          startTicker()
        }
        .onLeft {
          // TODO: Display error
        }
    }
  }

  private fun startTicker() {
    stopTicker()
    elapsedTimeJob = scope.launch {
      internalScreenState.value = ScreenState.Running
      elapsedTimeFlow(tickPeriod = TICKER_PERIOD).collect {
        internalElapsedTime.value = it
      }
    }
  }

  private fun stopTicker() {
    elapsedTimeJob?.cancel()
  }

  sealed class ScreenState {
    object Loading : ScreenState()
    object Running : ScreenState()
    class Failed(val moveCount: Int, val elapsedTime: Long) : ScreenState()
    class Succeeded(val moveCount: Int, val elapsedTime: Long) : ScreenState()

    fun isGameFinished() = this is Failed || this is Succeeded
  }
}
