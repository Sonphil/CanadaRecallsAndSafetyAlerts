package com.sonphil.canadarecallsandsafetyalerts.domain.utils

import android.content.Context
import android.os.Build
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Sonphil on 01-02-20.
 */

@Singleton
class LocaleUtils @Inject constructor(private val context: Context) {
    fun getCurrentLocale(): Locale {
        val config = context.resources.configuration

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            config.locale
        }
    }

    fun getCurrentLanguage(): String = getCurrentLocale().language.takeIf { it == "fr" } ?: "en"
}
