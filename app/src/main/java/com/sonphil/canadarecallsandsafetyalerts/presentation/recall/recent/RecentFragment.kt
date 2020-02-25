package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.sonphil.canadarecallsandsafetyalerts.NavGraphMainDirections
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentRecentBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.IncludeCategoriesFilterBinding
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.ext.applyAppTheme
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallAdapter
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import dagger.android.support.DaggerFragment
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import javax.inject.Inject

class RecentFragment : DaggerFragment() {
    private var binding: FragmentRecentBinding by viewLifecycle()
    private lateinit var mainActivityBinding: ActivityMainBinding
    private var _categoriesFilterBinding: IncludeCategoriesFilterBinding? = null
    private val categoriesFilterBinding get() = _categoriesFilterBinding!!
    private val viewModel: RecentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RecentViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var dateUtils: DateUtils
    private val adapter by lazy { RecallAdapter(viewModel, dateUtils) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentBinding.inflate(layoutInflater, container, false)

        mainActivityBinding = (requireActivity() as MainActivity).binding

        _categoriesFilterBinding = IncludeCategoriesFilterBinding.inflate(
            layoutInflater,
            mainActivityBinding.root
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecentRecalls.setupRecyclerView()

        subscribeUI()

        binding.swipeRefreshLayoutRecentRecalls.setupSwipeRefreshLayout()

        setupFilter()

        mainActivityBinding.includeEmptyView.btnRetry.setOnClickListener {
            viewModel.refresh()
        }
    }

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
                }
            }.let { listener ->
                chipGroupCategoryFilter.forEach { chip ->
                    (chip as Chip).setOnCheckedChangeListener(listener)
                }
            }
        }
    }

    private fun RecyclerView.setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        setLayoutManager(layoutManager)

        itemAnimator = SlideInLeftAnimator()

        adapter = this@RecentFragment.adapter
    }

    private fun SwipeRefreshLayout.setupSwipeRefreshLayout() {
        applyAppTheme(requireContext())
        binding.swipeRefreshLayoutRecentRecalls.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun subscribeUI() {
        (requireActivity() as MainActivity).selectedDestinationId.observe(
            viewLifecycleOwner,
            Observer { destinationId ->
                if (destinationId == R.id.fragment_recent) {
                    binding.rvRecentRecalls.smoothScrollToPosition(0)
                }
            })

        viewModel.recentRecalls.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            binding.swipeRefreshLayoutRecentRecalls.isRefreshing = loading
        })

        viewModel.genericError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toasty.error(requireContext(), error, Toast.LENGTH_SHORT, true).show()
            }
        })

        viewModel.networkError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                val icon = requireContext().getDrawable(R.drawable.ic_cloud_off_white_24dp)

                Toasty.normal(requireContext(), error, Toast.LENGTH_SHORT, icon).show()
            }
        })

        viewModel.emptyViewVisible.observe(viewLifecycleOwner, Observer { emptyViewVisible ->
            binding.rvRecentRecalls.isVisible = !emptyViewVisible
            mainActivityBinding.includeEmptyView.emptyView.isVisible = emptyViewVisible
        })

        viewModel.emptyViewIconResId.observe(viewLifecycleOwner, Observer { iconId ->
            mainActivityBinding.includeEmptyView.ivEmpty.setImageResource(iconId)
        })

        viewModel.emptyViewTitleResId.observe(viewLifecycleOwner, Observer { titleId ->
            mainActivityBinding.includeEmptyView.tvTitleEmpty.setText(titleId)
        })

        viewModel.emptyViewRetryButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            mainActivityBinding.includeEmptyView.btnRetry.isVisible = visible
        })

        viewModel.categoryFilters.observe(viewLifecycleOwner, Observer { visibleCategories ->
            with(categoriesFilterBinding) {
                chipCategoryFilterFood.isChecked =
                    Category.FOOD in visibleCategories
                chipCategoryFilterVehicle.isChecked =
                    Category.VEHICLE in visibleCategories
                chipCategoryFilterHealthProduct.isChecked =
                    Category.HEALTH_PRODUCT in visibleCategories
                chipCategoryFilterConsumerProduct.isChecked =
                    Category.CONSUMER_PRODUCT in visibleCategories
            }
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            val action = NavGraphMainDirections.actionToActivityRecallDetails(it.recall)

            findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        with(mainActivityBinding.root) {
            removeView(categoriesFilterBinding.cardViewCategoriesFilter)
            removeView(categoriesFilterBinding.btnFilterRecalls)
        }

        _categoriesFilterBinding = null

        super.onDestroyView()
    }
}
