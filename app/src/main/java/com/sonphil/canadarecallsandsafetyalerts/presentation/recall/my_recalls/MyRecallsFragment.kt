package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sonphil.canadarecallsandsafetyalerts.NavGraphMainDirections
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentMyRecallsBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallAdapter
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MyRecallsFragment : DaggerFragment() {
    private var binding: FragmentMyRecallsBinding by viewLifecycle()
    private lateinit var mainActivityBinding: ActivityMainBinding

    private val viewModel: MyRecallsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MyRecallsViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var dateUtils: DateUtils
    private val adapter by lazy { RecallAdapter(viewModel, dateUtils) }
    private val unbookmarkSnackbar by lazy {
        Snackbar.make(mainActivityBinding.root, R.string.message_unbookmark, Snackbar.LENGTH_LONG)
            .setAnchorView(mainActivityBinding.bottomNavigationView)
            .setAction(R.string.label_undo_unbookmark) {
                viewModel.undoLastUnbookmark()
            }
            .setActionTextColor(requireContext().getColor(R.color.colorPrimary))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyRecallsBinding.inflate(inflater, container, false)

        mainActivityBinding = (requireActivity() as MainActivity).binding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEmptyView()

        adapter.setupRecyclerView(requireContext(), binding.rvBookmarkedRecalls)

        subscribeUI()
    }

    private fun setupEmptyView() = with(mainActivityBinding.includeEmptyView) {
        ivEmpty.setImageResource(R.drawable.ic_bookmark_border_control_normal_24dp)
        tvTitleEmpty.setText(R.string.title_empty_bookmark)
        tvTextEmpty.setText(R.string.text_empty_bookmark)
        tvTextEmpty.isVisible = true
    }

    private fun subscribeUI() {
        (requireActivity() as MainActivity).selectedDestinationId.observe(
            viewLifecycleOwner,
            Observer { destinationId ->
                if (destinationId == R.id.fragment_my_recalls) {
                    binding.rvBookmarkedRecalls.smoothScrollToPosition(0)
                } else {
                    unbookmarkSnackbar.dismiss()
                }
            })

        viewModel.bookmarkedRecalls.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.emptyViewVisible.observe(viewLifecycleOwner, Observer { visible ->
            binding.rvBookmarkedRecalls.isVisible = !visible
            mainActivityBinding.includeEmptyView.emptyView.isVisible = visible
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            val action = NavGraphMainDirections.actionToActivityRecallDetails(it.recall)

            findNavController().navigate(action)
        })

        viewModel.showUndoUnbookmarkSnackbar.observe(viewLifecycleOwner, Observer { show ->
            if (show) {
                unbookmarkSnackbar.show()
            } else {
                unbookmarkSnackbar.dismiss()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
