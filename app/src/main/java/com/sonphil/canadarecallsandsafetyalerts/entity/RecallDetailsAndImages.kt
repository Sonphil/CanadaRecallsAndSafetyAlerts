package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Sonphil on 01-02-20.
 */

data class RecallDetailsAndImages(
    @Embedded
    val details: RecallDetails,
    @Relation(
        parentColumn = "recallId",
        entityColumn = "recallId",
        entity = RecallDetailsImage::class
    )
    val images: List<RecallDetailsImage>
)