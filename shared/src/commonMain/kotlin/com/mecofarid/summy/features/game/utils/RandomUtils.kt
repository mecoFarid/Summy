package com.mecofarid.summy.features.game.utils

import kotlin.random.Random

fun randomInt(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int =
    Random.nextInt(min, max)
