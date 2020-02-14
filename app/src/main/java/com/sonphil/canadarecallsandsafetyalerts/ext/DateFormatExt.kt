package com.sonphil.canadarecallsandsafetyalerts.ext

import java.text.DateFormat
import java.util.*

/**
 * Created by Sonphil on 14-02-20.
 */

fun DateFormat.formatUTC(unixTimestampInMilliseconds: Long): String {
    val UTCTimeZone = TimeZone.getTimeZone("UTC")

    return formatWithTimeZone(UTCTimeZone, unixTimestampInMilliseconds)
}

fun DateFormat.formatDefaultTimeZone(unixTimestampInMilliseconds: Long): String {
    return formatWithTimeZone(TimeZone.getDefault(), unixTimestampInMilliseconds)
}

private fun DateFormat.formatWithTimeZone(
    timeZone: TimeZone,
    unixTimestampInMilliseconds: Long
): String {
    this.timeZone = timeZone

    return format(Date(unixTimestampInMilliseconds))
}