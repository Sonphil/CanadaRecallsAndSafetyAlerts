package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemNotificationKeywordBinding

/**
 * Created by Sonphil on 13-02-20.
 */

class NotificationKeywordsAdapter(
    private val viewModel: NotificationKeywordsViewModel
) : ListAdapter<String, NotificationKeywordsAdapter.NotificationKeywordViewHolder>(
    DiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationKeywordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationKeywordBinding.inflate(
            inflater,
            parent,
            false
        )
        val holder = NotificationKeywordViewHolder(binding)

        holder.binding.btnDeleteNotificationKeyword.setOnClickListener {
            viewModel.deleteKeywordAtPosition(holder.adapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: NotificationKeywordViewHolder, position: Int) {
        val keyword = getItem(position)

        holder.binding.tvNotificationKeyword.text = keyword
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class NotificationKeywordViewHolder(val binding: ItemNotificationKeywordBinding) :
        RecyclerView.ViewHolder(binding.root)
}