package com.sonphil.canadarecallsandsafetyalerts.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecallImage(
    @ColumnInfo(index = true)
    val recallId: String,
    @PrimaryKey
    val fullUrl: String,
    val thumbUrl: String,
    val title: String
)
