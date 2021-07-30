package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import javax.inject.Inject

/**
 * Created by Sonphil on 29-07-21.
 */

class DebouncingRecallItemClickHandler @Inject constructor() : RecallItemClickHandler {
    private val _navigateToDetails = MutableLiveData<Event<RecallAndBookmarkAndReadStatus>>()
    override val navigateToDetails: LiveData<Event<RecallAndBookmarkAndReadStatus>>
        get() = _navigateToDetails

    private var lastClickTime = 0L

    override fun onRecallClicked(recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus) {
        if (navigateToDetails.value?.peekContent() == recallAndBookmarkAndReadStatus) {
            if (SystemClock.elapsedRealtime() - lastClickTime < DOUBLE_CLICK_MIN_DELAY_IN_MS) {
                return
            }
        }
        lastClickTime = SystemClock.elapsedRealtime()

        _navigateToDetails.value = Event(recallAndBookmarkAndReadStatus)
    }

    companion object {
        private const val DOUBLE_CLICK_MIN_DELAY_IN_MS = 1000
    }
}
