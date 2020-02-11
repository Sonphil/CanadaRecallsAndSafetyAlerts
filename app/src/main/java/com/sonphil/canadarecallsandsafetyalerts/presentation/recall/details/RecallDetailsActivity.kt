package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.os.Bundle
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerAppCompatActivity

class RecallDetailsActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_recall_details)

        intent?.extras ?.let { bundle ->
            val recall = RecallDetailsActivityArgs.fromBundle(bundle).recall
        }
    }
}
