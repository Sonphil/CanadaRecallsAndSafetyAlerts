package com.sonphil.canadarecallsandsafetyalerts.api.response.recent


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRecentRecallsResponse(
    @Json(name = "results")
    val results: ApiRecentRecallsResults
)