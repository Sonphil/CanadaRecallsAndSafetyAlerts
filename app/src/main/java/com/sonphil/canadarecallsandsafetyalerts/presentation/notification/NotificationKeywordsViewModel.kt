package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification_keyword.DeleteNotificationKeywordUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification_keyword.GetNotificationKeywordsUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 13-02-20.
 */

@HiltViewModel
class NotificationKeywordsViewModel @Inject constructor(
    getNotificationKeywordsUseCase: GetNotificationKeywordsUseCase,
    private val deleteNotificationKeywordUseCase: DeleteNotificationKeywordUseCase
) : ViewModel() {
    val keywords = getNotificationKeywordsUseCase()
        .asLiveData(viewModelScope.coroutineContext)

    private val lastKeywordDeleted = MutableLiveData<String>()

    val showKeywordDeletedSnackBar: LiveData<Event<Boolean>> = lastKeywordDeleted.map {
        Event(true)
    }

    val showEmptyView: LiveData<Boolean> = keywords.map { it.isNullOrEmpty() }

    fun deleteKeywordAtPosition(position: Int) {
        val keyword = keywords.value?.get(position)

        if (keyword != null) {
            deleteKeyword(keyword)
        }
    }

    private fun deleteKeyword(keyword: String) {
        viewModelScope.launch {
            deleteNotificationKeywordUseCase(keyword)

            lastKeywordDeleted.postValue(keyword)
        }
    }
}
