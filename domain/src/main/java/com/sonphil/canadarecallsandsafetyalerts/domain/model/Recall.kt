package com.sonphil.canadarecallsandsafetyalerts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Sonphil on 31-01-20.
 */

@Parcelize
data class Recall(
    val category: Category,
    val datePublished: Long?,
    val id: String,
    val title: String?,
    val apiUrl: String?
) : Parcelable
