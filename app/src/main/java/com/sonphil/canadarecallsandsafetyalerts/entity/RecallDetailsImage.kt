package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(
    entity = RecallDetails::class,
    parentColumns = ["recallId"],
    childColumns = ["recallId"],
    onDelete = ForeignKey.CASCADE
)])
data class RecallDetailsImage(
    @PrimaryKey
    val fullUrl: String?,
    val thumbUrl: String?,
    val title: String?,
    val recallId: String
)