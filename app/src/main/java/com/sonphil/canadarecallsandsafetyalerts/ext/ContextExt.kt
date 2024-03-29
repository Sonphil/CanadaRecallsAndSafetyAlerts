package com.sonphil.canadarecallsandsafetyalerts.ext

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

/**
 * Created by Sonphil on 01-02-20.
 */

/** True if the device is connected **/
inline val Context.isDeviceConnected: Boolean
    @RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

/**
 * Returns true when dark theme is on, false otherwise.
 */
inline val Context.isDarkModeOn: Boolean
    get() {
        val uiMode = resources.configuration.uiMode

        return uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

/**
 * Uses [ContextCompat] to return a color associated with a particular resource ID
 */
@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

/**
 * Uses [ContextCompat] to return a [Drawable] associated with a particular resource ID
 */
fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

/**
 * Returns color from attribute
 */
@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

/**
 * Returns dimension from attribute
 */
fun Context.getDimensionFromAttr(@AttrRes attrDimension: Int): Int {
    val styledAttributes: TypedArray = theme.obtainStyledAttributes(intArrayOf(attrDimension))
    val dimension = styledAttributes.getDimension(0, 0f).toInt()
    styledAttributes.recycle()

    return dimension
}
