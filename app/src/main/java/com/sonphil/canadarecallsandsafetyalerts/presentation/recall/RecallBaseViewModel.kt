package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Sonphil on 01-02-20.
 */

abstract class RecallBaseViewModel constructor(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    fun updateBookmark(recall: Recall, bookmarked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkRepository.updateBookmark(recall, bookmarked)
        }
    }
}