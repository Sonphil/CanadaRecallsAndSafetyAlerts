package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.repository.NotificationKeywordsRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 13-02-20.
 */

class NotificationKeywordsViewModel @Inject constructor(
    private val repository: NotificationKeywordsRepository
) : ViewModel() {
    val keywords = repository
        .getKeywordsFlow()
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val lastKeywordDeleted = MutableLiveData<String>()

    val showKeywordDeletedSnackBar: LiveData<Event<Boolean>> = lastKeywordDeleted.map {
        Event(true)
    }

    val showEmptyView: LiveData<Boolean> = keywords.map { it.isNullOrEmpty() }

    fun insertNewKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNewKeyword(keyword)
        }
    }

    fun deleteKeywordAtPosition(position: Int) {
        val keyword = keywords.value?.get(position)

        if (keyword != null) {
            deleteKeyword(keyword)
        }
    }

    private fun deleteKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteKeyword(keyword)

            lastKeywordDeleted.postValue(keyword)
        }
    }
}