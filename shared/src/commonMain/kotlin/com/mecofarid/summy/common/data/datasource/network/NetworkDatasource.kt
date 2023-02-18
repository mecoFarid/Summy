package com.mecofarid.summy.common.data.datasource.network

import com.mecofarid.summy.common.data.Mapper
import com.mecofarid.summy.common.data.NetworkException
import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.data.datasource.GetDatasource
import com.mecofarid.summy.common.data.datasource.PutDatasource
import com.mecofarid.summy.common.either.Either
import com.mecofarid.summy.common.either.asEither

class PutNetworkDatasource<I, O, E>(
    private val putService: PutNetworkService<I, O>,
    private val exceptionMapper: Mapper<NetworkException, E>
): PutDatasource<I, O, E> {

    override suspend fun put(query: Query, data: I): Either<E, O> =
        executeRequest(exceptionMapper) { putService.put(query, data) }
}

class GetNetworkDatasource<T, E>(
    private val putService: GetNetworkService<T>,
    private val exceptionMapper: Mapper<NetworkException, E>
): GetDatasource<T, E> {

    override suspend fun get(query: Query): Either<E, T> =
        executeRequest(exceptionMapper) { putService.get(query) }

}

class DeleteNetworkDatasource<T, E>(
    private val putService: DeleteNetworkService<T>,
    private val exceptionMapper: Mapper<NetworkException, E>
): GetDatasource<T, E> {

    override suspend fun get(query: Query): Either<E, T> =
        executeRequest(exceptionMapper) { putService.delete(query) }

}

private inline fun <T, E> executeRequest(
    exceptionMapper: Mapper<NetworkException, E>,
    block: () -> T
): Either<E, T> =
    asEither<NetworkException, T> {
        block()
    }.mapLeft {
        exceptionMapper.map(it)
    }
