package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.category_filter.GetCategoryFiltersUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.category_filter.UpdateFilterForCategoryUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall.GetRecallsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall.RefreshRecallsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import com.sonphil.canadarecallsandsafetyalerts.ext.isDeviceConnected
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.BaseRecallViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
    private val recentRecallsWithLoadResult: LiveData<LoadResult<List<RecallAndBookmarkAndReadStatus>>> =
        getRecallsUseCase().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val recentRecalls = recentRecallsWithLoadResult.map { result ->
        result.data
    }

    private val _loading = MediatorLiveData<Boolean>().apply {
        val source = recentRecallsWithLoadResult.map { result ->
            result is LoadResult.Loading
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
        result !is LoadResult.Loading && result.data.isNullOrEmpty()
    }

    val emptyViewIconResId = emptyViewVisible.map {
        if (categoryFilters.value.isNullOrEmpty()) {
            R.drawable.ic_filter_list_white_24dp
        } else {
            R.drawable.ic_access_time_white_24dp
        }
    }

    val emptyViewTitleResId = emptyViewVisible.map {
        if (categoryFilters.value.isNullOrEmpty()) {
            R.string.title_empty_recent_recall_no_category_selected
        } else {
            R.string.title_empty_recent_recall
        }
    }

    val emptyViewRetryButtonVisible = emptyViewVisible
        .map { emptyViewVisible ->
            emptyViewVisible && !categoryFilters.value.isNullOrEmpty()
        }

    fun refresh() {
        _loading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            refreshRecallsUseCase().onFailure { throwable ->
                _error.postValue(throwable.message)
            }

            _loading.postValue(false)
        }
    }

    val categoryFilters: LiveData<List<Category>> = getCategoryFiltersUseCase()
        .asLiveData(context = viewModelScope.coroutineContext + Dispatchers.IO)

    fun updateCategoryFilter(category: Category, checked: Boolean) = viewModelScope
        .launch(Dispatchers.IO) {
            updateFilterForCategoryUseCase(category, checked)
        }
}
