package com.mecofarid.summy.features.game

import com.mecofarid.summy.common.data.DataException
import com.mecofarid.summy.common.data.Operation
import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.data.repository.GetRepository
import com.mecofarid.summy.common.either.Either
import com.mecofarid.summy.features.game.data.source.local.GameplayLocalDatasource
import com.mecofarid.summy.features.game.domain.interactor.GetGameStateInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetGameplayInteractor
import com.mecofarid.summy.features.game.domain.model.Gameplay

interface GameComponent{
    fun getGameplayInteractor(): GetGameplayInteractor
    fun getGamepStateInteractor(): GetGameStateInteractor
}

class GameModule: GameComponent{

    private val repository by lazy {
        val datasource = GameplayLocalDatasource()
        object : GetRepository<Gameplay, DataException>{
            override suspend fun get(query: Query, operation: Operation): Either<DataException, Gameplay> =
                datasource.get(query)
        }
    }

    override fun getGameplayInteractor(): GetGameplayInteractor =
        GetGameplayInteractor(repository)

    override fun getGamepStateInteractor(): GetGameStateInteractor =
        GetGameStateInteractor()
}
