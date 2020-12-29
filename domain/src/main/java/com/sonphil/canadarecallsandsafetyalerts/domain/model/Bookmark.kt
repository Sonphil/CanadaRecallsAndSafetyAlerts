package com.sonphil.canadarecallsandsafetyalerts.domain.model

/**
 * Indicates that a [Recall] has been bookmarked by the user
 */

data class Bookmark(
    val recallId: String,
    val date: Long
)
