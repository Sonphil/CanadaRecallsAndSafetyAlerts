package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_recall_details.*

class RecallDetailsActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recall_details)

        val recall = intent?.extras?.run {
            RecallDetailsActivityArgs.fromBundle(this).recall
        }

        if (recall == null) {
            finish()
        }

        setupWindow()
        btn_back.setOnClickListener { onBackPressed() }
    }

    private fun setupWindow() {
        root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        addInsetTopToBackButtonTopMargin()
    }

    private fun addInsetTopToBackButtonTopMargin() {
        val params = btn_back.layoutParams as ViewGroup.MarginLayoutParams

        params.topMargin = params.topMargin + window.decorView.rootWindowInsets.stableInsetTop
        btn_back.layoutParams = params
    }
}
