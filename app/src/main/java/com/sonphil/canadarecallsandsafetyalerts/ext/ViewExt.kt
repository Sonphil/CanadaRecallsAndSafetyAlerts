package com.sonphil.canadarecallsandsafetyalerts.ext

import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.doOnAttach
import androidx.core.view.marginTop

fun View.doApplyInsetsWhenAttached(applyInsets: (View, WindowInsets) -> Unit) {
    doOnAttach {
        applyInsets(this, rootWindowInsets)
    }
}

fun View.doApplyInsetsToMarginsWhenAttached(applyInsetsToMargins: (View, WindowInsets) -> Unit) {
    doApplyInsetsWhenAttached { view, windowInsets ->
        applyInsetsToMargins(view, windowInsets)
    }
}

fun View.doApplyTopInsetToTopMarginWhenAttached() {
    doApplyInsetsToMarginsWhenAttached { view, windowInsets ->
        val params = view.layoutParams as ViewGroup.MarginLayoutParams

        params.topMargin = view.marginTop + windowInsets.systemWindowInsetTop
        view.layoutParams = params
    }
}