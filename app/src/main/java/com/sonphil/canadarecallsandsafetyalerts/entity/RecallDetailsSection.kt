package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Created by Sonphil on 21-02-20.
 */

@Entity(
    foreignKeys = [ForeignKey(
        entity = Recall::class,
        parentColumns = ["id"],
        childColumns = ["recallId"],
        onDelete = ForeignKey.CASCADE
    )],
    primaryKeys = ["recallId", "name"]
)
data class RecallDetailsSection(
    val recallId: String,
    val name: String,
    val title: String,
    val text: String
)