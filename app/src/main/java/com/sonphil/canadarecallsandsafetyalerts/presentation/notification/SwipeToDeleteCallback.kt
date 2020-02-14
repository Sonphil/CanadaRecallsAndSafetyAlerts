package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Sonphil on 13-02-20.
 */

class SwipeToDeleteCallback(
    private val viewModel: NotificationKeywordsViewModel
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewModel.deleteKeywordAtPosition(viewHolder.adapterPosition)
    }
}