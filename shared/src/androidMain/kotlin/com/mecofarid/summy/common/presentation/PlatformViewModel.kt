package com.mecofarid.summy.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class PlatformViewModel: ViewModel() {
    actual val scope: CoroutineScope = viewModelScope
}
