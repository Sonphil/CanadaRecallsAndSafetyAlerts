package com.sonphil.canadarecallsandsafetyalerts.model

/**
 * Created by Sonphil on 31-01-20.
 */

data class Recall(
    val category: List<Category>,
    val datePublished: Int?,
    val recallId: String?,
    val title: String?,
    val url: String?
)