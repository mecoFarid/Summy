package com.mecofarid.summy.features.game.domain.interactor

import com.mecofarid.summy.common.utils.randomInt

private const val ADDEND_SUM_MIN_MULTIPLIER = 1
private const val ADDEND_SUM_MAX_MULTIPLIER = 10

class GetAddendsSumMultiplierInteractor {
  suspend operator fun invoke() =
    randomInt(min = ADDEND_SUM_MIN_MULTIPLIER, max = ADDEND_SUM_MAX_MULTIPLIER)
}