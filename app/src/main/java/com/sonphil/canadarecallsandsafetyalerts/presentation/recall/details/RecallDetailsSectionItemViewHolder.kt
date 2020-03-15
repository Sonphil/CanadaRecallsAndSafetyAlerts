package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.os.Build
import android.text.Html
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemRecallDetailsSectionContentBinding
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemRecallDetailsSectionHeaderBinding

/**
 * Created by Sonphil on 01-03-20.
 */

sealed class RecallDetailsSectionItemViewHolder<T : RecallDetailsSectionItem>(open val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: RecallDetailsSectionItem)

    class SectionHeaderViewHolder(override val binding: ItemRecallDetailsSectionHeaderBinding) :
        RecallDetailsSectionItemViewHolder<RecallDetailsSectionItem.RecallDetailsSectionHeaderItem>(
            binding
        ) {

        override fun bind(item: RecallDetailsSectionItem) {
            if (item is RecallDetailsSectionItem.RecallDetailsSectionHeaderItem) {
                binding.tvRecallDetailsSectionHeaderTitle.text = item.title
            }
        }
    }

    class SectionContentViewHolder(override val binding: ItemRecallDetailsSectionContentBinding) :
        RecallDetailsSectionItemViewHolder<RecallDetailsSectionItem.RecallDetailsSectionContentItem>(
            binding
        ) {
        override fun bind(item: RecallDetailsSectionItem) {
            if (item is RecallDetailsSectionItem.RecallDetailsSectionContentItem) {
                // TODO: Display HTML content in a nicer way
                binding.tvRecallDetailsSectionContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val flags = Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV or
                            Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST or
                            Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM

                    Html.fromHtml(item.htmlContent, flags)
                } else {
                    Html.fromHtml(item.htmlContent)
                }
            }
        }
    }
}