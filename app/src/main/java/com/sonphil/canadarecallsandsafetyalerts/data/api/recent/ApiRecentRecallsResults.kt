package com.sonphil.canadarecallsandsafetyalerts.data.api.recent

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRecentRecallsResults(
    @Json(name = "ALL")
    val all: List<ApiRecall>?,
    @Json(name = "CPS")
    val cps: List<ApiRecall>?,
    @Json(name = "FOOD")
    val food: List<ApiRecall>?,
    @Json(name = "HEALTH")
    val health: List<ApiRecall>?,
    @Json(name = "VEHICLE")
    val vehicle: List<ApiRecall>?
)