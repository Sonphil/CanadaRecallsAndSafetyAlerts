package com.sonphil.canadarecallsandsafetyalerts.ext

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Created by Sonphil on 01-02-20.
 */

/**
 * Uses [ContextCompat] to return a color associated with a particular resource ID
 */
@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}