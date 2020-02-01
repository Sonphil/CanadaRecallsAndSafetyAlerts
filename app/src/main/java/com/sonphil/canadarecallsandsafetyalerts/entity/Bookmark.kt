package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 01-02-20.
 */

data class Bookmark(
    @PrimaryKey
    val recallId: String,
    val date: Int
)