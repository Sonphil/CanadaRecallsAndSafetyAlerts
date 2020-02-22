package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.ext.applyAppTheme
import com.sonphil.canadarecallsandsafetyalerts.ext.formatDefaultTimeZone
import com.sonphil.canadarecallsandsafetyalerts.ext.formatUTC
import com.sonphil.canadarecallsandsafetyalerts.ext.open
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_recall_details.*
import java.text.DateFormat
import javax.inject.Inject


class RecallDetailsActivity : DaggerAppCompatActivity() {

    private val viewModel: RecallDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RecallDetailsViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var localeUtils: LocaleUtils
    @Inject
    lateinit var dateUtils: DateUtils

    private val dateFormat by lazy { dateUtils.getDateFormat(DateFormat.LONG) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recall_details)

        val recall = getRecall()

        if (recall == null) {
            finish()
        } else {
            setupWindow()
            bindRecallCategory(recall)
            tv_recall_title.text = recall.title
            bindRecallPublicationDate(recall)
            btn_back.setOnClickListener { onBackPressed() }
            btn_recall_bookmark.setOnClickListener { viewModel.clickBookmark() }
            swipe_refresh_layout_recall_details.setupSwipeRefreshLayout()
            setupBottomAppBar()
            subscribeUI()
        }
    }

    fun getRecall(): Recall? = intent?.extras?.run {
        RecallDetailsActivityArgs.fromBundle(this).recall
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
        swipe_refresh_layout_recall_details.addTopInsetToTopMargin()
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
            tv_recall_date.text = dateFormat.formatUTC(date)
        }
    }

    private fun SwipeRefreshLayout.setupSwipeRefreshLayout() {
        applyAppTheme(context)
        setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupBottomAppBar() {
        bottom_app_bar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_link -> {
                    viewModel.clickRecallUrl()

                    true
                }
                R.id.item_share -> {
                    viewModel.clickShareUrl()

                    true
                }
                else -> false
            }
        }
    }

    private fun subscribeUI() {
        viewModel.bookmarked.observe(this, Observer { bookmarked ->
            if (bookmarked) {
                btn_recall_bookmark.setImageResource(R.drawable.ic_bookmark_red_24dp)
            } else {
                btn_recall_bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp)
            }
        })

        viewModel.bookmarkDate.observe(this, Observer { date ->
            if (date != null) {
                divider_recall_dates.isVisible = true
                tv_recall_bookmark_date.isVisible = true

                val dateStr = dateFormat.formatDefaultTimeZone(date)

                tv_recall_bookmark_date.text = String.format(
                    getString(R.string.label_bookmarked_on),
                    dateStr
                )
            } else {
                divider_recall_dates.isVisible = false
                tv_recall_bookmark_date.isVisible = false
            }
        })

        viewModel.loading.observe(this, Observer { loading ->
            swipe_refresh_layout_recall_details.isRefreshing = loading
        })

        viewModel.navigateToUrl.observe(this, EventObserver { uri ->
            uri.open(this)
        })

        viewModel.shareUrl.observe(this, EventObserver { url ->
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)

            startActivity(shareIntent)
        })

        viewModel.images.observe(this, Observer { images ->
            val image = images.takeIf { !it.isNullOrEmpty() }?.first()

            if (image != null) {
                val thumbnailRequest: RequestBuilder<Drawable> = Glide
                    .with(this)
                    .load(Uri.parse(image.thumbUrl))

                Glide.with(this)
                    .load(Uri.parse(image.fullUrl))
                    .centerCrop()
                    .thumbnail(thumbnailRequest)
                    .into(iv_recall_details)

                iv_recall_details.isVisible = true
            }

            // TODO: Let user slide between images
        })
    }
}
