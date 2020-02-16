package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 11-02-20.
 */

class RecallDetailsViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val recallRepository: RecallRepository,
    private val recall: Recall
) : ViewModel() {

    init {
        viewModelScope.launch {
            recallRepository.markRecallAsRead(recall)
        }
    }

    private val bookmark = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        val source = bookmarkRepository
            .getBookmark(recall)
            .asLiveData()

        emitSource(source)
    }
    val bookmarked: LiveData<Boolean> = bookmark.map { it != null }
    val bookmarkDate: LiveData<Long?> = bookmark.map { bookmark -> bookmark?.date }

    fun clickBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarked = bookmark.value != null

            bookmarkRepository.updateBookmark(recall, !bookmarked)
        }
    }
}