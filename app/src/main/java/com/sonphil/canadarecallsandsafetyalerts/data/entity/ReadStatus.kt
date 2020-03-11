package com.sonphil.canadarecallsandsafetyalerts.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Indicates a [Recall] has been read by the user
 */

@Entity
data class ReadStatus(
    @PrimaryKey
    val recallId: String
)