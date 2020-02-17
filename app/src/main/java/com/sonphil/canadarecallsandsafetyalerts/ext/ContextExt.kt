package com.sonphil.canadarecallsandsafetyalerts.ext

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false

            return nwInfo.isConnected
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