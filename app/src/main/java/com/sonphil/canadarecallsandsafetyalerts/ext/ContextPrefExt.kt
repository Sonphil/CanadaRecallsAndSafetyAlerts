package com.sonphil.canadarecallsandsafetyalerts.ext

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.sonphil.canadarecallsandsafetyalerts.R

fun Context.applyThemePref(modePreferenceValue: String?) {
    val nightMode = when (modePreferenceValue) {
        getString(R.string.value_theme_pref_dark) -> {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        getString(R.string.value_theme_pref_light) -> {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        getString(R.string.value_theme_pref_battery_saver) -> {
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    AppCompatDelegate.setDefaultNightMode(nightMode)
}
