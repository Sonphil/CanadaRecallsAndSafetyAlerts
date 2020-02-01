package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 01-02-20.
 */

@Entity
data class Bookmark(
    @PrimaryKey
    val recallId: String,
    val date: Int
)