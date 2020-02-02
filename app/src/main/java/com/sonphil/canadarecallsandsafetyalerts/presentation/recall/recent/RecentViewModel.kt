package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmark
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallBaseViewModel
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecentViewModel @Inject constructor(
    private val app: App,
    private val recallRepository: RecallRepository,
    bookmarkRepository: BookmarkRepository
) : RecallBaseViewModel(bookmarkRepository) {
    private val recentRecallsWithLoadState: LiveData<StateData<List<RecallAndBookmark>>> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val currentLang = LocaleUtils.getCurrentLanguage(app)
            // TODO: Let user filter by categories
            val categories = Category.values().asList()

            val source = recallRepository
                .getRecallsAndBookmarks(currentLang, categories)
                .asLiveData()

            emitSource(source)
        }

    val recentRecalls = recentRecallsWithLoadState.map { stateData ->
        stateData.data
    }

    private val _loading = MediatorLiveData<Boolean>().apply {
        val source = recentRecallsWithLoadState.map { stateData ->
            stateData.status == StateData.Status.LOADING
        }

        addSource(source) { loading ->
            value = loading
        }
    }
    val loading: LiveData<Boolean> = _loading

    private val _error = MediatorLiveData<String>().apply {
        val source = recentRecallsWithLoadState.map { stateData ->
            stateData.message
        }

        addSource(source) { errorMessage ->
            value = errorMessage
        }
    }
    val error: LiveData<String> = _error

    val emptyViewVisible = recentRecallsWithLoadState.map { stateData ->
        stateData.status != StateData.Status.LOADING && stateData.data.isNullOrEmpty()
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _loading.postValue(true)

            val currentLang = LocaleUtils.getCurrentLanguage(app)

            recallRepository.refreshRecallsAndBookmarks(currentLang)
        } catch (t: Throwable) {
            _loading.postValue(false)
            _error.postValue(t.message)
        }
    }
}