package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Sonphil on 01-02-20.
 */

abstract class RecallBaseViewModel constructor(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    private val _navigateToDetails = MutableLiveData<Event<RecallAndBookmarkAndReadStatus>>()
    val navigateToDetails = _navigateToDetails

    fun updateBookmark(recall: Recall, bookmarked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkRepository.updateBookmark(recall, bookmarked)
        }
    }

    fun clickRecall(recallAndBookmarkAndReadStatus: RecallAndBookmarkAndReadStatus) {
        _navigateToDetails.value = Event(recallAndBookmarkAndReadStatus)
    }
}