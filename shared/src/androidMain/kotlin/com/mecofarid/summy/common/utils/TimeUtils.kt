package com.mecofarid.summy.common.utils

import java.text.SimpleDateFormat
import java.util.*

actual fun Long.formatTime(
    pattern: String,
): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))
