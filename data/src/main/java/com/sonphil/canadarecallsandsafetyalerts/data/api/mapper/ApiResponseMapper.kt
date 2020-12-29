package com.sonphil.canadarecallsandsafetyalerts.data.api.mapper

import android.webkit.URLUtil
import com.sonphil.canadarecallsandsafetyalerts.data.api.model.details.ApiRecallDetailsPanel
import com.sonphil.canadarecallsandsafetyalerts.data.api.model.details.ApiRecallDetailsResponse
import com.sonphil.canadarecallsandsafetyalerts.data.api.model.recent.ApiRecall
import com.sonphil.canadarecallsandsafetyalerts.data.api.model.recent.ApiRecentRecallsResponse
import com.sonphil.canadarecallsandsafetyalerts.data.api.model.search.ApiSearchResponse
import com.sonphil.canadarecallsandsafetyalerts.data.api.model.search.ApiSearchResult
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallDetailsBasicInformation
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallDetailsSection
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallDetailsSectionType
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallImage

/**
 * Created by Sonphil on 01-02-20.
 */

fun ApiRecall.toRecall() = Recall(
    Category.values()[category.first() - 1],
    datePublished?.times(1000L),
    recallId,
    title,
    apiUrl
)

fun List<ApiRecall>.toRecalls() = map { it.toRecall() }

fun ApiRecentRecallsResponse.toRecalls(): List<Recall> = results.all?.toRecalls().orEmpty()

fun ApiSearchResult.toRecall(): Recall {
    val categoryType = Category.values().find {
        it.value == category.first()
    } ?: Category.MISCELLANEOUS

    return Recall(
        categoryType,
        datePublished?.times(1000L),
        recallId,
        title,
        url
    )
}

fun ApiSearchResponse.toRecalls(): List<Recall> = results.orEmpty().map { it.toRecall() }

fun ApiRecallDetailsPanel.toRecallDetailsSection(recall: Recall): RecallDetailsSection {
    val type = RecallDetailsSectionType
        .values()
        .find { type ->
            type.name.equals(panelName, true)
        } ?: RecallDetailsSectionType.OTHER

    return RecallDetailsSection(
        recall.id,
        panelName,
        type,
        this.title,
        this.text ?: ""
    )
}

fun List<ApiRecallDetailsPanel>?.toRecallDetailsSections(
    recall: Recall,
    ignoredPanelsNames: Set<String> = setOf(
        "images",
        "media_enquiries",
        "id_numbers",
        "product_"
    )
): List<RecallDetailsSection> = orEmpty()
    .filter { panel ->
        val containsTable = panel.text?.contains("<table")

        !ignoredPanelsNames.any { panel.panelName.startsWith(it, true) } &&
            containsTable == false // Ignore texts with tables until we implement a way to display them properly
    }.map { panel ->
        panel.toRecallDetailsSection(recall)
    }

fun ApiRecallDetailsPanel.toRecallDetailsImages(
    recall: Recall,
    apiBaseUrl: String
) = data?.map { apiImage ->
    RecallImage(
        recall.id,
        apiImage.fullUrl.processedUrl(apiBaseUrl),
        apiImage.thumbUrl.processedUrl(apiBaseUrl),
        apiImage.title
    )
}.orEmpty()

/**
 * Attempts to fix the URL provided by the API
 *
 * Examples of URLs sent by the API:
 *
 * 1) /recall-alert-rappel-avis/inspection/2020/assets/http://wwwqa.inspection.gc.ca/DAM/DAM-recall-rappel/STAGING/images-images/20200221a_1582330689190_fra.jpg
 * 2) /recall-alert-rappel-avis/hc-sc/2020/assets/ra-72335-01_s_fs_3_20200214-150635_17_fr.jpg
 *
 * For the first case, we need to remove the first part and remove "qa" from the URL and use https.
 * For the second case, we simply need to add the base url at the beginning.
 */
private fun String.processedUrl(apiBaseUrl: String): String {
    val delimiter = "http"

    if (contains(delimiter)) {
        var url = delimiter + this.split(delimiter)
            .last()
            .replaceFirst("qa", "")

        if (!URLUtil.isHttpsUrl(url)) {
            url = url.replaceFirst("http://", "https://")
        }

        return url
    } else {
        return apiBaseUrl + this
    }
}

fun List<ApiRecallDetailsPanel>?.toRecallDetailsImages(
    recall: Recall,
    apiBaseUrl: String
) = this
    ?.find { panel ->
        panel.title.equals("images", true) && panel.data != null
    }?.toRecallDetailsImages(recall, apiBaseUrl)
    .orEmpty()

fun ApiRecallDetailsResponse.toBasicInformation(recall: Recall) = RecallDetailsBasicInformation(
    recall.id,
    recallId,
    url,
    title,
    startDate,
    datePublished
)

fun ApiRecallDetailsResponse.toRecallAndDetailsSectionsAndImages(
    recall: Recall,
    apiBaseUrl: String
) =
    RecallAndBasicInformationAndDetailsSectionsAndImages(
        recall,
        basicInformation = this.toBasicInformation(recall),
        detailsSections = panels.toRecallDetailsSections(recall),
        images = panels.toRecallDetailsImages(recall, apiBaseUrl)
    )
