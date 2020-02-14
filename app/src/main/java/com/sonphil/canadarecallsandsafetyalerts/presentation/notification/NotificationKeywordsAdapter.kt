package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_notification_keyword.*
import kotlinx.android.synthetic.main.item_notification_keyword.view.*

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
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_notification_keyword,
            parent,
            false
        )
        val holder = NotificationKeywordViewHolder(view)

        holder.itemView.btn_delete_notification_keyword.setOnClickListener {
            viewModel.deleteKeywordAtPosition(holder.adapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: NotificationKeywordViewHolder, position: Int) {
        val keyword = getItem(position)

        holder.tv_notification_keyword.text = keyword
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class NotificationKeywordViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer
}