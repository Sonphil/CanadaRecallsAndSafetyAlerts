package com.sonphil.canadarecallsandsafetyalerts.utils

import android.content.Context
import android.os.Build

/**
 * Created by Sonphil on 01-02-20.
 */

object LocaleUtils {
    fun getCurrentLanguage(context: Context): String {
        val config = context.resources.configuration

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.get(0).language
        } else {
            @Suppress("DEPRECATION")
            config.locale.language
        }
    }
}