package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallImage
import com.sonphil.canadarecallsandsafetyalerts.ext.*
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_recall_details.*
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView
import technolifestyle.com.imageslider.pagetransformers.ZoomOutPageTransformer
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
    private val constraintLayoutInitialTopMargin by lazy {
        constraint_layout_recall_details.marginTop
    }

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
            setupBackButton()
            flipper_layout.setupFlipperLayout()
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

    private fun setupBackButton() {
        btn_back.setOnClickListener { onBackPressed() }
        btn_back.doApplyTopInsetToTopMarginWhenAttached()
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

    private fun FlipperLayout.setupFlipperLayout() {
        removeAutoCycle()
        showInnerPagerIndicator()
        addPageTransformer(false, ZoomOutPageTransformer())
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
            if (!images.isNullOrEmpty()) {
                fillImagesGallery(images)
            }
        })

        viewModel.galleryVisible.observe(this, Observer { visible ->
            flipper_layout.isVisible = visible

            constraint_layout_recall_details.doApplyInsetsWhenAttached { view, windowInsets ->
                val params = view.layoutParams as ViewGroup.MarginLayoutParams

                if (visible) {
                    params.topMargin = constraintLayoutInitialTopMargin
                } else {
                    val toolbarHeight = getDimensionFromAttr(R.attr.actionBarSize)
                    params.topMargin = constraintLayoutInitialTopMargin + toolbarHeight + windowInsets.systemWindowInsetTop
                }

                view.layoutParams = params
            }
        })
    }

    private fun ImageView.loadWithGlide(recallImage: RecallImage) {
        val thumbnailRequest: RequestBuilder<Drawable> = Glide
            .with(this)
            .load(recallImage.thumbUrl)

        Glide.with(this)
            .load(recallImage.fullUrl)
            .optionalCenterCrop()
            .thumbnail(thumbnailRequest)
            .into(this)
    }

    private fun fillImagesGallery(recallImages: List<RecallImage>?) {
        recallImages
            .orEmpty()
            .map { recallImage ->
                FlipperView(this).apply {
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    setDescriptionBackgroundAlpha(0f)
                    fun setFlipperImage(flipperImageView: ImageView, imageUrl: String) {
                        flipperImageView.loadWithGlide(recallImage)
                    }
                    setImageUrl(recallImage.fullUrl, ::setFlipperImage)
                }
            }
            .apply {
                flipper_layout.removeAllFlipperViews()
                flipper_layout.addFlipperViewList(this)
            }
    }
}
