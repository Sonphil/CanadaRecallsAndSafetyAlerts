package com.sonphil.canadarecallsandsafetyalerts.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 31-01-20.
 */

@Entity(foreignKeys = [ForeignKey(
    entity = Recall::class,
    parentColumns = ["id"],
    childColumns = ["recallId"],
    onDelete = CASCADE
)])
data class RecallDetails(
    val category: List<Category>,
    val datePublished: Int?,
    val basicDetails: String?,
    val summary: String?,
    val whatToDo: String?,
    val productDescription: String,
    val images: List<RecallDetailsImage>,
    @PrimaryKey
    val recallId: String,
    val startDate: Int?,
    val title: String?,
    val url: String?
)