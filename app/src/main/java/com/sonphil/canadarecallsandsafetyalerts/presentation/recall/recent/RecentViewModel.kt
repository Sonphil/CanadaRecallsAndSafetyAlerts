package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmark
import com.sonphil.canadarecallsandsafetyalerts.ext.isDeviceConnected
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallBaseViewModel
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.CategoryFilterRepository
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
    bookmarkRepository: BookmarkRepository,
    private val categoryFilterRepository: CategoryFilterRepository
) : RecallBaseViewModel(bookmarkRepository) {
    private val recentRecallsWithLoadState: LiveData<StateData<List<RecallAndBookmark>>> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val currentLang = LocaleUtils.getCurrentLanguage(app)

            val source = recallRepository
                .getRecallsAndBookmarks(currentLang)
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

    private val _error = MediatorLiveData<String?>().apply {
        val source = recentRecallsWithLoadState.map { stateData ->
            stateData.message
        }

        addSource(source) { errorMessage ->
            value = errorMessage
        }
    }
    val genericError: LiveData<String?> = _error.map { error ->
        if (app.isDeviceConnected && !error.isNullOrBlank()) {
            app.getString(R.string.error_generic)
        } else {
            null
        }
    }
    val networkError: LiveData<String?> = _error.map {
        if (!app.isDeviceConnected) {
            app.getString(R.string.error_offline)
        } else {
            null
        }
    }

    val emptyViewVisible = recentRecallsWithLoadState.map { stateData ->
        stateData.status != StateData.Status.LOADING && stateData.data.isNullOrEmpty()
    }

    val emptyProgressBarVisible = recentRecallsWithLoadState.map { stateData ->
        stateData.status == StateData.Status.LOADING && stateData.data.isNullOrEmpty()
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

    val categoryFilters: LiveData<List<Category>> = categoryFilterRepository
        .getFilters()
        .asLiveData(context = viewModelScope.coroutineContext + Dispatchers.IO)

    fun updateCategoryFilter(category: Category, checked: Boolean) = viewModelScope
        .launch(Dispatchers.IO) {
            if (checked) {
                categoryFilterRepository.addFilter(category)
            } else {
                categoryFilterRepository.removeFilter(category)
            }
        }
}