package com.sonphil.canadarecallsandsafetyalerts.presentation.more

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemMoreBinding

/**
 * Created by Sonphil on 08-02-20.
 */

class MoreAdapter(private val items: List<MoreItem>) :
    RecyclerView.Adapter<MoreAdapter.MoreItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoreBinding.inflate(
            inflater,
            parent,
            false
        )

        return MoreItemViewHolder(binding).apply {
            itemView.setOnClickListener {
                items[adapterPosition].moreItemClickHandler.invoke(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemId(position: Int): Long = items[position].icon.toLong()

    override fun onBindViewHolder(holder: MoreItemViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {
            ivItemMoreIcon.setImageResource(item.icon)
            tvItemMoreLabel.setText(item.label)
            tvItemMoreDescription.setText(item.description)
        }
    }

    inner class MoreItemViewHolder(val binding: ItemMoreBinding) :
        RecyclerView.ViewHolder(binding.root)
}