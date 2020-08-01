package com.sonphil.canadarecallsandsafetyalerts.data.api.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSearchResponse(
    @Json(name = "results")
    val results: List<ApiSearchResult>?,
    @Json(name = "results_count")
    val resultsCount: Int?
)
