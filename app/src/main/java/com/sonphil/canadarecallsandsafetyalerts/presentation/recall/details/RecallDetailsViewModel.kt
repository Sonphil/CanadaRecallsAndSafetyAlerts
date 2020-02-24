package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.net.Uri
import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallDetailsRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 11-02-20.
 */

class RecallDetailsViewModel @Inject constructor(
    private val app: App,
    private val recall: Recall,
    private val localeUtils: LocaleUtils,
    private val bookmarkRepository: BookmarkRepository,
    private val recallRepository: RecallRepository,
    private val recallDetailsRepository: RecallDetailsRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            recallRepository.markRecallAsRead(recall)
        }
    }

    private val bookmark = bookmarkRepository
        .getBookmark(recall)
        .asLiveData(context = viewModelScope.coroutineContext + Dispatchers.IO)
    val bookmarked: LiveData<Boolean> = bookmark.map { it != null }
    val bookmarkDate: LiveData<Long?> = bookmark.map { bookmark -> bookmark?.date }

    private val recallAndDetailsSectionsAndImages = recallDetailsRepository
        .getRecallAndDetailsSectionsAndImages(recall, localeUtils.getCurrentLanguage())
        .asLiveData(context = viewModelScope.coroutineContext + Dispatchers.IO)

    val images = recallAndDetailsSectionsAndImages.map { it.data?.images }
    val galleryVisible = images.map { !it.isNullOrEmpty() }

    private val _loading = MediatorLiveData<Boolean>().apply {
        val source = recallAndDetailsSectionsAndImages.map { stateData ->
            stateData is StateData.Loading
        }

        addSource(source) { loading ->
            value = loading
        }
    }
    val loading: LiveData<Boolean> = _loading

    private val _error = MediatorLiveData<String?>().apply {
        val source = recallAndDetailsSectionsAndImages.map { stateData ->
            stateData.message
        }

        addSource(source) { errorMessage ->
            value = errorMessage
        }
    }

    fun clickBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarked = bookmark.value != null

            bookmarkRepository.updateBookmark(recall, !bookmarked)
        }
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _loading.postValue(true)

            recallDetailsRepository.refreshRecallAndDetailsSectionsAndImages(
                recall,
                localeUtils.getCurrentLanguage()
            )
        } catch (t: Throwable) {
            _loading.postValue(false)
            _error.postValue(t.message)
        }
    }

    private suspend fun getRecallUrl(): String? {
        var url = recallAndDetailsSectionsAndImages
            .value
            ?.url

        if (url == null) {
            url = recallAndDetailsSectionsAndImages
                .asFlow()
                .filter { stateData ->
                    stateData !is StateData.Loading && stateData.url != null
                }
                .map {
                    it.url
                }
                .first()
        }

        return url
    }

    val _navigateToUrl = MutableLiveData<Event<Uri>>()
    val navigateToUrl = _navigateToUrl
    private inline val StateData<RecallAndBasicInformationAndDetailsSectionsAndImages>.url: String?
        get() = this.data?.basicInformation?.url

    fun clickRecallUrl() {
        viewModelScope.launch(Dispatchers.Default) {
            val url = getRecallUrl()

            if (url != null) {
                _navigateToUrl.postValue(Event(Uri.parse(url)))
            }
        }
    }

    private val _shareUrl = MutableLiveData<Event<String>>()
    val shareUrl = _shareUrl
    fun clickShareUrl() {
        viewModelScope.launch(Dispatchers.Default) {
            val url = getRecallUrl()

            if (url != null) {
                _shareUrl.postValue(Event(url))
            }
        }
    }
}