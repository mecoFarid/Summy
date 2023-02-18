package com.mecofarid.summy.features.game.data.source.local

import com.mecofarid.summy.common.data.DataException
import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.data.datasource.GetDatasource
import com.mecofarid.summy.common.either.Either
import com.mecofarid.summy.features.game.data.query.GameplayQuery
import com.mecofarid.summy.features.game.domain.model.Gameplay
import com.mecofarid.summy.features.game.utils.randomInt


class GameplayLocalDatasource: GetDatasource<Gameplay, DataException> {

    override suspend fun get(query: Query): Either<Nothing, Gameplay> =
        when (query) {
            is GameplayQuery -> {
                val addends = query.getAddends()
                Either.Right(Gameplay(addends, query.getTarget(addends)))
            }
            else -> error("Implementation not found for $query")
        }

    private fun GameplayQuery.getAddends() =
        buildList {
            repeat(addendCount) {
                add(randomInt(min = minAddend, max = maxAddend))
            }
        }

    private fun GameplayQuery.getTarget(addends: List<Int>): Int {
        var sum = addends.sum() * addendSumMultiplier

        val randomAddendCount = randomInt(min = addends.size / 2, max = addends.size)
        // Add random addends to make target less predictable

        repeat(randomAddendCount) {
            sum += addends.random()
        }
        return sum
    }
}
