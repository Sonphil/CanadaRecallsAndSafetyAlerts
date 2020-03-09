package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.net.Uri
import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.ReadStatusRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallDetailsRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 11-02-20.
 */

class RecallDetailsViewModel @Inject constructor(
    private val recall: Recall,
    private val localeUtils: LocaleUtils,
    private val bookmarkRepository: BookmarkRepository,
    private val recallRepository: RecallRepository,
    private val recallDetailsRepository: RecallDetailsRepository,
    private val readStatusRepository: ReadStatusRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            readStatusRepository.markRecallAsRead(recall)
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

    val detailsSectionsItems = recallAndDetailsSectionsAndImages.map { stateData ->
        stateData
            .data
            ?.detailsSections
            ?.map { section ->
                setOf(
                    RecallDetailsSectionItem.RecallDetailsSectionHeaderItem(section.title),
                    RecallDetailsSectionItem.RecallDetailsSectionContentItem(section.text)
                )
            }
            ?.flatten()
    }
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

    val menuItemsVisible = recallAndDetailsSectionsAndImages.map { stateData ->
        stateData.data?.basicInformation?.url != null
    }

    private val _navigateToUrl = MutableLiveData<Event<Uri>>()
    val navigateToUrl = _navigateToUrl
    private inline val StateData<RecallAndBasicInformationAndDetailsSectionsAndImages>.url: String?
        get() = this.data?.basicInformation?.url

    fun clickRecallUrl() {
        val url = recallAndDetailsSectionsAndImages.value?.url

        if (url != null) {
            _navigateToUrl.value = Event(Uri.parse(url))
        }
    }

    private val _shareUrl = MutableLiveData<Event<String>>()
    val shareUrl = _shareUrl
    fun clickShareUrl() {
        val url = recallAndDetailsSectionsAndImages.value?.url

        if (url != null) {
            _shareUrl.value = Event(url)
        }
    }
}