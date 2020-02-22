package com.sonphil.canadarecallsandsafetyalerts.api.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRecallDetailsResponse(
    @Json(name = "category")
    val category: List<String>?,
    @Json(name = "date_published")
    val datePublished: Long?,
    @Json(name = "panels")
    val panels: List<ApiRecallDetailsPanel>?,
    @Json(name = "recallId")
    val recallId: String,
    @Json(name = "start_date")
    val startDate: Long?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "url")
    val url: String?
)