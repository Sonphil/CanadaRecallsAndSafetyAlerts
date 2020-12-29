package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import android.os.SystemClock
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Sonphil on 01-02-20.
 */

abstract class BaseRecallViewModel constructor(
    private val updateBookmarkUseCase: UpdateBookmarkUseCase
) : ViewModel() {
    abstract val emptyViewVisible: LiveData<Boolean>

    private val _navigateToDetails = MutableLiveData<Event<RecallAndBookmarkAndReadStatus>>()
    val navigateToDetails = _navigateToDetails

    private var lastClickTime = 0L

    @CallSuper
    open fun updateBookmark(recall: Recall, bookmarked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateBookmarkUseCase(recall, bookmarked)
        }
    }

    fun clickRecall(recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus) {
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
