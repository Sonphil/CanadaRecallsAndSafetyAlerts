package com.sonphil.canadarecallsandsafetyalerts.ext

import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sonphil.canadarecallsandsafetyalerts.R

/**
 * Created by Sonphil on 08-05-19.
 */

fun SwipeRefreshLayout.applyAppTheme(context: Context) {
    val bgColor = context.getColorFromAttr(R.attr.colorSurface)

    setProgressBackgroundColorSchemeColor(bgColor)
    setColorSchemeResources(R.color.colorPrimary)
}
