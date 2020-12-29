package com.sonphil.canadarecallsandsafetyalerts.domain.model

/**
 * Created by Sonphil on 22-02-20.
 */

class RecallDetailsBasicInformation(
    val recallId: String,
    val recallFullId: String,
    val url: String?,
    val title: String?,
    val startDate: Long?,
    val datePublished: Long?
)
