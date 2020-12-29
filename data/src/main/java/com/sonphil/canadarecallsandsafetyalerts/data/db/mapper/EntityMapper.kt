package com.sonphil.canadarecallsandsafetyalerts.data.db.mapper

import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.CategoryFilter
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.model.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallDetailsBasicInformation
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallDetailsSection
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallDetailsSectionType
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallImage
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.Bookmark as DbBookmark
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.ReadStatus as DbReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.Recall as DbRecall
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallAndBasicInformationAndDetailsSectionsAndImages as DbRecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallAndBookmarkAndReadStatus as DbRecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallDetailsBasicInformation as DbRecallDetailsBasicInformation
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallDetailsSection as DbRecallDetailsSection
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallImage as DbRecallImage

/**
 * Created by Sonphil on 29-12-20.
 */

fun Bookmark.toBookmark() = DbBookmark(
    recallId = recallId,
    date = date
)

fun DbBookmark.toBookmark() = Bookmark(
    recallId = recallId,
    date = date
)

fun Category.toCategoryFilter() = CategoryFilter(
    category = value
)

fun List<CategoryFilter>.toCategories() = map {
    it.toCategory()
}

fun CategoryFilter.toCategory() = category.toCategory()

fun Int.toCategory() = Category.values().find {
    it.value == this
} ?: Category.MISCELLANEOUS

fun ReadStatus.toDbReadStatus() = DbReadStatus(
    recallId = this.recallId
)

fun DbReadStatus.toReadStatus() = ReadStatus(
    recallId = this.recallId
)

fun List<Recall>.toDbRecallList() = map { it.toDbRecall() }

fun Recall.toDbRecall() = DbRecall(
    category = category.value,
    datePublished = datePublished,
    id = id,
    title = title,
    apiUrl = apiUrl
)

fun DbRecall.toRecall() = Recall(
    category = category.toCategory(),
    datePublished = datePublished,
    id = id,
    title = title,
    apiUrl = apiUrl
)

fun List<DbRecallAndBookmarkAndReadStatus>.toRecallAndBookmarkAndReadStatusList() = map {
    it.toRecallAndBookmarkAndReadStatus()
}

fun RecallAndBookmarkAndReadStatus.toDbRecallAndBookmarkAndReadStatus() =
    DbRecallAndBookmarkAndReadStatus(
        recall = recall.toDbRecall(),
        bookmark = bookmark?.toBookmark(),
        readStatus = readStatus?.toDbReadStatus()
    )

fun DbRecallAndBookmarkAndReadStatus.toRecallAndBookmarkAndReadStatus() =
    RecallAndBookmarkAndReadStatus(
        recall = recall.toRecall(),
        bookmark = bookmark?.toBookmark(),
        readStatus = readStatus?.toReadStatus()
    )

fun RecallAndBasicInformationAndDetailsSectionsAndImages.toDbRecallAndBasicInformationAndDetailsSectionsAndImages() =
    DbRecallAndBasicInformationAndDetailsSectionsAndImages(
        recall = recall.toDbRecall(),
        basicInformation = basicInformation?.toDbRecallDetailsBasicInformation(),
        detailsSections = detailsSections?.toDbRecallDetailsSectionList(),
        images = images?.toDbRecallImages()
    )

fun DbRecallAndBasicInformationAndDetailsSectionsAndImages.toRecallAndBasicInformationAndDetailsSectionsAndImages() =
    RecallAndBasicInformationAndDetailsSectionsAndImages(
        recall = recall.toRecall(),
        basicInformation = basicInformation?.toRecallDetailsBasicInformation(),
        detailsSections = detailsSections?.toRecallDetailsSectionList(),
        images = images?.toRecallImages()
    )

fun RecallDetailsBasicInformation.toDbRecallDetailsBasicInformation() =
    DbRecallDetailsBasicInformation(
        recallId = recallId,
        recallFullId = recallFullId,
        url = url,
        title = title,
        startDate = startDate,
        datePublished = datePublished
    )

fun DbRecallDetailsBasicInformation.toRecallDetailsBasicInformation() =
    RecallDetailsBasicInformation(
        recallId = recallId,
        recallFullId = recallFullId,
        url = url,
        title = title,
        startDate = startDate,
        datePublished = datePublished
    )

fun List<RecallDetailsSection>.toDbRecallDetailsSectionList() = map {
    it.toDbRecallDetailsSection()
}

fun List<DbRecallDetailsSection>.toRecallDetailsSectionList() = map {
    it.toRecallDetailsSection()
}

fun RecallDetailsSection.toDbRecallDetailsSection() = DbRecallDetailsSection(
    recallId = recallId,
    panelName = panelName,
    type = type.name,
    title = title,
    text = text
)

fun DbRecallDetailsSection.toRecallDetailsSection() = RecallDetailsSection(
    recallId = recallId,
    panelName = panelName,
    type = RecallDetailsSectionType.values()
        .find { type ->
            type.name.equals(this.type, true)
        } ?: RecallDetailsSectionType.OTHER,
    title = title,
    text = text
)

fun List<RecallImage>.toDbRecallImages() = map {
    it.toDbRecallImage()
}

fun List<DbRecallImage>.toRecallImages() = map {
    it.toRecallImage()
}

fun RecallImage.toDbRecallImage() = DbRecallImage(
    recallId = recallId,
    fullUrl = fullImageUrl,
    thumbUrl = thumbnailUrl,
    title = title
)

fun DbRecallImage.toRecallImage() = RecallImage(
    recallId = recallId,
    fullImageUrl = fullUrl,
    thumbnailUrl = thumbUrl,
    title = title
)
