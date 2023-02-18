package com.mecofarid.summy.common.data.repository.cache

import com.mecofarid.summy.common.data.Operation
import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.data.datasource.DeleteDatasource
import com.mecofarid.summy.common.data.datasource.GetDatasource
import com.mecofarid.summy.common.data.datasource.PutDatasource
import com.mecofarid.summy.common.data.repository.DeleteRepository
import com.mecofarid.summy.common.data.repository.GetRepository
import com.mecofarid.summy.common.data.repository.PutRepository
import com.mecofarid.summy.common.either.Either

class CacheRepository<T, E>(
    private val cachePutDatasource: PutDatasource<T, T, E>,
    private val cacheGetDatasource: GetDatasource<T, E>,
    private val cacheDeleteDatasource: DeleteDatasource<T, E>,
    private val mainPutDatasource: PutDatasource<T, T, E>,
    private val mainGetDatasource: GetDatasource<T, E>,
    private val mainDeleteDatasource: DeleteDatasource<T, E>,
) : PutRepository<T, T, E>, GetRepository<T, E>, DeleteRepository<T, E> {

    override suspend fun get(query: Query, operation: Operation): Either<E, T> =
        when (operation) {
            Operation.MainThenCache ->
                mainGetDatasource.get(query)
                    .onRight {
                        cachePutDatasource.put(query, it)
                        return getCachedData(query)
                    }
                    .onLeft {
                        return getCachedData(query)
                    }
            Operation.CacheThenMain ->
                getCachedData(query)
                    .onLeft {
                        return get(query, Operation.MainThenCache)
                    }

        }

    override suspend fun put(query: Query, operation: Operation, data: T): Either<E, T> =
        when (operation) {
            Operation.CacheThenMain ->
                cachePutDatasource.put(query, data)
                    .onRight {
                        return mainPutDatasource.put(query, data)
                    }
            Operation.MainThenCache ->
                mainPutDatasource.put(query, data)
                    .onRight {
                        return cachePutDatasource.put(query, data)
                    }
        }

    override suspend fun delete(query: Query, operation: Operation): Either<E, T> =
        when (operation) {
            Operation.CacheThenMain ->
                cacheDeleteDatasource.delete(query)
                    .onRight {
                        return mainDeleteDatasource.delete(query)
                    }
            Operation.MainThenCache ->
                mainDeleteDatasource.delete(query)
                    .onRight {
                        return cacheDeleteDatasource.delete(query)
                    }
        }

    private suspend fun getCachedData(query: Query): Either<E, T> {
        return cacheGetDatasource.get(query)
    }
}
