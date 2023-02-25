package com.mecofarid.summy.common.data

import com.mecofarid.summy.common.data.datasource.DeleteDatasource
import com.mecofarid.summy.common.data.datasource.GetDatasource
import com.mecofarid.summy.common.data.datasource.PutDatasource
import com.mecofarid.summy.common.either.Either

class DatasourceMapper<I, O, E>(
    private val putDatasource: PutDatasource<I, I, E>,
    private val getDatasource: GetDatasource<I, E>,
    private val deleteDatasource: DeleteDatasource<I, E>,
    private val outMapper: Mapper<I, O>,
    private val inMapper: Mapper<O, I>
) : PutDatasource<O, O, E>, GetDatasource<O, E>, DeleteDatasource<O, E> {

    override suspend fun get(query: Query): Either<E, O> =
        getDatasource.get(query).map {
            outMapper.map(it)
        }

    override suspend fun put(query: Query, data: O): Either<E, O> =
        putDatasource.put(query, inMapper.map(data)).map {
            outMapper.map(it)
        }

    override suspend fun delete(query: Query): Either<E, O> =
        deleteDatasource.delete(query).map {
            outMapper.map(it)
        }
}
