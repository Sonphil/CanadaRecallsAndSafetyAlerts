package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentRecentRecallsBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.IncludeCategoriesFilterBinding
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.ext.applyAppTheme
import com.sonphil.canadarecallsandsafetyalerts.ext.getDrawableCompat
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.BaseRecallsFragment
import es.dmoral.toasty.Toasty

class RecentRecallsFragment : BaseRecallsFragment() {
    private var binding: FragmentRecentRecallsBinding by viewLifecycle()
    private lateinit var mainActivityBinding: ActivityMainBinding
    private var _categoriesFilterBinding: IncludeCategoriesFilterBinding? = null
    private val categoriesFilterBinding get() = _categoriesFilterBinding!!

    override val currentDestinationId = R.id.fragment_recent
    override val viewModel: RecentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RecentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        subscribeUI()

        binding.swipeRefreshLayoutRecentRecalls.setupSwipeRefreshLayout()

        setupFilter()

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

    override fun getEmptyView() = mainActivityBinding.includeEmptyView.emptyView

    private fun setupFilter() {
        with(categoriesFilterBinding) {
            btnFilterRecalls.setOnClickListener {
                btnFilterRecalls.isExpanded = true
            }

            btnCategoriesFilterDone.setOnClickListener {
                btnFilterRecalls.isExpanded = false
            }

            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                when (buttonView.id) {
                    R.id.chip_category_filter_food -> viewModel.updateCategoryFilter(
                        Category.FOOD,
                        isChecked
                    )
                    R.id.chip_category_filter_vehicle -> viewModel.updateCategoryFilter(
                        Category.VEHICLE,
                        isChecked
                    )
                    R.id.chip_category_filter_health_product -> viewModel.updateCategoryFilter(
                        Category.HEALTH_PRODUCT,
                        isChecked
                    )
                    R.id.chip_category_filter_consumer_product -> viewModel.updateCategoryFilter(
                        Category.CONSUMER_PRODUCT,
                        isChecked
                    )
                    R.id.chip_category_filter_miscellaneous -> viewModel.updateCategoryFilter(
                        Category.MISCELLANEOUS,
                        isChecked
                    )
                }
            }.let { listener ->
                chipGroupCategoryFilter.forEach { chip ->
                    (chip as Chip).setOnCheckedChangeListener(listener)
                }
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
                with(categoriesFilterBinding) {
                    chipCategoryFilterFood.isChecked = Category.FOOD in visibleCategories
                    chipCategoryFilterVehicle.isChecked = Category.VEHICLE in visibleCategories
                    chipCategoryFilterHealthProduct.isChecked = Category.HEALTH_PRODUCT in visibleCategories
                    chipCategoryFilterConsumerProduct.isChecked = Category.CONSUMER_PRODUCT in visibleCategories
                    chipCategoryFilterMiscellaneous.isChecked = Category.MISCELLANEOUS in visibleCategories
                }
            }
        )
    }

    override fun onDestroyView() {
        _categoriesFilterBinding = null

        super.onDestroyView()
    }
}
