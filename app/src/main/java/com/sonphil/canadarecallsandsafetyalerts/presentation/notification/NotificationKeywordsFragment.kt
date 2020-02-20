package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.utils.EventObserver
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_notification_keywords.*
import kotlinx.android.synthetic.main.include_empty_view.*
import javax.inject.Inject

class NotificationKeywordsFragment : DaggerFragment() {

    private val viewModel: NotificationKeywordsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NotificationKeywordsViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter by lazy { NotificationKeywordsAdapter(viewModel) }
    private val keywordDeletedSnackBar by lazy {
        Snackbar.make(
            layout_notification_keywords,
            R.string.message_notification_keyword_deleted,
            Snackbar.LENGTH_LONG
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notification_keywords, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEmptyView()

        rv_notification_keywords.setupRecyclerView()

        subscribeUI()
    }

    private fun setupEmptyView() {
        with(requireActivity()) {
            iv_empty.setImageResource(R.drawable.ic_notifications_control_normal_24dp)
            tv_title_empty.setText(R.string.title_empty_notification_keyword)
            tv_text_empty.setText(R.string.text_empty_notification_keyword)
            tv_text_empty.isVisible = true
        }
    }

    private fun RecyclerView.setupRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )
        addItemDecoration(dividerItemDecoration)

        val swipeToDeleteCallback = SwipeToDeleteCallback(viewModel)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(this)

        adapter = this@NotificationKeywordsFragment.adapter
    }

    private fun subscribeUI() {
        viewModel.keywords.observe(viewLifecycleOwner, Observer { keywords ->
            adapter.submitList(keywords)
        })

        viewModel.showKeywordDeletedSnackBar.observe(viewLifecycleOwner, EventObserver { show ->
            if (show) {
                keywordDeletedSnackBar.show()
            }
        })

        viewModel.showEmptyView.observe(viewLifecycleOwner, Observer { show ->
            rv_notification_keywords.isVisible = !show
            requireActivity().empty_view.isVisible = show
        })
    }
}
