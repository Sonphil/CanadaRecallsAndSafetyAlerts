package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemRecallDetailsSectionContentBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemRecallDetailsSectionHeaderBinding

/**
 * Created by Sonphil on 01-03-20.
 */

class RecallDetailsSectionAdapter :
    ListAdapter<RecallDetailsSectionItem, RecallDetailsSectionItemViewHolder<out RecallDetailsSectionItem>>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecallDetailsSectionItemViewHolder<out RecallDetailsSectionItem> {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == R.layout.item_recall_details_section_header) {
            val binding = ItemRecallDetailsSectionHeaderBinding.inflate(
                inflater,
                parent,
                false
            )

            RecallDetailsSectionItemViewHolder.SectionHeaderViewHolder(binding)
        } else {
            val binding = ItemRecallDetailsSectionContentBinding.inflate(
                inflater,
                parent,
                false
            )

            binding.tvRecallDetailsSectionContent.movementMethod = LinkMovementMethod.getInstance()

            RecallDetailsSectionItemViewHolder.SectionContentViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: RecallDetailsSectionItemViewHolder<out RecallDetailsSectionItem>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId

    private class DiffCallback : DiffUtil.ItemCallback<RecallDetailsSectionItem>() {
        override fun areItemsTheSame(
            oldItem: RecallDetailsSectionItem,
            newItem: RecallDetailsSectionItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: RecallDetailsSectionItem,
            newItem: RecallDetailsSectionItem
        ) = oldItem == newItem
    }
}
