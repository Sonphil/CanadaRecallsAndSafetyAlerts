package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 07-02-20.
 */

@Entity
data class CategoryFilter(
    @PrimaryKey
    val category: Category
)