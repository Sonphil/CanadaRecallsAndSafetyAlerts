package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmark
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recall.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallAdapter(
    context: Context,
    private val viewModel: RecallBaseViewModel
) : ListAdapter<RecallAndBookmark, RecallAdapter.RecallViewHolder>(
    DiffCallback()
) {

    private val dateFormat = SimpleDateFormat.getDateInstance(
        DateFormat.MEDIUM,
        LocaleUtils.getCurrentLocale(context)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecallViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recall,
            parent,
            false
        )
        val holder = RecallViewHolder(view)

        holder.btn_recall_bookmark.setOnClickListener {
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
            when (recall.category) {
                Category.FOOD -> {
                    iv_recall_category_icon.setImageResource(R.drawable.ic_restaurant_black_24dp)
                    tv_category.setText(R.string.label_category_food)
                }
                Category.VEHICLE -> {
                    iv_recall_category_icon.setImageResource(R.drawable.ic_directions_car_black_24dp)
                    tv_category.setText(R.string.label_category_vehicle)
                }
                Category.HEALTH_PRODUCT -> {
                    iv_recall_category_icon.setImageResource(R.drawable.ic_healing_black_24dp)
                    tv_category.setText(R.string.label_category_health_product)
                }
                Category.CONSUMER_PRODUCT -> {
                    iv_recall_category_icon.setImageResource(R.drawable.ic_shopping_cart_black_24dp)
                    tv_category.setText(R.string.label_category_consumer_product)
                }
            }

            this.bindBookmark(bookmark != null)

            tv_recall_title.text = recall.title

            val datePublishedLong = recall.datePublished?.toLong()

            if (datePublishedLong == null) {
                tv_recall_date.isVisible = false
            } else {
                tv_recall_date.text = dateFormat.format(Date(datePublishedLong))
                tv_recall_date.isVisible = true
            }
        }
    }

    private fun RecallViewHolder.bindBookmark(bookmarked: Boolean) {
        val bookmarkDrawableRes = if (bookmarked) {
            R.drawable.ic_bookmark_red_24dp
        } else {
            R.drawable.ic_bookmark_border_black_24dp
        }

        btn_recall_bookmark.setImageResource(bookmarkDrawableRes)
    }

    private class DiffCallback : DiffUtil.ItemCallback<RecallAndBookmark>() {
        override fun areItemsTheSame(
            oldItem: RecallAndBookmark,
            newItem: RecallAndBookmark
        ): Boolean = oldItem.recall.id == newItem.recall.id

        override fun areContentsTheSame(
            oldItem: RecallAndBookmark,
            newItem: RecallAndBookmark
        ): Boolean = oldItem == newItem
    }

    inner class RecallViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer
}