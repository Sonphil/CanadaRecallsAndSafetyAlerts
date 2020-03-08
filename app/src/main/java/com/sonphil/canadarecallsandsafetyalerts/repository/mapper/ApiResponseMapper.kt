package com.sonphil.canadarecallsandsafetyalerts.repository.mapper

import android.webkit.URLUtil
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
    apiUrl
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
        "details",
        "media_enquiries",
        "id_numbers",
        "product_"
    )
): List<RecallDetailsSection> = orEmpty()
    .filter { panel ->
        !ignoredPanelsNames.any { panel.panelName.startsWith(it, true) }
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
 * For the second case, we simply to add the base url at the beginning.
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