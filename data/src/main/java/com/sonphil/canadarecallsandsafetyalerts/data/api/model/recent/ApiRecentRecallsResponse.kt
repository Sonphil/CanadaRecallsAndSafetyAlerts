package com.sonphil.canadarecallsandsafetyalerts.data.api.model.recent

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRecentRecallsResponse(
    @Json(name = "results")
    val results: ApiRecentRecallsResults
)
