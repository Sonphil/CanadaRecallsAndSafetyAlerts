package com.sonphil.canadarecallsandsafetyalerts.data.api.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSearchResult(
    @Json(name = "category")
    val category: List<Int>,
    @Json(name = "date_published")
    val datePublished: Int?,
    @Json(name = "department")
    val department: String?,
    @Json(name = "recallId")
    val recallId: String,
    @Json(name = "title")
    val title: String?,
    @Json(name = "url")
    val url: String?
)
