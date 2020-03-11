package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallBaseViewModel
import com.sonphil.canadarecallsandsafetyalerts.data.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class MyRecallsViewModel @Inject constructor(
    private val recallRepository: RecallRepository,
    private val bookmarkRepository: BookmarkRepository
) : RecallBaseViewModel(bookmarkRepository) {
    private val bookmarkedRecallsWithLoadState: LiveData<StateData<List<RecallAndBookmarkAndReadStatus>>> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val source = recallRepository
                .getBookmarkedRecalls()
                .asLiveData()

            emitSource(source)
        }

    val bookmarkedRecalls = bookmarkedRecallsWithLoadState.map { stateData ->
        stateData.data
    }

    val emptyViewVisible = bookmarkedRecalls.map { list ->
        list.orEmpty().isEmpty()
    }

    private val lastBookmarkRemoved = MutableLiveData<Bookmark?>()
    private val _showUndoUnbookmarkSnackbar = MutableLiveData<Boolean>()
    val showUndoUnbookmarkSnackbar = _showUndoUnbookmarkSnackbar

    override fun updateBookmark(recall: Recall, bookmarked: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            if (!bookmarked) {
                val bookmark = bookmarkedRecalls
                    .value
                    ?.find { it.recall == recall }
                    ?.bookmark

                lastBookmarkRemoved.postValue(bookmark)
                _showUndoUnbookmarkSnackbar.postValue(true)
            }

            super.updateBookmark(recall, bookmarked)
        }
    }

    fun undoLastUnbookmark() {
        _showUndoUnbookmarkSnackbar.value = false

        viewModelScope.launch(Dispatchers.IO) {
            lastBookmarkRemoved.value?.let {
                bookmarkRepository.insertBookmark(it)
            }
        }
    }
}