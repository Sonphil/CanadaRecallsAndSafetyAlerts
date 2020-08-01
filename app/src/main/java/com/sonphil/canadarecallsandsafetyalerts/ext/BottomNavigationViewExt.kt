package com.sonphil.canadarecallsandsafetyalerts.ext

import android.animation.Animator
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setVisible(visible: Boolean, duration: Long = 200) {
    clearAnimation()
    animate()
        .translationY(
            when (visible) {
                true -> 0f
                false -> height.toFloat()
            }
        )
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                if (!visible) { // Need to hide view
                    // Set visibility to GONE at the end of animation
                    isVisible = false
                }
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationStart(animator: Animator) {
                if (visible) { // Need to reveal view
                    // Set visibility to VISIBLE at the beginning of animation
                    isVisible = true
                }
            }
        })
}
