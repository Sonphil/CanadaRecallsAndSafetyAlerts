package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemRecallBinding
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.presentation.AppTheme
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent.RecallItem
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import java.text.DateFormat

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallAdapter(
    dateUtils: DateUtils,
    private val onItemClicked: (item: RecallAndBookmarkAndReadStatus) -> Unit,
    private val onBookmarkClicked: (recall: Recall, isCurrentlyBookmarked: Boolean) -> Unit
) : ListAdapter<RecallAndBookmarkAndReadStatus, RecallAdapter.RecallViewHolder>(
    DiffCallback()
) {
    private val dateFormat = dateUtils.getDateFormat(DateFormat.MEDIUM)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecallViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecallBinding.inflate(inflater, parent, false)
        val holder = RecallViewHolder(binding)

        binding.composeViewItemRecall.setContent {
            AppTheme {
                RecallItem(
                    holder.itemLiveData,
                    dateFormat,
                    onItemClicked = { item: RecallAndBookmarkAndReadStatus ->
                        onItemClicked(item)
                    },
                    onBookmarkClicked = { recall: Recall, isCurrentlyBookmarked: Boolean ->
                        onBookmarkClicked(recall, isCurrentlyBookmarked)
                    }
                )
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecallViewHolder, position: Int) {
        holder.itemLiveData.value = getItem(position)
    }

    fun setupRecyclerView(context: Context, recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.itemAnimator = SlideInLeftAnimator()

        registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if (positionStart == 0) {
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            recyclerView.smoothScrollToPosition(0)
                        }
                    }
                }
            }
        )

        recyclerView.adapter = this
    }

    private class DiffCallback : DiffUtil.ItemCallback<RecallAndBookmarkAndReadStatus>() {
        override fun areItemsTheSame(
            oldItem: RecallAndBookmarkAndReadStatus,
            newItem: RecallAndBookmarkAndReadStatus
        ): Boolean = oldItem.recall.id == newItem.recall.id

        override fun areContentsTheSame(
            oldItem: RecallAndBookmarkAndReadStatus,
            newItem: RecallAndBookmarkAndReadStatus
        ): Boolean = oldItem == newItem
    }

    inner class RecallViewHolder(
        val binding: ItemRecallBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val itemLiveData = MutableLiveData<RecallAndBookmarkAndReadStatus>()
    }
}
