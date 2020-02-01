package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sonphil.canadarecallsandsafetyalerts.db.CategoryTypeConverter

/**
 * Created by Sonphil on 31-01-20.
 */

@Entity
@TypeConverters(CategoryTypeConverter::class)
data class Recall(
    val category: Category,
    val datePublished: Int?,
    @PrimaryKey
    val id: String,
    val title: String?,
    val url: String?
)