package com.sonphil.canadarecallsandsafetyalerts.api.details


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRecallDetailsPanel(
    @Json(name = "data")
    val `data`: List<ApiRecallDetailsImage>?,
    @Json(name = "panelName")
    val panelName: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "title")
    val title: String?
)