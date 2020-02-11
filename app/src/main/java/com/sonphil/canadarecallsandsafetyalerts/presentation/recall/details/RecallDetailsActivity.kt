package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.os.Bundle
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

        btn_back.setOnClickListener { onBackPressed() }
    }
}
