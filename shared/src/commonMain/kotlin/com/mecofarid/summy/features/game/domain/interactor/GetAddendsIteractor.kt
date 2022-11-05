package com.mecofarid.summy.features.game.domain.interactor

import com.mecofarid.summy.common.utils.randomInt

private const val MIN_ADDEND = 1
private const val MAX_ADDEND = 9

class GetAddendsInteractor {
  suspend operator fun invoke(addendCount: Int): Collection<Int> {
    return mutableListOf<Int>().apply {
      repeat(addendCount) {
        add(randomInt(min = MIN_ADDEND, max = MAX_ADDEND))
      }
    }
  }
}