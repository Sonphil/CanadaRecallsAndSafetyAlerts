package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 31-01-20.
 */

@Entity
data class Recall(
    val category: Category,
    val datePublished: Long?,
    @PrimaryKey
    val id: String,
    val title: String?,
    val url: String?
)