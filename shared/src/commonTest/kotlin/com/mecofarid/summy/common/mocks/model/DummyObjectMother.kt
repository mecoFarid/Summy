@file:Suppress("MatchingDeclarationName")

package com.mecofarid.summy.common.mocks.model

import com.mecofarid.summy.utils.randomInt
import com.mecofarid.summy.utils.randomString

data class Dummy(
    val dummyName: String,
    val dummyAge: Int
)

fun anyDummy() = Dummy(
    randomString(),
    randomInt()
)
