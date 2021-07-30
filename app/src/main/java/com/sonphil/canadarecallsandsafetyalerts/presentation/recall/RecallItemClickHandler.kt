package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import androidx.lifecycle.LiveData
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.utils.Event

/**
 * Created by Sonphil on 29-07-21.
 */

interface RecallItemClickHandler {
    val navigateToDetails: LiveData<Event<RecallAndBookmarkAndReadStatus>>

    fun onRecallClicked(recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus)
}
