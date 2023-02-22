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

  companion object{
    val Factory:() -> GameViewModel = {
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
  private val internalGameProgess = MutableStateFlow(INITIAL_GAME_PROGRESS)
  val gameProgress: StateFlow<GameProgress> = internalGameProgess
  private val internalElapsedTime = MutableStateFlow(0L)
  val elapsedTime: StateFlow<Long> = internalElapsedTime
  private val internalGameState = MutableStateFlow<GameState>(GameState.Running)
  val gameState: StateFlow<GameState> = internalGameState

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

  private fun isGameRunning() = gameState.value.isGameRunning()

  private fun updateSumMoveCounter(addend: Int): GameProgress {
    val gameProgress = requireNotNull(internalGameProgess.value)
    val newProgress = GameProgress(
      sum = gameProgress.sum + addend,
      moveCounter = gameProgress.moveCounter + 1
    )
    internalGameProgess.value = newProgress
    return newProgress
  }

  private fun getScreenState(gameProgress: GameProgress): GameState {
    val elapsedTime = requireNotNull(elapsedTime.value)
    val addendTarget = requireNotNull(internalGameplay.value)
    val gameState =
      when (getGameStateInteractor(gameProgress.sum, addendTarget.target)) {
        GetGameStateInteractor.CompletionResult.SUCCESS -> GameState.Completed.Succeeded(
          gameProgress.moveCounter,
          elapsedTime
        )
        GetGameStateInteractor.CompletionResult.FAILURE -> GameState.Completed.Failed(
          gameProgress.moveCounter,
          elapsedTime
        )
        GetGameStateInteractor.CompletionResult.RUNNING -> GameState.Running
      }
    return gameState
  }

  private fun updateScreeState(gameState: GameState) {
    if (gameState.isGameCompleted())
      stopTicker()

    internalGameState.value = gameState
  }

  @Suppress("ForbiddenComment")
  private fun startGame() {
    updateScreeState(GameState.Loading)
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
      internalGameState.value = GameState.Running
      elapsedTimeFlow(tickPeriod = TICKER_PERIOD).collect {
        internalElapsedTime.value = it
      }
    }
  }

  private fun stopTicker() {
    elapsedTimeJob?.cancel()
  }

  sealed class GameState {
    object Loading : GameState()
    object Running : GameState()
    sealed class Completed(open val moveCount: Int, open val elapsedTime: Long): GameState() {
      class Failed(override val moveCount: Int, override val elapsedTime: Long) : Completed(moveCount, elapsedTime)
      class Succeeded(override val moveCount: Int, override val elapsedTime: Long) : Completed(moveCount, elapsedTime)
    }

    fun isGameCompleted() = this is Completed

    fun isGameRunning() = this == Running
  }
}
