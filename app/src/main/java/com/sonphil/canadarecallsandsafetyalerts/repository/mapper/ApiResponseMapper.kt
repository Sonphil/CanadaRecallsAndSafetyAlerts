package com.sonphil.canadarecallsandsafetyalerts.repository.mapper

import com.sonphil.canadarecallsandsafetyalerts.api.details.ApiRecallDetailsPanel
import com.sonphil.canadarecallsandsafetyalerts.api.details.ApiRecallDetailsResponse
import com.sonphil.canadarecallsandsafetyalerts.api.recent.ApiRecall
import com.sonphil.canadarecallsandsafetyalerts.api.recent.ApiRecentRecallsResponse
import com.sonphil.canadarecallsandsafetyalerts.api.search.ApiSearchResponse
import com.sonphil.canadarecallsandsafetyalerts.api.search.ApiSearchResult
import com.sonphil.canadarecallsandsafetyalerts.entity.*

/**
 * Created by Sonphil on 01-02-20.
 */

fun ApiRecall.toRecall() = Recall(
    Category.values()[category.first() - 1],
    datePublished?.times(1000L),
    recallId,
    title,
    url
)

fun List<ApiRecall>.toRecalls() = map { it.toRecall() }

fun ApiRecentRecallsResponse.toRecalls(): List<Recall> = results.all?.toRecalls().orEmpty()

fun ApiSearchResult.toRecall() = Recall(
    Category.values()[category.first() - 1],
    datePublished?.times(1000L),
    recallId,
    title,
    url
)

fun ApiSearchResponse.toRecalls(): List<Recall> = results.orEmpty().map { it.toRecall() }

fun ApiRecallDetailsPanel.toRecallDetailsSection(recall: Recall) = RecallDetailsSection(
    recall.id,
    this.panelName,
    this.title,
    this.text ?: ""
)

fun List<ApiRecallDetailsPanel>?.toRecallDetailsSections(recall: Recall) = orEmpty()
    .filter { panel ->
        !panel.title.equals("images", true)
    }.map { panel ->
        panel.toRecallDetailsSection(recall)
    }

fun ApiRecallDetailsPanel.toRecallDetailsImages(recall: Recall) = data?.map { apiImage ->
    RecallDetailsImage(recall.id, apiImage.fullUrl, apiImage.thumbUrl, apiImage.title)
}.orEmpty()

fun List<ApiRecallDetailsPanel>?.toRecallDetailsImages(recall: Recall) = this
    ?.find { panel ->
        panel.title.equals("images", true) && panel.data != null
    }?.toRecallDetailsImages(recall)
    .orEmpty()

fun ApiRecallDetailsResponse.toRecallAndDetailsSectionsAndImages(recall: Recall) =
    RecallDetailsSectionsAndImages(
        sections = panels.toRecallDetailsSections(recall),
        images = panels.toRecallDetailsImages(recall)
    )