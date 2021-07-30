package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentRecentRecallsBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.IncludeCategoriesFilterBinding
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.ext.applyAppTheme
import com.sonphil.canadarecallsandsafetyalerts.ext.getDrawableCompat
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.BaseRecallsFragment
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class RecentRecallsFragment : BaseRecallsFragment() {

    private var binding: FragmentRecentRecallsBinding by viewLifecycle()
    private lateinit var mainActivityBinding: ActivityMainBinding
    private var _categoriesFilterBinding: IncludeCategoriesFilterBinding? = null
    private val categoriesFilterBinding get() = _categoriesFilterBinding!!

    override val currentDestinationId = R.id.fragment_recent
    val viewModel: RecentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentRecallsBinding.inflate(layoutInflater, container, false)

        mainActivityBinding = (requireActivity() as MainActivity).binding

        _categoriesFilterBinding = IncludeCategoriesFilterBinding.inflate(
            layoutInflater,
            mainActivityBinding.root
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayoutRecentRecalls.setupSwipeRefreshLayout()

        setupFilter()

        subscribeUI()

        mainActivityBinding.includeEmptyView.btnRetry.setOnClickListener {
            viewModel.refresh()
        }
    }

    override fun onOtherDestinationSelected() {
        with(mainActivityBinding.root) {
            removeView(categoriesFilterBinding.cardViewCategoriesFilter)
            removeView(categoriesFilterBinding.btnFilterRecalls)
        }
    }

    override fun getRecyclerView() = binding.rvRecentRecalls

    override fun getNavigateToDetailsEventLiveData(): LiveData<Event<RecallAndBookmarkAndReadStatus>> {
        return viewModel.navigateToDetails
    }

    override fun onItemClicked(item: RecallAndBookmarkAndReadStatus) {
        viewModel.onRecallClicked(item)
    }

    override fun onBookmarkClicked(recall: Recall, isCurrentlyBookmarked: Boolean) {
        viewModel.onBookmarkClicked(recall, isCurrentlyBookmarked)
    }

    private fun getEmptyView() = mainActivityBinding.includeEmptyView.emptyView

    private fun setupFilter() {
        with(categoriesFilterBinding) {
            btnFilterRecalls.setOnClickListener {
                btnFilterRecalls.isExpanded = true
            }

            btnCategoriesFilterDone.setOnClickListener {
                btnFilterRecalls.isExpanded = false
            }

            val categoryResources = Category.values().map {
                CategoryResources(it)
            }.sortedBy { categoryResources ->
                getString(categoryResources.labelId)
            }

            val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                val category = buttonView.tag as? Category ?: return@OnCheckedChangeListener

                viewModel.updateCategoryFilter(category, isChecked)
            }

            categoryResources.forEach {
                val chip = layoutInflater.inflate(
                    R.layout.chip_category_filter,
                    chipGroupCategoryFilter,
                    false
                ) as? Chip

                chip?.tag = it.category
                chip?.setText(it.labelId)

                chip?.setOnCheckedChangeListener(listener)

                chipGroupCategoryFilter.addView(chip)
            }
        }
    }

    private fun SwipeRefreshLayout.setupSwipeRefreshLayout() {
        applyAppTheme(requireContext())
        binding.swipeRefreshLayoutRecentRecalls.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun subscribeUI() {
        viewModel.emptyViewVisible.observe(
            viewLifecycleOwner,
            { visible ->
                getRecyclerView().isVisible = !visible
                getEmptyView().isVisible = visible
            }
        )

        viewModel.recentRecalls.observe(
            viewLifecycleOwner,
            {
                adapter.submitList(it)
            }
        )

        viewModel.loading.observe(
            viewLifecycleOwner,
            { loading ->
                binding.swipeRefreshLayoutRecentRecalls.isRefreshing = loading
            }
        )

        viewModel.genericError.observe(
            viewLifecycleOwner,
            { error ->
                error?.let {
                    Toasty.error(requireContext(), error, Toast.LENGTH_SHORT, true).show()
                }
            }
        )

        viewModel.networkError.observe(
            viewLifecycleOwner,
            { error ->
                error?.let {
                    val icon = requireContext().getDrawableCompat(R.drawable.ic_cloud_off_white_24dp)

                    Toasty.normal(requireContext(), error, Toast.LENGTH_SHORT, icon).show()
                }
            }
        )

        viewModel.emptyViewIconResId.observe(
            viewLifecycleOwner,
            { iconId ->
                mainActivityBinding.includeEmptyView.ivEmpty.setImageResource(iconId)
            }
        )

        viewModel.emptyViewTitleResId.observe(
            viewLifecycleOwner,
            { titleId ->
                mainActivityBinding.includeEmptyView.tvTitleEmpty.setText(titleId)
            }
        )

        viewModel.emptyViewRetryButtonVisible.observe(
            viewLifecycleOwner,
            { visible ->
                mainActivityBinding.includeEmptyView.btnRetry.isVisible = visible
            }
        )

        viewModel.categoryFilters.observe(
            viewLifecycleOwner,
            { visibleCategories ->
                categoriesFilterBinding.chipGroupCategoryFilter.forEach { view ->
                    if (view is Chip) {
                        val category = view.tag as? Category

                        view.isChecked = category in visibleCategories
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        _categoriesFilterBinding = null

        super.onDestroyView()
    }
}
