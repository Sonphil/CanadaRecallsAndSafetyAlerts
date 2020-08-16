package com.sonphil.canadarecallsandsafetyalerts.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sonphil on 13-02-20.
 */

@Entity
data class NotificationKeyword(
    @PrimaryKey
    val value: String
)
