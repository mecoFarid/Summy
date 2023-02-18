package com.mecofarid.summy.common.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatTime(
    pattern: String,
    locale: Locale
): String =
    SimpleDateFormat(pattern, locale).format(Date(this))
