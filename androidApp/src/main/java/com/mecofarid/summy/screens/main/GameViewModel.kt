package com.mecofarid.summy.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mecofarid.summy.utils.elapsedTimeFlow
import com.mecofarid.summy.features.game.domain.interactor.GetAddendsInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetGameCompletionResultInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetTargetNumberInteractor
import com.mecofarid.summy.features.game.domain.model.AddendTarget
import com.mecofarid.summy.features.game.domain.model.SumMoveCounter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val ADDEND_COUNT = 3
private const val TICKER_PERIOD = 1000L // seconds

class GameViewModel(
  private val getAddendsInteractor: GetAddendsInteractor,
  private val getTargetNumberInteractor: GetTargetNumberInteractor,
  private val gameCompletedResultUseCase: GetGameCompletionResultInteractor
) : ViewModel() {

  private val internalAddendTarget = MutableLiveData(AddendTarget())
  val addendTarget: LiveData<AddendTarget> = internalAddendTarget
  private val internalSumMoveCounter = MutableLiveData(SumMoveCounter())
  val sumMoveCounter: LiveData<SumMoveCounter> = internalSumMoveCounter
  private val internalElapsedTime = MutableLiveData(0L)
  val elapsedTime: LiveData<Long> = internalElapsedTime
  private val internalScreenState = MutableLiveData<ScreenState>(ScreenState.Running)
  val screenState: LiveData<ScreenState> = internalScreenState

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

  private fun updateSumMoveCounter(addend: Int) : SumMoveCounter {
    val sumMoveCounter = requireNotNull(internalSumMoveCounter.value)
    sumMoveCounter.apply {
      sum += addend
      moveCounter++
    }
    internalSumMoveCounter.value = sumMoveCounter
    return sumMoveCounter
  }

  private fun getScreenState(sumMoveCounter: SumMoveCounter): ScreenState {
    val elapsedTime = requireNotNull(elapsedTime.value)
    val addendTarget = requireNotNull(internalAddendTarget.value)
    val screenState =
      when (gameCompletedResultUseCase(sumMoveCounter.sum, addendTarget.target)) {
        GetGameCompletionResultInteractor.CompletionResult.SUCCESS -> ScreenState.Succeeded(
          sumMoveCounter.moveCounter,
          elapsedTime
        )
        GetGameCompletionResultInteractor.CompletionResult.FAILURE -> ScreenState.Failed(
          sumMoveCounter.moveCounter,
          elapsedTime
        )
        GetGameCompletionResultInteractor.CompletionResult.RUNNING -> ScreenState.Running
      }
    return screenState
  }

  private fun updateScreeState(screenState: ScreenState) {
    if (screenState.isGameFinished())
      stopTicker()

    internalScreenState.value = screenState
  }

  private fun startGame() {
    updateScreeState(ScreenState.Loading)
    gameJob?.cancel()
    gameJob = viewModelScope.launch {
      val addends = getAddendsInteractor(ADDEND_COUNT)
      val target = getTargetNumberInteractor(addends)
      internalAddendTarget.value = AddendTarget(addends = addends, target = target)
      internalElapsedTime.value = 0
      internalSumMoveCounter.value = SumMoveCounter()

      startTicker()
    }
  }

  private fun startTicker() {
    stopTicker()
    elapsedTimeJob = viewModelScope.launch {
      internalScreenState.value = ScreenState.Running
      elapsedTimeFlow(tickPeriod = TICKER_PERIOD).collect {
        internalElapsedTime.value = it
      }
    }
  }

  private fun stopTicker() {
    elapsedTimeJob?.cancel()
  }

  sealed class ScreenState() {
    object Loading : ScreenState()
    object Running : ScreenState()
    class Failed(val moveCount: Int, val elapsedTime: Long) : ScreenState()
    class Succeeded(val moveCount: Int, val elapsedTime: Long) : ScreenState()

    fun isGameFinished() = this is Failed || this is Succeeded
  }
}