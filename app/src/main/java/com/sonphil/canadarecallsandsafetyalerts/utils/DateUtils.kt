package com.sonphil.canadarecallsandsafetyalerts.utils

import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Sonphil on 14-02-20.
 */

@Singleton
class DateUtils @Inject constructor(private val localeUtils: LocaleUtils) {
    fun getDateFormat(style: Int): DateFormat {
        return SimpleDateFormat.getDateInstance(
            style,
            localeUtils.getCurrentLocale()
        )
    }
}
