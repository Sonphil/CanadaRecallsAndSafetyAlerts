package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity

/**
 * Created by Sonphil on 21-02-20.
 */

@Entity(
    primaryKeys = ["recallId", "name"]
)
data class RecallDetailsSection(
    val recallId: String,
    val name: String,
    val title: String,
    val text: String
)