package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_recall_details.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecallDetailsActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recall_details)

        val recall = intent?.extras?.run {
            RecallDetailsActivityArgs.fromBundle(this).recall
        }

        if (recall == null) {
            finish()
        } else {
            bindRecallCategory(recall)
            tv_recall_title.text = recall.title
            bindRecallPublicationDate(recall)
            setupWindow()
            btn_back.setOnClickListener { onBackPressed() }
        }
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

        btn_back.addTopInsetToTopMargin()
        scroll_view_recall_details.addTopInsetToTopMargin()
    }

    private fun View.addTopInsetToTopMargin() {
        val params = layoutParams as ViewGroup.MarginLayoutParams

        params.topMargin = params.topMargin + window.decorView.rootWindowInsets.stableInsetTop
        this.layoutParams = params
    }

    private fun bindRecallCategory(recall: Recall) {
        val resources = CategoryResources(recall.category)

        iv_recall_category_icon.setImageResource(resources.iconId)
        tv_recall_category.setText(resources.labelId)
    }

    private fun bindRecallPublicationDate(recall: Recall) {
        recall.datePublished?.let { date ->
            val dateFormat = SimpleDateFormat.getDateInstance(
                DateFormat.LONG,
                LocaleUtils.getCurrentLocale(this)
            )

            tv_recall_date.text = dateFormat.format(Date(date))
        }
    }
}
