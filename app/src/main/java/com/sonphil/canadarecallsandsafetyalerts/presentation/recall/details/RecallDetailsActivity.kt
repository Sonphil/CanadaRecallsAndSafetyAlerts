package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.transition.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransform.FADE_MODE_IN
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityRecallDetailsBinding
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.ext.applyAppTheme
import com.sonphil.canadarecallsandsafetyalerts.ext.doApplyInsetsWhenAttached
import com.sonphil.canadarecallsandsafetyalerts.ext.doApplyTopInsetToTopMarginWhenAttached
import com.sonphil.canadarecallsandsafetyalerts.ext.formatUTC
import com.sonphil.canadarecallsandsafetyalerts.ext.getColorCompat
import com.sonphil.canadarecallsandsafetyalerts.ext.getDimensionFromAttr
import com.sonphil.canadarecallsandsafetyalerts.ext.open
import com.sonphil.canadarecallsandsafetyalerts.ext.viewBinding
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.UltraViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.text.DateFormat
import javax.inject.Inject

@AndroidEntryPoint
class RecallDetailsActivity : AppCompatActivity() {

    private val binding: ActivityRecallDetailsBinding by viewBinding(ActivityRecallDetailsBinding::inflate)
    private val viewModel: RecallDetailsViewModel by viewModels()

    @Inject
    lateinit var localeUtils: LocaleUtils

    @Inject
    lateinit var dateUtils: DateUtils

    private val dateFormat by lazy { dateUtils.getDateFormat(DateFormat.LONG) }
    private val recallDetailsImagePagerAdapter = RecallDetailsImagePagerAdapter()
    private val adapter = RecallDetailsSectionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setupSharedElementTransition()
        }

        setupWindow()
        setupBackButton()
        setupUltraViewPager()
        binding.btnRecallBookmark.setOnClickListener { viewModel.clickBookmark() }
        binding.swipeRefreshLayoutRecallDetails.setupSwipeRefreshLayout()
        setupRecyclerView()
        setupBottomAppBar()
        bindRecallCategory(viewModel.recall)
        binding.tvRecallTitle.text = viewModel.recall.title
        bindRecallPublicationDate(viewModel.recall)
        subscribeUI()
    }

    private fun setupSharedElementTransition() {
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        val transitionName = getString(R.string.name_recall_details_shared_element_transition)
        ViewCompat.setTransitionName(binding.root, transitionName)
        val transform = MaterialContainerTransform().apply {
            addTarget(binding.root)
            duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            pathMotion = MaterialArcMotion()
            fadeMode = FADE_MODE_IN
            isElevationShadowEnabled = false
        }
        window.sharedElementEnterTransition = transform

        window.sharedElementEnterTransition.addListener(
            onStart = {
                binding.bottomAppBar.performHide()
            },
            onEnd = {
                binding.bottomAppBar.performShow()
            }
        )

        window.sharedElementReturnTransition = transform
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

    private fun setupUltraViewPager() {
        val ultraViewPager = binding.ultraViewpagerRecallDetailsImages
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        val adapter = UltraViewPagerAdapter(recallDetailsImagePagerAdapter)
        ultraViewPager.adapter = adapter
        ultraViewPager.setupRecallDetailsImageIndicator()
        ultraViewPager.setInfiniteLoop(true)
        ultraViewPager.setAutoScroll(7000)
    }

    private fun UltraViewPager.setupRecallDetailsImageIndicator() {
        initIndicator()
        indicator
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(context.getColorCompat(R.color.colorViewPagerIndicatorFocused))
            .setNormalColor(context.getColorCompat(R.color.colorViewPagerIndicatorNormal))
            .setMargin(
                0,
                0,
                0,
                resources.getDimensionPixelSize(R.dimen.recall_details_images_view_pager_indicator_bottom_margin)
            )
            .setRadius(resources.getDimensionPixelSize(R.dimen.recall_details_images_view_pager_indicator_radius))
            .setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
            .build()
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

        viewModel.bookmarked.observe(
            this,
            { bookmarked ->
                if (bookmarked) {
                    binding.btnRecallBookmark.setImageResource(R.drawable.ic_bookmark_red_24dp)
                } else {
                    binding.btnRecallBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp)
                }
            }
        )

        viewModel.loading.observe(
            this,
            { loading ->
                binding.swipeRefreshLayoutRecallDetails.isRefreshing = loading
            }
        )

        viewModel.navigateToUrl.observe(
            this,
            EventObserver { uri ->
                uri.open(this)
            }
        )

        viewModel.shareUrl.observe(
            this,
            EventObserver { url ->
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, url)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)

                startActivity(shareIntent)
            }
        )

        viewModel.images.observe(
            this,
            { images ->
                if (!images.isNullOrEmpty()) {
                    recallDetailsImagePagerAdapter.recallImages = images
                    val viewPager = binding.ultraViewpagerRecallDetailsImages
                    viewPager.refresh()

                    if (images.size > 1) {
                        if (viewPager.indicator == null) {
                            viewPager.setupRecallDetailsImageIndicator()
                        }
                    } else {
                        viewPager.disableIndicator()
                    }
                }
            }
        )

        viewModel.galleryVisible.observe(
            this,
            { visible ->
                binding.ultraViewpagerRecallDetailsImages.isVisible = visible

                binding.constraintLayoutRecallDetails.doApplyInsetsWhenAttached { view, windowInsets ->

                    if (visible) {
                        view.updatePadding(top = 0)
                    } else {
                        val toolbarHeight = getDimensionFromAttr(R.attr.actionBarSize)
                        view.updatePadding(top = toolbarHeight + windowInsets.systemWindowInsetTop)
                    }
                }
            }
        )

        viewModel.menuItemsVisible.observe(
            this,
            { visible ->
                binding.bottomAppBar.menu.children.forEach { it.isVisible = visible }
            }
        )

        viewModel.detailsSectionsItems.observe(
            this,
            { sections ->
                adapter.submitList(sections)
            }
        )

        viewModel.error.observe(
            this,
            { error: String? ->
                error?.let {
                    Toasty.normal(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
