package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Sonphil on 01-02-20.
 */

data class RecallAndDetailsSectionsAndImages(
    @Embedded
    val recall: Recall,
    @Relation(
        parentColumn = "id",
        entityColumn = "recallId",
        entity = RecallDetailsSection::class
    )
    val sections: List<RecallDetailsSection>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recallId",
        entity = RecallDetailsImage::class
    )
    val images: List<RecallDetailsImage>
)