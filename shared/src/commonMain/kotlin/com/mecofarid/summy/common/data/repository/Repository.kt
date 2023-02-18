package com.mecofarid.summy.common.data.repository

import com.mecofarid.summy.common.data.Operation
import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.either.Either

interface PutRepository<I, O, E> {
    suspend fun put(query: Query, operation: Operation, data: I): Either<E, O>
}

interface GetRepository<T, E> {
    suspend fun get(query: Query, operation: Operation): Either<E, T>
}

interface DeleteRepository<T, E> {
    suspend fun delete(query: Query, operation: Operation): Either<E, T>
}
