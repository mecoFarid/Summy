package com.mecofarid.summy.common.data.datasource

import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.either.Either

interface GetDatasource<T, E> {
    suspend fun get(query: Query): Either<E, T>
}

/**
 * It is common to persist specific type of data and receive different type of data as return.
 * That is why [RequestType] and [ReturnType] are specified
 */
interface PutDatasource<RequestType, ReturnType, E> {
    suspend fun put(query: Query, data: RequestType): Either<E, ReturnType>
}

/**
 * It is common to request deletion and receive specific type of data as return.
 * That is [T] is specified
 */
interface DeleteDatasource<T, E> {
    suspend fun delete(query: Query): Either<E, T>
}
