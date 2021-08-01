package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentNotificationKeywordsBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.IncludeFabAddNotificationKeywordBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

@AndroidEntryPoint
class NotificationKeywordsFragment : Fragment() {
    private var binding: FragmentNotificationKeywordsBinding by viewLifecycle()
    private var _addBtnBinding: IncludeFabAddNotificationKeywordBinding? = null
    private val addBtnBinding get() = _addBtnBinding!!
    private lateinit var mainActivityBinding: ActivityMainBinding
    private val viewModel: NotificationKeywordsViewModel by viewModels()
    private val adapter by lazy { NotificationKeywordsAdapter(viewModel) }
    private var keywordDeletedSnackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationKeywordsBinding.inflate(inflater, container, false)

        mainActivityBinding = (requireActivity() as MainActivity).binding

        _addBtnBinding = IncludeFabAddNotificationKeywordBinding.inflate(
            inflater,
            mainActivityBinding.root
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEmptyView()

        addBtnBinding.btnAddNotificationKeyword.setOnClickListener {
            AddNotificationKeywordDialogFragment().show(
                childFragmentManager,
                AddNotificationKeywordDialogFragment.TAG
            )
        }

        binding.rvNotificationKeywords.setupRecyclerView()

        subscribeUI()

        setupOnDestinationChangedListener()
    }

    private fun setupEmptyView() {
        with(mainActivityBinding.includeEmptyView) {
            ivEmpty.setImageResource(R.drawable.ic_notifications_off_control_normal_24dp)
            tvTitleEmpty.setText(R.string.title_empty_notification_keyword)
            tvTextEmpty.setText(R.string.text_empty_notification_keyword)
            tvTextEmpty.isVisible = true
        }
    }

    private fun RecyclerView.setupRecyclerView() {
        val swipeToDeleteCallback = SwipeToDeleteCallback(viewModel)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(this)

        itemAnimator = SlideInLeftAnimator()

        adapter = this@NotificationKeywordsFragment.adapter
    }

    private fun subscribeUI() {
        viewModel.keywords.observe(
            viewLifecycleOwner,
            Observer { keywords ->
                adapter.submitList(keywords)
            }
        )

        viewModel.showKeywordDeletedSnackBar.observe(
            viewLifecycleOwner,
            EventObserver { show ->
                if (show) {
                    if (keywordDeletedSnackBar == null) {
                        keywordDeletedSnackBar = Snackbar.make(
                            binding.layoutNotificationKeywords,
                            R.string.message_notification_keyword_deleted,
                            Snackbar.LENGTH_LONG
                        )
                    }

                    keywordDeletedSnackBar?.show()
                }
            }
        )

        viewModel.showEmptyView.observe(
            viewLifecycleOwner,
            Observer { show ->
                binding.rvNotificationKeywords.isVisible = !show
                mainActivityBinding.includeEmptyView.emptyView.isVisible = show
            }
        )
    }

    private fun setupOnDestinationChangedListener() {
        val navController = findNavController()

        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    if (destination.id != R.id.fragment_notification_keywords) {
                        navController.removeOnDestinationChangedListener(this)

                        keywordDeletedSnackBar?.dismiss()

                        mainActivityBinding.root.removeView(addBtnBinding.btnAddNotificationKeyword)

                        _addBtnBinding = null
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        keywordDeletedSnackBar = null

        super.onDestroyView()
    }
}
