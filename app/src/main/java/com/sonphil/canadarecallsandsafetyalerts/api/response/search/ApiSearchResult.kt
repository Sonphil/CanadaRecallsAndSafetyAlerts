package com.sonphil.canadarecallsandsafetyalerts.api.response.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSearchResult(
    @Json(name = "category")
    val category: List<String?>?,
    @Json(name = "date_published")
    val datePublished: Int?,
    @Json(name = "department")
    val department: String?,
    @Json(name = "recallId")
    val recallId: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "url")
    val url: String?
)