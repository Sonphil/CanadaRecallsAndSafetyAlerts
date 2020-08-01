package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.AddBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.GetBookmarkedRecallsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.BaseRecallViewModel
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class MyRecallsViewModel @Inject constructor(
    getBookmarkedRecallsUseCase: GetBookmarkedRecallsUseCase,
    updateBookmarkUseCase: UpdateBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase
) : BaseRecallViewModel(updateBookmarkUseCase) {
    private val bookmarkedRecallsWithLoadResult: LiveData<Result<List<RecallAndBookmarkAndReadStatus>>> =
        getBookmarkedRecallsUseCase().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val bookmarkedRecalls = bookmarkedRecallsWithLoadResult.map { result ->
        result.data
    }

    override val emptyViewVisible = bookmarkedRecalls.map { list ->
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
                addBookmarkUseCase(it)
            }
        }
    }
}
