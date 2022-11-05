package com.mecofarid.summy.features.game.domain.interactor

class GetGameCompletionResultInteractor {
  operator fun invoke(sum: Int, target: Int): CompletionResult =
    when {
      sum < target -> CompletionResult.RUNNING
      sum == target -> CompletionResult.SUCCESS
      else -> CompletionResult.FAILURE
    }

  enum class CompletionResult {
    RUNNING,
    SUCCESS,
    FAILURE
  }
}