package com.mecofarid.summy.features.game.data.query

import com.mecofarid.summy.common.data.Query

data class GameplayQuery(
    val addendCount: Int,
    val addendSumMultiplier: Int,
    val minAddend: Int,
    val maxAddend: Int
): Query
