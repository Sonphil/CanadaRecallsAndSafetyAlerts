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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallAdapter
import dagger.android.support.DaggerFragment
import es.dmoral.toasty.Toasty
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
    private val adapter by lazy {
        RecallAdapter(
            requireContext(),
            viewModel
        )
    }
    private val filterButton: FloatingActionButton by lazy {
        requireActivity().btn_filter_recalls
    }
    private val filterCardView: CircularRevealCardView by lazy {
        requireActivity().card_view_categories_filter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.include_categories_filter, requireActivity().root, true)

        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_recent_recalls.setupRecyclerView()

        requireActivity()
            .iv_section_icon
            .setImageResource(R.drawable.ic_access_time_white_24dp)

        subscribeUI()

        swipe_refresh_layout_recent_recalls.setupSwipeRefreshLayout()

        setupFilter()

        btn_retry_recent_recalls.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun setupFilter() {
        filterButton.setOnClickListener {
            filterButton.isExpanded = true
        }
        with(filterCardView) {
            btn_categories_filter_done.setOnClickListener {
                filterButton.isExpanded = false
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

        val divider = DividerItemDecoration(requireContext(), layoutManager.orientation)
        addItemDecoration(divider)

        adapter = this@RecentFragment.adapter
    }

    private fun SwipeRefreshLayout.setupSwipeRefreshLayout() {
        setColorSchemeResources(R.color.colorPrimary)
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
            if (!loading) {
                swipe_refresh_layout_recent_recalls.isRefreshing = loading
            }
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

        viewModel.emptyProgressBarVisible.observe(
            viewLifecycleOwner,
            Observer { emptyProgressBarVisible ->
                progress_bar_empty_recent_recalls.isVisible = emptyProgressBarVisible
            }
        )

        viewModel.categoryFilters.observe(viewLifecycleOwner, Observer { visibleCategories ->
            with(filterCardView) {
                chip_category_filter_food.isChecked = Category.FOOD in visibleCategories
                chip_category_filter_vehicle.isChecked = Category.VEHICLE in visibleCategories
                chip_category_filter_health_product.isChecked =
                    Category.HEALTH_PRODUCT in visibleCategories
                chip_category_filter_consumer_product.isChecked =
                    Category.CONSUMER_PRODUCT in visibleCategories
            }
        })
    }

    override fun onDestroyView() {
        requireActivity().root.removeView(filterCardView)
        requireActivity().root.removeView(filterButton)

        super.onDestroyView()
    }
}
