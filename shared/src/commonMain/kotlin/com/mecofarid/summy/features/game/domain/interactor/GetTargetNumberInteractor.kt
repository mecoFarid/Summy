package com.mecofarid.summy.features.game.domain.interactor

import kotlin.math.roundToInt

class GetTargetNumberInteractor(private val sumMultiplierUseCase: GetAddendsSumMultiplierInteractor) {
  suspend operator fun invoke(addends: Collection<Int>): Int {
    val multipliedSum = addends.sum() * sumMultiplierUseCase()
    return multipliedSum + sumHalfOfAddends(addends)
  }

  private fun sumHalfOfAddends(addends: Collection<Int>) : Int {
    val halfSizeOfAddends = (addends.size / 2f).roundToInt()

    var sum = 0
    repeat(halfSizeOfAddends){
      sum += addends.random()
    }
    return sum
  }
}