package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark.AddBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark.GetBookmarkedRecallsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallItemClickHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

@HiltViewModel
class MyRecallsViewModel @Inject constructor(
    private val appDispatchers: AppDispatchers,
    getBookmarkedRecallsUseCase: GetBookmarkedRecallsUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    recallItemClickHandler: RecallItemClickHandler
) : ViewModel(), RecallItemClickHandler by recallItemClickHandler {
    private val bookmarkedRecallsWithLoadResult: LiveData<LoadResult<List<RecallAndBookmarkAndReadStatus>>> =
        getBookmarkedRecallsUseCase().asLiveData(viewModelScope.coroutineContext)

    val bookmarkedRecalls = bookmarkedRecallsWithLoadResult.map { result ->
        result.data
    }

    val emptyViewVisible = bookmarkedRecalls.map { list ->
        list.orEmpty().isEmpty()
    }

    private val lastBookmarkRemoved = MutableLiveData<Bookmark?>()
    private val _showUndoUnbookmarkSnackbar = MutableLiveData<Boolean>()
    val showUndoUnbookmarkSnackbar = _showUndoUnbookmarkSnackbar

    fun onBookmarkClicked(recall: Recall, isCurrentlyBookmarked: Boolean) {
        viewModelScope.launch(appDispatchers.default) {
            val shouldBookmark = !isCurrentlyBookmarked

            if (!shouldBookmark) {
                val bookmark = bookmarkedRecalls
                    .value
                    ?.find { it.recall == recall }
                    ?.bookmark

                lastBookmarkRemoved.postValue(bookmark)
                _showUndoUnbookmarkSnackbar.postValue(true)
            }

            updateBookmarkUseCase(recall, shouldBookmark)
        }
    }

    fun undoLastUnbookmark() {
        _showUndoUnbookmarkSnackbar.value = false

        viewModelScope.launch {
            lastBookmarkRemoved.value?.let {
                addBookmarkUseCase(it)
            }
        }
    }
}
