package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallBaseViewModel
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class MyRecallsViewModel @Inject constructor(
    private val recallRepository: RecallRepository,
    bookmarkRepository: BookmarkRepository
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
}