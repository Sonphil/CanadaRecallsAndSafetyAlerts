package com.sonphil.canadarecallsandsafetyalerts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Indicates that a [Recall] has been bookmarked by the user
 */

@Entity
data class Bookmark(
    @PrimaryKey
    val recallId: String,
    val date: Long
)