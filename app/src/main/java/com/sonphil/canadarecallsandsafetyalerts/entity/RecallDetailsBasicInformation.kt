package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 22-02-20.
 */

@Entity(foreignKeys = [ForeignKey(
    entity = Recall::class,
    parentColumns = ["id"],
    childColumns = ["recallId"],
    onDelete = ForeignKey.CASCADE
)])
class RecallDetailsBasicInformation(
    @PrimaryKey
    val recallId: String,
    val recallFullId: String,
    val url: String?,
    val title: String?,
    val startDate: Long?,
    val datePublished: Long?
)