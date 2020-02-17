package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.NavGraphMainDirections
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallAdapter
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import dagger.android.support.DaggerFragment
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_my_recalls.*
import kotlinx.android.synthetic.main.include_empty_view_my_recalls.*
import javax.inject.Inject

class MyRecallsFragment : DaggerFragment() {

    private val viewModel: MyRecallsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MyRecallsViewModel::class.java)
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
        itemAnimator = SlideInLeftAnimator()
        adapter = this@MyRecallsFragment.adapter
    }

    private fun subscribeUI() {
        (requireActivity() as MainActivity).selectedDestinationId.observe(viewLifecycleOwner, Observer { destinationId ->
            if (destinationId == R.id.fragment_my_recalls) {
                rv_bookmarked_recalls.smoothScrollToPosition(0)
            }
        })

        viewModel.bookmarkedRecalls.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.emptyViewVisible.observe(viewLifecycleOwner, Observer { visible ->
            rv_bookmarked_recalls.isVisible = !visible
            empty_view_bookmarked_recalls.isVisible = visible
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            val action = NavGraphMainDirections.actionToActivityRecallDetails(it.recall)

            findNavController().navigate(action)
        })
    }
}
