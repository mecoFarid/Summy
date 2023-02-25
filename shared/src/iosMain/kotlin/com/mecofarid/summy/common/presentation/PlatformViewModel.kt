package com.mecofarid.summy.common.presentation

import com.mecofarid.summy.common.ext.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

actual open class PlatformViewModel {
    actual val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun clear() {
        scope.cancel()
    }

    fun <T> observe(flow: Flow<T>, onChanged: (T) -> Unit) {
        flow.observe(scope) {
            onChanged(it)
        }
    }
}
