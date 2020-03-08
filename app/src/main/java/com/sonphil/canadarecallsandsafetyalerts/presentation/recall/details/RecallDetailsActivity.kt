package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityRecallDetailsBinding
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallImage
import com.sonphil.canadarecallsandsafetyalerts.ext.*
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import dagger.android.support.DaggerAppCompatActivity
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView
import technolifestyle.com.imageslider.pagetransformers.ZoomOutPageTransformer
import java.text.DateFormat
import javax.inject.Inject

class RecallDetailsActivity : DaggerAppCompatActivity() {

    private val binding: ActivityRecallDetailsBinding by viewBinding(ActivityRecallDetailsBinding::inflate)
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
    private val adapter = RecallDetailsSectionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recall = getRecall()

        if (recall == null) {
            finish()
        } else {
            setupWindow()
            bindRecallCategory(recall)
            binding.tvRecallTitle.text = recall.title
            bindRecallPublicationDate(recall)
            setupBackButton()
            binding.flipperLayout.setupFlipperLayout()
            binding.btnRecallBookmark.setOnClickListener { viewModel.clickBookmark() }
            binding.swipeRefreshLayoutRecallDetails.setupSwipeRefreshLayout()
            setupRecyclerView()
            setupBottomAppBar()
            subscribeUI()
        }
    }

    fun getRecall(): Recall? = intent?.extras?.run {
        RecallDetailsActivityArgs.fromBundle(this).recall
    }

    private fun setupWindow() {
        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnBack.doApplyTopInsetToTopMarginWhenAttached()
    }

    private fun bindRecallCategory(recall: Recall) {
        val resources = CategoryResources(recall.category)

        binding.ivRecallCategoryIcon.setImageResource(resources.iconId)
        binding.tvRecallCategory.setText(resources.labelId)
    }

    private fun bindRecallPublicationDate(recall: Recall) {
        recall.datePublished?.let { date ->
            binding.tvRecallDate.text = dateFormat.formatUTC(date)
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
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)

        with(binding.rvRecallDetails) {
            this.layoutManager = layoutManager
            adapter = this@RecallDetailsActivity.adapter
        }
    }

    private fun setupBottomAppBar() {
        binding.bottomAppBar.setOnMenuItemClickListener { item ->
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
                binding.btnRecallBookmark.setImageResource(R.drawable.ic_bookmark_red_24dp)
            } else {
                binding.btnRecallBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp)
            }
        })

        viewModel.bookmarkDate.observe(this, Observer { date ->
            if (date != null) {
                binding.dividerRecallDates.isVisible = true
                binding.tvRecallBookmarkDate.isVisible = true

                val dateStr = dateFormat.formatDefaultTimeZone(date)

                binding.tvRecallBookmarkDate.text = String.format(
                    getString(R.string.label_bookmarked_on),
                    dateStr
                )
            } else {
                binding.dividerRecallDates.isVisible = false
                binding.tvRecallBookmarkDate.isVisible = false
            }
        })

        viewModel.loading.observe(this, Observer { loading ->
            binding.swipeRefreshLayoutRecallDetails.isRefreshing = loading
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
            binding.flipperLayout.isVisible = visible

            binding.constraintLayoutRecallDetails.doApplyInsetsWhenAttached { view, windowInsets ->

                if (visible) {
                    view.updatePadding(top = 0)
                } else {
                    val toolbarHeight = getDimensionFromAttr(R.attr.actionBarSize)
                    view.updatePadding(top = toolbarHeight + windowInsets.systemWindowInsetTop)
                }
            }
        })

        viewModel.menuItemsVisible.observe(this, Observer { visible ->
            binding.bottomAppBar.menu.children.forEach { it.isVisible = visible }
        })

        viewModel.detailsSectionsItems.observe(this, Observer { sections ->
            adapter.submitList(sections)
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
            .also { flipperViews ->
                with(binding.flipperLayout) {
                    removeAllFlipperViews()
                    if (flipperViews.count() < 2) {
                        removeCircleIndicator()
                    } else {
                        showCircleIndicator()
                    }
                    addFlipperViewList(flipperViews)
                }
            }
    }
}
