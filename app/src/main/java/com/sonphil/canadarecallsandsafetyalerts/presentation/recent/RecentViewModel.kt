package com.sonphil.canadarecallsandsafetyalerts.presentation.recent

import android.app.Application
import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmark
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
    application: Application,
    private val recallRepository: RecallRepository
) : AndroidViewModel(application) {
    private val recentRecallsWithLoadState: LiveData<StateData<List<RecallAndBookmark>>> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val currentLang = LocaleUtils.getCurrentLanguage(getApplication())
            val categories = Category.values().asList()

            val source = recallRepository
                .getRecallsAndBookmarks(currentLang, categories)
                .asLiveData()

            emitSource(source)
        }

    val recentRecalls = recentRecallsWithLoadState.map { stateData ->
        stateData.data
    }

    val loading = recentRecallsWithLoadState.map { stateData ->
        stateData.status == StateData.Status.LOADING
    }

    val error = recentRecallsWithLoadState.map { stateData ->
        stateData.message
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        val currentLang = LocaleUtils.getCurrentLanguage(getApplication())

        recallRepository.refreshRecallsAndBookmarks(currentLang)
    }
}