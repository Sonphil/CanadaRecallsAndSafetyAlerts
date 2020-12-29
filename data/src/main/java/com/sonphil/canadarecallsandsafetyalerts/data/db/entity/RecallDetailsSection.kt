package com.sonphil.canadarecallsandsafetyalerts.data.db.entity

import androidx.room.Entity

/**
 * Created by Sonphil on 21-02-20.
 */

@Entity(
    primaryKeys = ["recallId", "panelName"]
)
data class RecallDetailsSection(
    val recallId: String,
    val panelName: String,
    val type: String,
    val title: String,
    val text: String
)
