package com.sonphil.canadarecallsandsafetyalerts.repository.mapper

import com.sonphil.canadarecallsandsafetyalerts.api.recent.ApiRecall
import com.sonphil.canadarecallsandsafetyalerts.api.recent.ApiRecentRecallsResponse
import com.sonphil.canadarecallsandsafetyalerts.api.search.ApiSearchResponse
import com.sonphil.canadarecallsandsafetyalerts.api.search.ApiSearchResult
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall

/**
 * Created by Sonphil on 01-02-20.
 */

fun ApiRecall.toRecall() = Recall(
    Category.values()[category.first() - 1],
    datePublished?.times(1000L),
    recallId,
    title,
    url
)

fun List<ApiRecall>.toRecalls() = map { it.toRecall() }

fun ApiRecentRecallsResponse.toRecalls(): List<Recall> = results.all?.toRecalls().orEmpty()

fun ApiSearchResult.toRecall() = Recall(
    Category.values()[category.first() - 1],
    datePublished?.times(1000L),
    recallId,
    title,
    url
)

fun ApiSearchResponse.toRecalls(): List<Recall> = results.orEmpty().map { it.toRecall() }