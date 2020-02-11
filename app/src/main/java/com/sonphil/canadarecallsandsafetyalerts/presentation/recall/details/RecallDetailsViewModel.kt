package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Sonphil on 11-02-20.
 */

class RecallDetailsViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val recallRepository: RecallRepository
) : ViewModel() {
    private val recall = MutableLiveData<Recall>()

    private val bookmark = recall.switchMap { recall ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val source = bookmarkRepository
                .getBookmark(recall)
                .asLiveData()

            emitSource(source)
        }
    }
    val bookmarked: LiveData<Boolean> = bookmark.map { it != null }
    val bookmarkDate: LiveData<Date?> = bookmark.map { bookmark ->
        if (bookmark == null) {
            null
        } else {
            Date(bookmark.date)
        }
    }

    fun setRecall(recall: Recall) {
        viewModelScope.launch(Dispatchers.IO) {
            this@RecallDetailsViewModel.recall.postValue(recall)

            recallRepository.markRecallAsRead(recall)
        }
    }

    fun clickBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            val recall = recall.value
            val bookmarked = bookmark.value != null

            if (recall != null) {
                bookmarkRepository.updateBookmark(recall, !bookmarked)
            }
        }
    }
}