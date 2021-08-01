package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.NavGraphMainDirections
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import javax.inject.Inject

/**
 * Created by Sonphil on 27-04-20.
 */

abstract class BaseRecallsFragment : Fragment() {
    @Inject
    lateinit var dateUtils: DateUtils

    protected abstract val currentDestinationId: Int
    protected val adapter: RecallAdapter
        get() = getRecyclerView().adapter as RecallAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecallAdapter(
            dateUtils = dateUtils,
            onItemClicked = this::onItemClicked,
            onBookmarkClicked = this::onBookmarkClicked
        )
        adapter.setupRecyclerView(requireContext(), getRecyclerView())

        subscribeUI()
    }

    protected abstract fun onOtherDestinationSelected()

    protected abstract fun getRecyclerView(): RecyclerView

    protected abstract fun getNavigateToDetailsEventLiveData(): LiveData<Event<RecallAndBookmarkAndReadStatus>>

    protected abstract fun onItemClicked(item: RecallAndBookmarkAndReadStatus)

    protected abstract fun onBookmarkClicked(recall: Recall, isCurrentlyBookmarked: Boolean)

    private fun subscribeUI() {
        (requireActivity() as MainActivity).selectedTopLevelDestinationId.observe(
            viewLifecycleOwner,
            { destinationId ->
                if (destinationId == currentDestinationId) {
                    getRecyclerView().smoothScrollToPosition(0)
                } else {
                    onOtherDestinationSelected()
                }
            }
        )

        getNavigateToDetailsEventLiveData().observe(
            viewLifecycleOwner,
            EventObserver {
                navigateToDetails(it)
            }
        )
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

    private fun getItemViewForRecall(
        recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus
    ): View? {
        val position = adapter.currentList.indexOfFirst {
            it == recallAndBookmarkAndReadStatus
        }

        return getRecyclerView().findViewHolderForAdapterPosition(position)?.itemView
    }
}
