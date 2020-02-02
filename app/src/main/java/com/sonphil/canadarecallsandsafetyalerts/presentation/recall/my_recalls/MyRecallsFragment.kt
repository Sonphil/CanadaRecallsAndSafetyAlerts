package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_my_recalls.*
import javax.inject.Inject

class MyRecallsFragment : DaggerFragment() {

    private val viewModel: MyRecallsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MyRecallsViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter by lazy {
        RecallAdapter(
            requireContext(),
            viewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_recalls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_bookmarked_recalls.setupRecyclerView()

        subscribeUI()
    }

    private fun RecyclerView.setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        setLayoutManager(layoutManager)

        val divider = DividerItemDecoration(requireContext(), layoutManager.orientation)
        addItemDecoration(divider)

        adapter = this@MyRecallsFragment.adapter
    }

    private fun subscribeUI() {
        viewModel.bookmarkedRecalls.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}
