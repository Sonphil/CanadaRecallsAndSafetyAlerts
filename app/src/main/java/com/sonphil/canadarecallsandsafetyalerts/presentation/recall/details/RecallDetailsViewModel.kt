package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.net.Uri
import androidx.lifecycle.*
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.GetBookmarkForRecallUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.read_status.MarkRecallAsReadUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.recall_details.GetRecallsDetailsSectionsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.recall_details.RefreshRecallsDetailsSectionsUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 11-02-20.
 */

class RecallDetailsViewModel @Inject constructor(
    private val recall: Recall,
    getRecallsDetailsSectionsUseCase: GetRecallsDetailsSectionsUseCase,
    private val refreshRecallsDetailsSectionsUseCase: RefreshRecallsDetailsSectionsUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    getBookmarkForRecallUseCase: GetBookmarkForRecallUseCase,
    markRecallAsReadUseCase: MarkRecallAsReadUseCase
) : ViewModel() {
    init {
        viewModelScope.launch {
            markRecallAsReadUseCase(recall)
        }
    }

    private val bookmark = getBookmarkForRecallUseCase(recall)
        .asLiveData(context = viewModelScope.coroutineContext + Dispatchers.IO)
    val bookmarked: LiveData<Boolean> = bookmark.map { it != null }
    val bookmarkDate: LiveData<Long?> = bookmark.map { bookmark -> bookmark?.date }

    private val recallAndDetailsSectionsAndImages = getRecallsDetailsSectionsUseCase(recall)
        .asLiveData(context = viewModelScope.coroutineContext + Dispatchers.IO)

    val detailsSectionsItems = recallAndDetailsSectionsAndImages.map { result ->
        result
            .data
            ?.detailsSections
            ?.flatMap { section ->
                setOf(
                    RecallDetailsSectionItem.RecallDetailsSectionHeaderItem(section.title),
                    RecallDetailsSectionItem.RecallDetailsSectionContentItem(section.text)
                )
            }
    }
    val images = recallAndDetailsSectionsAndImages.map { it.data?.images }

    val galleryVisible = images.map { !it.isNullOrEmpty() }

    private val _loading = MediatorLiveData<Boolean>().apply {
        val source = recallAndDetailsSectionsAndImages.map { result ->
            result is Result.Loading
        }

        addSource(source) { loading ->
            value = loading
        }
    }
    val loading: LiveData<Boolean> = _loading

    private val _error = MediatorLiveData<String?>().apply {
        val source = recallAndDetailsSectionsAndImages.map { result ->
            result.throwable?.message
        }

        addSource(source) { errorMessage ->
            value = errorMessage
        }
    }

    fun clickBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarked = bookmark.value != null

            updateBookmarkUseCase(recall, !bookmarked)
        }
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _loading.postValue(true)

            refreshRecallsDetailsSectionsUseCase(recall)
        } catch (t: Throwable) {
            _loading.postValue(false)
            _error.postValue(t.message)
        }
    }

    val menuItemsVisible = recallAndDetailsSectionsAndImages.map { result ->
        result.data?.basicInformation?.url != null
    }

    private val _navigateToUrl = MutableLiveData<Event<Uri>>()
    val navigateToUrl = _navigateToUrl
    private inline val Result<RecallAndBasicInformationAndDetailsSectionsAndImages>.url: String?
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