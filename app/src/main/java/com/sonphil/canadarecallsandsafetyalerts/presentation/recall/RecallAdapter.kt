package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.data.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemRecallBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.formatUTC
import com.sonphil.canadarecallsandsafetyalerts.utils.DateUtils
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import java.text.DateFormat

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallAdapter(
    private val viewModel: BaseRecallViewModel,
    dateUtils: DateUtils
) : ListAdapter<RecallAndBookmarkAndReadStatus, RecallAdapter.RecallViewHolder>(
    DiffCallback()
) {
    private val dateFormat = dateUtils.getDateFormat(DateFormat.MEDIUM)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecallViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecallBinding.inflate(inflater, parent, false)

        val holder = RecallViewHolder(binding)

        holder.itemView.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            viewModel.clickRecall(item)
        }

        binding.btnRecallBookmark.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            val isCurrentlyBookmarked = item.bookmark != null

            holder.bindBookmark(!isCurrentlyBookmarked)
            viewModel.updateBookmark(item.recall, !isCurrentlyBookmarked)
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecallViewHolder, position: Int) {
        val item = getItem(position)
        val recall = item.recall
        val bookmark = item.bookmark

        with(holder) {
            bindCategory(recall.category)
            bindTitle(recall, item.readStatus)
            bindBookmark(bookmark != null)
            bindDate(recall.datePublished)
        }
    }

    private fun RecallViewHolder.bindCategory(category: Category) {
        val res = CategoryResources(category)

        binding.ivRecallCategoryIcon.setImageResource(res.iconId)
        binding.tvCategory.setText(res.labelId)
    }

    private fun RecallViewHolder.bindTitle(recall: Recall, readStatus: ReadStatus?) {
        binding.tvRecallTitle.text = recall.title
        val read = readStatus != null
        val typeface = if (read) Typeface.NORMAL else Typeface.BOLD
        binding.tvRecallTitle.setTypeface(null, typeface)
    }

    private fun RecallViewHolder.bindBookmark(bookmarked: Boolean) {
        val bookmarkDrawableRes = if (bookmarked) {
            R.drawable.ic_bookmark_red_24dp
        } else {
            R.drawable.ic_bookmark_border_control_normal_24dp
        }

        binding.btnRecallBookmark.setImageResource(bookmarkDrawableRes)
    }

    private fun RecallViewHolder.bindDate(date: Long?) {
        if (date == null) {
            binding.tvRecallDate.isVisible = false
        } else {
            binding.tvRecallDate.text = dateFormat.formatUTC(date)
            binding.tvRecallDate.isVisible = true
        }
    }

    fun setupRecyclerView(context: Context, recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.itemAnimator = SlideInLeftAnimator()

        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
            }
        })

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

    inner class RecallViewHolder(val binding: ItemRecallBinding) :
        RecyclerView.ViewHolder(binding.root)
}
