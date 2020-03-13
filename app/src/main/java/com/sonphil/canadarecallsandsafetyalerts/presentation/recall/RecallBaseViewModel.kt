package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Sonphil on 01-02-20.
 */

abstract class RecallBaseViewModel constructor(
    private val updateBookmarkUseCase: UpdateBookmarkUseCase
) : ViewModel() {
    private val _navigateToDetails = MutableLiveData<Event<RecallAndBookmarkAndReadStatus>>()
    val navigateToDetails = _navigateToDetails

    @CallSuper
    open fun updateBookmark(recall: Recall, bookmarked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateBookmarkUseCase(recall, bookmarked)
        }
    }

    fun clickRecall(recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus) {
        _navigateToDetails.value = Event(recallAndBookmarkAndReadStatus)
    }
}