package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Indicates a [Recall] has been read by the user
 */

@Entity
class ReadStatus(
    @PrimaryKey
    val recallId: String
)