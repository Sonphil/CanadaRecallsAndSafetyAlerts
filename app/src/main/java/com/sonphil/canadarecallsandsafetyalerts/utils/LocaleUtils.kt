package com.sonphil.canadarecallsandsafetyalerts.utils

import android.content.Context
import android.os.Build
import java.util.*

/**
 * Created by Sonphil on 01-02-20.
 */

object LocaleUtils {
    fun getCurrentLocale(context: Context): Locale {
        val config = context.resources.configuration

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            config.locale
        }
    }
    
    fun getCurrentLanguage(context: Context): String = getCurrentLocale(context).language
}