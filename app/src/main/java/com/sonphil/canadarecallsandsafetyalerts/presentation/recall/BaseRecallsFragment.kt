package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.NavGraphMainDirections
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Created by Sonphil on 27-04-20.
 */

abstract class BaseRecallsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var dateUtils: DateUtils

    protected abstract val currentDestinationId: Int
    protected abstract val viewModel: BaseRecallViewModel
    protected val adapter by lazy { RecallAdapter(viewModel, dateUtils) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setupRecyclerView(requireContext(), getRecyclerView())

        subscribeUI()
    }

    protected abstract fun onOtherDestinationSelected()

    protected abstract fun getRecyclerView(): RecyclerView

    protected abstract fun getEmptyView(): View

    private fun getItemViewForRecall(
        recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus
    ): View? {
        val position = adapter.currentList.indexOfFirst {
            it == recallAndBookmarkAndReadStatus
        }

        return getRecyclerView().findViewHolderForAdapterPosition(position)?.itemView
    }

    private fun subscribeUI() {
        (requireActivity() as MainActivity).selectedTopLevelDestinationId.observe(
            viewLifecycleOwner,
            Observer { destinationId ->
                if (destinationId == currentDestinationId) {
                    getRecyclerView().smoothScrollToPosition(0)
                } else {
                    onOtherDestinationSelected()
                }
            })

        viewModel.emptyViewVisible.observe(viewLifecycleOwner, Observer { visible ->
            getRecyclerView().isVisible = !visible
            getEmptyView().isVisible = visible
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            navigateToDetails(it)
        })
    }

    private fun navigateToDetails(recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus) {
        val action = NavGraphMainDirections.actionToActivityRecallDetails(recallAndBookmarkAndReadStatus.recall)
        val transitionName = getString(R.string.name_recall_details_shared_element_transition)
        val itemView = getItemViewForRecall(recallAndBookmarkAndReadStatus)

        if (itemView == null) {
            findNavController().navigate(action)
        } else {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                itemView,
                transitionName
            )
            val extras = ActivityNavigatorExtras(options)

            findNavController().navigate(action, extras)
        }
    }
}