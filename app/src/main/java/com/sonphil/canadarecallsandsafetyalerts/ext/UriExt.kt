package com.sonphil.canadarecallsandsafetyalerts.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.sonphil.canadarecallsandsafetyalerts.R

/**
 * Created by Sonphil on 08-02-20.
 */

fun Uri.open(context: Context) {
    try {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setToolbarColor(context.getColorCompat(R.color.colorPrimary))
            .build()

        customTabsIntent.launchUrl(context, this)
    } catch (e: Exception) {
        openExternal(context)
    }
}

fun Uri.openExternal(context: Context) {
    try {
        with(Intent(Intent.ACTION_VIEW, this)) {
            if (resolveActivity(context.packageManager) != null) {
                context.startActivity(this)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openUrl(@StringRes stringId: Int) {
    val uri = Uri.parse(getString(stringId))

    uri.open(this)
}

fun Context.openUrlExternal(@StringRes stringId: Int) {
    val uri = Uri.parse(getString(stringId))

    uri.openExternal(this)
}

fun Fragment.openUrl(@StringRes stringId: Int) = context?.openUrl(stringId)

fun Fragment.openUrlExternal(@StringRes stringId: Int) = context?.openUrlExternal(stringId)