package com.mecofarid.summy.common.presentation

import kotlinx.coroutines.CoroutineScope

expect open class PlatformViewModel() {
    val scope: CoroutineScope
}
