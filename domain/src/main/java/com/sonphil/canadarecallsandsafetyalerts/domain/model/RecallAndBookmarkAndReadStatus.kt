package com.sonphil.canadarecallsandsafetyalerts.domain.model

/**
 * Created by Sonphil on 01-02-20.
 */

data class RecallAndBookmarkAndReadStatus(
    val recall: Recall,
    val bookmark: Bookmark?,
    val readStatus: ReadStatus?
)
