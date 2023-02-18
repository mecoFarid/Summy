package com.mecofarid.summy.common.data

sealed class NetworkException(override val cause: Throwable? = null): DataException(cause) {
    data class HttpException(val code: Int): NetworkException()
    data class ConnectionException(override val cause: Throwable? = null): NetworkException(cause)
}
