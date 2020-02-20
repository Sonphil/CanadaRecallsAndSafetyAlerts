package com.sonphil.canadarecallsandsafetyalerts.presentation.more

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_more.*

/**
 * Created by Sonphil on 08-02-20.
 */

class MoreAdapter(private val items: List<MoreItem>) :
    RecyclerView.Adapter<MoreAdapter.MoreItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_more, parent, false)

        return MoreItemViewHolder(itemView).apply {
            itemView.setOnClickListener {
                items[adapterPosition].moreItemClickHandler.invoke(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemId(position: Int): Long = items[position].icon.toLong()

    override fun onBindViewHolder(holder: MoreItemViewHolder, position: Int) {
        val item = items[position]

        holder.iv_item_more_icon.setImageResource(item.icon)
        holder.tv_item_more_label.setText(item.label)
        holder.tv_item_more_description.setText(item.description)
    }

    inner class MoreItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer
}