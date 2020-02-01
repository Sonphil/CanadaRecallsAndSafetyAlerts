package com.sonphil.canadarecallsandsafetyalerts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 31-01-20.
 */

@Entity
data class Recall(
    val category: List<Category>,
    val datePublished: Int?,
    @PrimaryKey
    val recallId: String?,
    val title: String?,
    val url: String?,
    val bookMarked: Boolean = false
)