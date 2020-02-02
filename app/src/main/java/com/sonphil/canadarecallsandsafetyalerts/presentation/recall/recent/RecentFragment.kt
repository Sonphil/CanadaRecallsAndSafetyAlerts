package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recent.*
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
    private val filterButton: ExtendedFloatingActionButton by lazy {
        requireActivity().btn_filter_recalls
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_recent_recalls.setupRecyclerView()

        subscribeUI()

        swipe_refresh_layout_recent_recalls.setupSwipeRefreshLayout()

        setupFilterButton()

        btn_retry_recent_recalls.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun setupFilterButton() {
        filterButton.show()
        filterButton.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun RecyclerView.setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        setLayoutManager(layoutManager)

        val divider = DividerItemDecoration(requireContext(), layoutManager.orientation)
        addItemDecoration(divider)

        adapter = this@RecentFragment.adapter

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    filterButton.shrink()
                }

                val firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()

                // Extend the button when the user reaches the top of the RecyclerView
                if (firstVisibleItem == 0) {
                    filterButton.extend()
                }
            }
        })
    }

    private fun SwipeRefreshLayout.setupSwipeRefreshLayout() {
        setColorSchemeResources(R.color.colorPrimary)
        swipe_refresh_layout_recent_recalls.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun subscribeUI() {
        viewModel.recentRecalls.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (!loading) {
                swipe_refresh_layout_recent_recalls.isRefreshing = loading
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            // TODO: Display errors in a nicer way
            error?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        })

        viewModel.emptyViewVisible.observe(viewLifecycleOwner, Observer { emptyViewVisible ->
            rv_recent_recalls.isVisible = !emptyViewVisible
            filterButton.isVisible = !emptyViewVisible
            empty_view_recent_recalls.isVisible = emptyViewVisible
        })
    }

    override fun onDestroyView() {
        filterButton.isVisible = false

        super.onDestroyView()
    }
}
