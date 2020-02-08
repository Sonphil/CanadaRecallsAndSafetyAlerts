package com.sonphil.canadarecallsandsafetyalerts.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Created by Sonphil on 08-02-20.
 */

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

fun Context.openUrlExternal(@StringRes stringId: Int) {
    val uri = Uri.parse(getString(stringId))

    uri.openExternal(this)
}

fun Fragment.openUrlExternal(@StringRes stringId: Int) = context?.openUrlExternal(stringId)