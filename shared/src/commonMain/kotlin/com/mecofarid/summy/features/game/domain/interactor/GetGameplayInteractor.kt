package com.mecofarid.summy.features.game.domain.interactor

import com.mecofarid.summy.common.data.DataException
import com.mecofarid.summy.common.data.datasource.GetDatasource
import com.mecofarid.summy.common.either.Either
import com.mecofarid.summy.features.game.data.query.GameplayQuery
import com.mecofarid.summy.features.game.domain.model.Gameplay

class GetGameplayInteractor(
    private val datasource: GetDatasource<Gameplay, DataException>
){
    suspend operator fun invoke(query: GameplayQuery): Either<DataException, Gameplay> =
        datasource.get(query)
}
