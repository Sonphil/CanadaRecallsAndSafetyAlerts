package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentMyRecallsBinding
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.BaseRecallsFragment
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecallsFragment : BaseRecallsFragment() {

    private var binding: FragmentMyRecallsBinding by viewLifecycle()
    private lateinit var mainActivityBinding: ActivityMainBinding

    override val currentDestinationId = R.id.fragment_my_recalls
    val viewModel: MyRecallsViewModel by viewModels()

    private var unbookmarkSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRecallsBinding.inflate(inflater, container, false)

        mainActivityBinding = (requireActivity() as MainActivity).binding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEmptyView()

        subscribeUI()
    }

    override fun onOtherDestinationSelected() {
        unbookmarkSnackbar?.dismiss()
    }

    override fun getRecyclerView(): RecyclerView = binding.rvBookmarkedRecalls

    override fun getNavigateToDetailsEventLiveData(): LiveData<Event<RecallAndBookmarkAndReadStatus>> {
        return viewModel.navigateToDetails
    }

    override fun onItemClicked(item: RecallAndBookmarkAndReadStatus) {
        viewModel.onRecallClicked(item)
    }

    override fun onBookmarkClicked(recall: Recall, isCurrentlyBookmarked: Boolean) {
        viewModel.onBookmarkClicked(recall, isCurrentlyBookmarked)
    }

    private fun getEmptyView() = mainActivityBinding.includeEmptyView.emptyView

    private fun setupEmptyView() = with(mainActivityBinding.includeEmptyView) {
        ivEmpty.setImageResource(R.drawable.ic_bookmark_border_control_normal_24dp)
        tvTitleEmpty.setText(R.string.title_empty_bookmark)
        tvTextEmpty.setText(R.string.text_empty_bookmark)
        tvTextEmpty.isVisible = true
    }

    private fun subscribeUI() {
        viewModel.emptyViewVisible.observe(
            viewLifecycleOwner,
            { visible ->
                getRecyclerView().isVisible = !visible
                getEmptyView().isVisible = visible
            }
        )

        viewModel.bookmarkedRecalls.observe(
            viewLifecycleOwner,
            {
                adapter.submitList(it)
            }
        )

        viewModel.showUndoUnbookmarkSnackbar.observe(
            viewLifecycleOwner,
            { show ->
                if (show) {
                    if (unbookmarkSnackbar == null) {
                        unbookmarkSnackbar = Snackbar.make(mainActivityBinding.root, R.string.message_unbookmark, Snackbar.LENGTH_LONG)
                            .setAnchorView(mainActivityBinding.bottomNavigationView)
                            .setAction(R.string.label_undo_unbookmark) {
                                viewModel.undoLastUnbookmark()
                            }
                            .setActionTextColor(requireContext().getColor(R.color.colorPrimary))
                    }

                    unbookmarkSnackbar?.show()
                } else {
                    unbookmarkSnackbar?.dismiss()
                }
            }
        )
    }

    override fun onDestroyView() {
        unbookmarkSnackbar = null

        super.onDestroyView()
    }
}
