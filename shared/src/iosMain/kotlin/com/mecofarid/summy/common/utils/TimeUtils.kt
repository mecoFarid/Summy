package com.mecofarid.summy.common.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.dateWithTimeIntervalSince1970

private const val MILLIS = 1000.0

fun Long.formatTime(
    pattern: String,
    locale: NSLocale
): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.locale = locale
    dateFormatter.dateFormat = pattern
    return dateFormatter.stringFromDate(NSDate.dateWithTimeIntervalSince1970(this / MILLIS))
}
