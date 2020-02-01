package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Sonphil on 01-02-20.
 */

data class RecallAndBookmark(
    @Embedded
    val recall: Recall,
    @Relation(
        parentColumn = "id",
        entityColumn = "recallId"
    )
    val bookmark: Bookmark?
)