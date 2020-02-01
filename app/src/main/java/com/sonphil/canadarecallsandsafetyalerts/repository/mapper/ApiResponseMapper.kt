package com.sonphil.canadarecallsandsafetyalerts.repository.mapper

import com.sonphil.canadarecallsandsafetyalerts.api.recent.ApiRecall
import com.sonphil.canadarecallsandsafetyalerts.api.recent.ApiRecentRecallsResponse
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall

/**
 * Created by Sonphil on 01-02-20.
 */

fun ApiRecall.toRecall() = Recall(
    Category.values()[category.first() - 1],
    datePublished,
    recallId,
    title,
    url
)

fun List<ApiRecall>.toRecalls() = map { it.toRecall() }

fun ApiRecentRecallsResponse.toRecalls(): List<Recall> {
    val recallList = mutableListOf<Recall>()

    recallList.addAll(results.cps?.toRecalls() ?: emptyList())
    recallList.addAll(results.food?.toRecalls() ?: emptyList())
    recallList.addAll(results.health?.toRecalls() ?: emptyList())
    recallList.addAll(results.vehicle?.toRecalls() ?: emptyList())

    return recallList
}