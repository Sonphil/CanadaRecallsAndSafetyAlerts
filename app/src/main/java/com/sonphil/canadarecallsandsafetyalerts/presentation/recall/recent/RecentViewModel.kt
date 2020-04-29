package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.ext.isDeviceConnected
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.BaseRecallViewModel
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.category_filter.GetCategoryFiltersUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.category_filter.UpdateFilterForCategoryUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.recent_recall.GetRecallsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.recent_recall.RefreshRecallsUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecentViewModel @Inject constructor(
    private val app: App,
    getRecallsUseCase: GetRecallsUseCase,
    private val refreshRecallsUseCase: RefreshRecallsUseCase,
    getCategoryFiltersUseCase: GetCategoryFiltersUseCase,
    private val updateFilterForCategoryUseCase: UpdateFilterForCategoryUseCase,
    updateBookmarkUseCase: UpdateBookmarkUseCase
) : BaseRecallViewModel(updateBookmarkUseCase) {
    private val recentRecallsWithLoadResult: LiveData<Result<List<RecallAndBookmarkAndReadStatus>>> =
        getRecallsUseCase().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val recentRecalls = recentRecallsWithLoadResult.map { result ->
        result.data
    }

    private val _loading = MediatorLiveData<Boolean>().apply {
        val source = recentRecallsWithLoadResult.map { result ->
            result is Result.Loading
        }

        addSource(source) { loading ->
            value = loading
        }
    }
    val loading: LiveData<Boolean> = _loading

    private val _error = MediatorLiveData<String?>().apply {
        val source = recentRecallsWithLoadResult.map { result ->
            result.throwable?.message
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

    override val emptyViewVisible = recentRecallsWithLoadResult.map { result ->
        result !is Result.Loading && result.data.isNullOrEmpty()
    }

    val emptyViewIconResId = emptyViewVisible.map {
        if (categoryFilters.value.isNullOrEmpty()) {
            R.drawable.ic_filter_list_white_24dp
        } else {
            R.drawable.ic_access_time_white_24dp
        }
    }

    val emptyViewTitleResId = emptyViewVisible.distinctUntilChanged().map {
        if (categoryFilters.value.isNullOrEmpty()) {
            R.string.title_empty_recent_recall_no_category_selected
        } else {
            R.string.title_empty_recent_recall
        }
    }

    val emptyViewRetryButtonVisible = emptyViewVisible
        .distinctUntilChanged()
        .map { emptyViewVisible ->
            emptyViewVisible && !categoryFilters.value.isNullOrEmpty()
        }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _loading.postValue(true)

            refreshRecallsUseCase()
        } catch (t: Throwable) {
            _loading.postValue(false)
            _error.postValue(t.message)
        }
    }

    val categoryFilters: LiveData<List<Category>> = getCategoryFiltersUseCase()
        .asLiveData(context = viewModelScope.coroutineContext + Dispatchers.IO)

    fun updateCategoryFilter(category: Category, checked: Boolean) = viewModelScope
        .launch(Dispatchers.IO) {
            updateFilterForCategoryUseCase(category, checked)
        }
}