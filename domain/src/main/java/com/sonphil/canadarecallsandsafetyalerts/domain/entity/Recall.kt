package com.sonphil.canadarecallsandsafetyalerts.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Created by Sonphil on 31-01-20.
 */

@Parcelize
@Entity
data class Recall(
    val category: Category,
    val datePublished: Long?,
    @PrimaryKey
    val id: String,
    val title: String?,
    val apiUrl: String?
) : Parcelable
