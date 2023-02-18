package com.mecofarid.summy.common.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

// This timeout is taken from internal lifecycle library
internal const val DEFAULT_TIMEOUT = 5000L

fun <T> Flow<T>.observeAsLiveData(
    owner: LifecycleOwner,
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = DEFAULT_TIMEOUT,
    observer: Observer<T>
) = asLiveData(
        context,
        timeoutInMs
    ).observe(
        owner,
        observer
    )

