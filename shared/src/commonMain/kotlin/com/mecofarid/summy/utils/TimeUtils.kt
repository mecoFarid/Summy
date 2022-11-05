package com.mecofarid.summy.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

const val MINUTE_SECOND_PATTERN = "mm:ss"

fun elapsedTimeFlow(tickPeriod: Long) = flow {
  var accumulatedTime = 0L
  while (true) {
    delay(tickPeriod)
    accumulatedTime += tickPeriod
    emit(accumulatedTime)
  }
}