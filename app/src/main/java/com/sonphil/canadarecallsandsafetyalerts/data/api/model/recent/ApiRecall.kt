package com.sonphil.canadarecallsandsafetyalerts.data.api.model.recent

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRecall(
    @Json(name = "category")
    val category: List<Int>,
    @Json(name = "date_published")
    val datePublished: Long?,
    @Json(name = "recallId")
    val recallId: String,
    @Json(name = "title")
    val title: String?,
    @Json(name = "url")
    val apiUrl: String?
)
