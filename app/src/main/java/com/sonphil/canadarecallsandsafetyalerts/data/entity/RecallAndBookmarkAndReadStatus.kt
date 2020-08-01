package com.sonphil.canadarecallsandsafetyalerts.data.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Sonphil on 01-02-20.
 */

data class RecallAndBookmarkAndReadStatus(
    @Embedded
    val recall: Recall,
    @Relation(parentColumn = "id", entityColumn = "recallId", entity = Bookmark::class)
    val bookmark: Bookmark?,
    @Relation(parentColumn = "id", entityColumn = "recallId", entity = ReadStatus::class)
    val readStatus: ReadStatus?
)
