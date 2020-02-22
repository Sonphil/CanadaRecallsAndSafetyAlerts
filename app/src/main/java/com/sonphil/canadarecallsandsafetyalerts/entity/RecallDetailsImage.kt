package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Recall::class,
    parentColumns = ["id"],
    childColumns = ["recallId"],
    onDelete = ForeignKey.CASCADE
)])
data class RecallDetailsImage(
    @ColumnInfo(index = true)
    val recallId: String,
    @PrimaryKey
    val fullUrl: String,
    val thumbUrl: String,
    val title: String
)