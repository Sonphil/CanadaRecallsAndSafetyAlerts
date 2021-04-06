package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.BaseRecallViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class MyRecallsViewModel @Inject constructor(
    private val appDispatchers: AppDispatchers,
    getBookmarkedRecallsUseCase: GetBookmarkedRecallsUseCase,
    updateBookmarkUseCase: UpdateBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
) : BaseRecallViewModel(updateBookmarkUseCase) {
    private val bookmarkedRecallsWithLoadResult: LiveData<LoadResult<List<RecallAndBookmarkAndReadStatus>>> =
        getBookmarkedRecallsUseCase().asLiveData(viewModelScope.coroutineContext)

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
        viewModelScope.launch(appDispatchers.default) {
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

        viewModelScope.launch {
            lastBookmarkRemoved.value?.let {
                addBookmarkUseCase(it)
            }
        }
    }
}
