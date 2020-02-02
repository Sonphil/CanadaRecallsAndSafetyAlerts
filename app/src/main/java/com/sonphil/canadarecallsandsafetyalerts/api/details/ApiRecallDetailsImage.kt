package com.sonphil.canadarecallsandsafetyalerts.api.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRecallDetailsImage(
    @Json(name = "fullUrl")
    val fullUrl: String?,
    @Json(name = "thumbUrl")
    val thumbUrl: String?,
    @Json(name = "title")
    val title: String?
)