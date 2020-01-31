package com.sonphil.canadarecallsandsafetyalerts.model

/**
 * Created by Sonphil on 31-01-20.
 */

data class RecallDetails(
    val category: List<Category>,
    val datePublished: Int?,
    val basicDetails: String?,
    val summary: String?,
    val whatToDo: String?,
    val productDescription: String,
    val images: List<RecallDetailsImage>,
    val recallId: String?,
    val startDate: Int?,
    val title: String?,
    val url: String?
)