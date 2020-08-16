package com.sonphil.canadarecallsandsafetyalerts.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 22-02-20.
 */

@Entity
class RecallDetailsBasicInformation(
    @PrimaryKey
    val recallId: String,
    val recallFullId: String,
    val url: String?,
    val title: String?,
    val startDate: Long?,
    val datePublished: Long?
)
