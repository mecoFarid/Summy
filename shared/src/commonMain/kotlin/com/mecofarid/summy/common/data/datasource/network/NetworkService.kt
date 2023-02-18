package com.mecofarid.summy.common.data.datasource.network

import com.mecofarid.summy.common.data.Query

interface PutNetworkService<I, O> {
    suspend fun put(query: Query, data: I): O
}

interface GetNetworkService<T> {
    suspend fun get(query: Query): T
}

interface DeleteNetworkService<T> {
    suspend fun delete(query: Query): T
}
