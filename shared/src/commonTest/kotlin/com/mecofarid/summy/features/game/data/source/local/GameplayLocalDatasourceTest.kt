package com.mecofarid.summy.features.game.data.source.local

import com.mecofarid.summy.features.game.data.query.GameplayQuery
import com.mecofarid.summy.features.game.utils.randomInt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameplayLocalDatasourceTest {

    @Suppress("ForbiddenComment")
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `addends are within min-max range`() = runTest {
        val addendCount = randomInt(min = 1, max = 50)
        val minAddend = randomInt(max = 0)
        val maxAddend = randomInt(min = 1)
        val query = GameplayQuery(
            addendCount = addendCount,
            addendSumMultiplier = randomInt(),
            minAddend = minAddend,
            maxAddend = maxAddend
        )
        val datasource = GameplayLocalDatasource()

        val gameplay = datasource.get(query).getOrNull()!!

        gameplay.apply {
            addends.forEach {
                assertTrue((minAddend..maxAddend).contains(it))
            }
        }
        // TODO: There must be one more check to assert if sum can actually be achieved by summing addends
    }

    @Suppress("ForbiddenComment")
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `produced addends count matches requested addend count`() = runTest {
        val addendCount = randomInt(min = 1, max = 50)
        val minAddend = randomInt(max = 0)
        val maxAddend = randomInt(min = 1)
        val query = GameplayQuery(
            addendCount = addendCount,
            addendSumMultiplier = randomInt(),
            minAddend = minAddend,
            maxAddend = maxAddend
        )
        val datasource = GameplayLocalDatasource()

        val gameplayList = buildList {
            repeat(randomInt(min = 200, max = 300)) {
                add(datasource.get(query).getOrNull()!!)
            }
        }

        gameplayList.forEach {
            it.apply {
                assertEquals(addendCount, addends.size)
            }
        }
    }
}
