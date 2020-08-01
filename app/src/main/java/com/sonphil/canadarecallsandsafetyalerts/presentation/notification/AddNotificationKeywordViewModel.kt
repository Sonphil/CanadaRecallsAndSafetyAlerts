package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.domain.notification_keyword.AddNotificationKeywordUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 20-02-20.
 */

class AddNotificationKeywordViewModel @Inject constructor(
    private val addNotificationKeywordUseCase: AddNotificationKeywordUseCase
) : ViewModel() {
    private val _dismissDialog: MutableLiveData<Event<Unit>> = MutableLiveData()
    val dismissDialog: LiveData<Event<Unit>> = _dismissDialog

    fun insertNewKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addNotificationKeywordUseCase(keyword)
            _dismissDialog.postValue(null)
        }
    }
}
