package com.mecofarid.summy.features.game.domain.interactor

import com.mecofarid.summy.features.game.domain.model.GameProgress

class GetGameStateInteractor {
    operator fun invoke(progress: GameProgress, addend: Int, target: Int): State {
        val newProgress = progress.copy(
            sum = progress.sum + addend,
            moveCounter = progress.moveCounter + 1
        )
        val newSum = newProgress.sum

        return when {
            newSum < target -> State.Running(newProgress)
            newSum == target -> State.Success(newProgress)
            else -> State.Failure(newProgress)
        }
    }

    sealed class State(open val gameProgress: GameProgress) {
        data class Running(override val gameProgress: GameProgress) : State(gameProgress)
        data class Success(override val gameProgress: GameProgress) : State(gameProgress)
        data class Failure(override val gameProgress: GameProgress) : State(gameProgress)
    }
}
