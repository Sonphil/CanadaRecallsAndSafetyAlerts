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
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.ext.applyAppTheme
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallAdapter
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import dagger.android.support.DaggerFragment
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recent.*
import kotlinx.android.synthetic.main.include_categories_filter.*
import kotlinx.android.synthetic.main.include_categories_filter.view.*
import kotlinx.android.synthetic.main.include_empty_view_recent_recalls.*
import javax.inject.Inject

class RecentFragment : DaggerFragment() {

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
        if (requireActivity().card_view_categories_filter == null) {
            inflater.inflate(R.layout.include_categories_filter, requireActivity().root, true)
        }

        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_recent_recalls.setupRecyclerView()

        subscribeUI()

        swipe_refresh_layout_recent_recalls.setupSwipeRefreshLayout()

        setupFilter()

        btn_retry_recent_recalls.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun setupFilter() {
        val btnFilterRecalls = requireActivity().btn_filter_recalls

        btnFilterRecalls.setOnClickListener { btn ->
            btnFilterRecalls.isExpanded = true
        }

        with(requireActivity().card_view_categories_filter) {
            btn_categories_filter_done.setOnClickListener {
                btnFilterRecalls.isExpanded = false
            }
            val filterChipListener =
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
                }
            chip_group_category_filter.forEach { chip ->
                (chip as Chip).setOnCheckedChangeListener(filterChipListener)
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
        swipe_refresh_layout_recent_recalls.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun subscribeUI() {
        (requireActivity() as MainActivity).selectedDestinationId.observe(
            viewLifecycleOwner,
            Observer { destinationId ->
                if (destinationId == R.id.fragment_recent) {
                    rv_recent_recalls.smoothScrollToPosition(0)
                }
            })

        viewModel.recentRecalls.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            swipe_refresh_layout_recent_recalls.isRefreshing = loading
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
            rv_recent_recalls.isVisible = !emptyViewVisible
            empty_view_recent_recalls.isVisible = emptyViewVisible
        })

        viewModel.emptyViewIconResId.observe(viewLifecycleOwner, Observer { iconId ->
            iv_empty_recent_recalls.setImageResource(iconId)
        })

        viewModel.emptyViewTitleResId.observe(viewLifecycleOwner, Observer { titleId ->
            tv_title_empty_recent_recalls.setText(titleId)
        })

        viewModel.emptyViewRetryButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            btn_retry_recent_recalls.isVisible = visible
        })

        viewModel.categoryFilters.observe(viewLifecycleOwner, Observer { visibleCategories ->
            requireActivity().card_view_categories_filter?.let { filterCardView ->
                filterCardView.chip_category_filter_food.isChecked =
                    Category.FOOD in visibleCategories
                filterCardView.chip_category_filter_vehicle.isChecked =
                    Category.VEHICLE in visibleCategories
                filterCardView.chip_category_filter_health_product.isChecked =
                    Category.HEALTH_PRODUCT in visibleCategories
                filterCardView.chip_category_filter_consumer_product.isChecked =
                    Category.CONSUMER_PRODUCT in visibleCategories
            }
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            val action = NavGraphMainDirections.actionToActivityRecallDetails(it.recall)

            findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        with(requireActivity().root) {
            removeView(requireActivity().card_view_categories_filter)
            removeView(requireActivity().btn_filter_recalls)
        }

        super.onDestroyView()
    }
}
