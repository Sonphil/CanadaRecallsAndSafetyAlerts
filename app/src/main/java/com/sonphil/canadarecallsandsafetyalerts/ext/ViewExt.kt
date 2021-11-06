package com.sonphil.canadarecallsandsafetyalerts.ext

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

fun View.doApplyTopInsetToTopMargin() {
    doOnApplyWindowInsets { view, windowInsets, _, initialMargin ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(
                initialMargin.left,
                initialMargin.top + insets.top,
                initialMargin.right,
                initialMargin.bottom
            )
        }
    }
}

fun View.doApplyBottomInsetToBottomMargin() {
    doOnApplyWindowInsets { view, windowInsets, _, initialMargin ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(
                initialMargin.left,
                initialMargin.top,
                initialMargin.right,
                initialMargin.bottom + insets.bottom
            )
        }
    }
}

fun View.doApplyInsetsToBottomPadding() {
    doOnApplyWindowInsets { view, windowInsets, initialPadding, _ ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updatePadding(
            left = initialPadding.left,
            top = initialPadding.top,
            right = initialPadding.right,
            bottom = initialPadding.bottom + insets.top + insets.bottom
        )
    }
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialPadding, InitialMargin) -> Unit) {
    // Create a snapshot of the view's padding/margin state
    val initialPadding = this.recordInitialPadding()
    val initialMargin = this.recordInitialMargin()
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        f(view, windowInsets, initialPadding, initialMargin)

        // Always return the insets, so that children can also use them
        windowInsets
    }
    // Request some insets on attach
    doOnAttach { requestApplyInsets() }
}

class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun View.recordInitialPadding() = InitialPadding(
    left = this.paddingLeft,
    top = this.paddingTop,
    right = this.paddingRight,
    bottom = this.paddingBottom
)

private fun View.recordInitialMargin() = InitialMargin(
    left = this.marginLeft,
    top = this.marginTop,
    right = this.marginRight,
    bottom = this.marginBottom
)
