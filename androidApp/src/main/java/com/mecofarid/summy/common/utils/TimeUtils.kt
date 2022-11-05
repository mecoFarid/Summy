package com.mecofarid.summy.common.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

fun Long.formatTime(
    pattern: String,
    locale: Locale
): String = SimpleDateFormat(pattern, locale).format(Date(this))