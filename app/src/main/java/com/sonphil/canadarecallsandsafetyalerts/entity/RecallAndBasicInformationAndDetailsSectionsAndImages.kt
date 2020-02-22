package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Sonphil on 01-02-20.
 */

data class RecallAndBasicInformationAndDetailsSectionsAndImages(
    @Embedded
    val recall: Recall,
    @Relation(
        parentColumn = "id",
        entityColumn = "recallId",
        entity = RecallDetailsBasicInformation::class
    )
    val basicInformation: RecallDetailsBasicInformation?,
    @Relation(
        parentColumn = "id",
        entityColumn = "recallId",
        entity = RecallDetailsSection::class
    )
    val detailsSections: List<RecallDetailsSection>?,
    @Relation(
        parentColumn = "id",
        entityColumn = "recallId",
        entity = RecallImage::class
    )
    val images: List<RecallImage>?
)