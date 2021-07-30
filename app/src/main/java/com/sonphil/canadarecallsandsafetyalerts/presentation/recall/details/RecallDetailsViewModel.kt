package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark.GetBookmarkForRecallUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging.RecordNonFatalExceptionUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.read_status.MarkRecallAsReadUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details.GetRecallsDetailsSectionsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details.RefreshRecallsDetailsSectionsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import com.sonphil.canadarecallsandsafetyalerts.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import javax.inject.Inject

/**
 * Created by Sonphil on 11-02-20.
 */

@HiltViewModel
class RecallDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRecallsDetailsSectionsUseCase: GetRecallsDetailsSectionsUseCase,
    private val refreshRecallsDetailsSectionsUseCase: RefreshRecallsDetailsSectionsUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    getBookmarkForRecallUseCase: GetBookmarkForRecallUseCase,
    markRecallAsReadUseCase: MarkRecallAsReadUseCase,
    recordNonFatalExceptionUseCase: RecordNonFatalExceptionUseCase
) : ViewModel() {
    val recall: Recall? = savedStateHandle.get<Recall>("recall")
    private lateinit var _recall: Recall

    init {
        if (recall == null) {
            recordNonFatalExceptionUseCase(NullPointerException("Recall argument is null"))
        } else {
            this._recall = recall
            viewModelScope.launch {
                markRecallAsReadUseCase(recall)
            }
        }
    }

    private val bookmark = getBookmarkForRecallUseCase(_recall)
        .asLiveData(context = viewModelScope.coroutineContext)
    val bookmarked: LiveData<Boolean> = bookmark.map { it != null }

    private val recallAndDetailsSectionsAndImages = getRecallsDetailsSectionsUseCase(_recall)
        .asLiveData(context = viewModelScope.coroutineContext)

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
            result is LoadResult.Loading
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
        viewModelScope.launch {
            val bookmarked = bookmark.value != null

            updateBookmarkUseCase(_recall, !bookmarked)
        }
    }

    fun refresh() {
        _loading.postValue(true)

        viewModelScope.launch {
            refreshRecallsDetailsSectionsUseCase(_recall).onFailure { throwable ->
                _error.postValue(throwable.message)
            }
            _loading.postValue(false)
        }
    }

    val menuItemsVisible = recallAndDetailsSectionsAndImages.map { result ->
        result.data?.basicInformation?.url != null
    }

    private val _navigateToUrl = MutableLiveData<Event<Uri>>()
    val navigateToUrl = _navigateToUrl
    private inline val LoadResult<RecallAndBasicInformationAndDetailsSectionsAndImages>.url: String?
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
