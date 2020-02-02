package com.sonphil.canadarecallsandsafetyalerts.presentation.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_recent.*
import javax.inject.Inject

class RecentFragment : DaggerFragment() {

    private val viewModel: RecentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RecentViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter by lazy { RecentRecallAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_recent_recalls.setupRecyclerView()

        swipe_refresh_layout_recent_recalls.setupSwipeRefreshLayout()

        subscribeUI()
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
        viewModel.recentRecalls.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (!loading) {
                swipe_refresh_layout_recent_recalls.isRefreshing = loading
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT) }
        })
    }
}
