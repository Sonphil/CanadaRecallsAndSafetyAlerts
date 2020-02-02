package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = RecallDetails::class,
    parentColumns = ["recallId"],
    childColumns = ["recall_id"],
    onDelete = ForeignKey.CASCADE
)])
data class RecallDetailsImage(
    @PrimaryKey
    val fullUrl: String,
    val thumbUrl: String?,
    val title: String?,
    @ColumnInfo(name = "recall_id", index = true)
    val recallId: String
)