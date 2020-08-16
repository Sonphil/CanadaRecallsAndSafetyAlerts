package com.sonphil.canadarecallsandsafetyalerts.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 15-08-20.
 */

interface NotificationKeywordsRepositoryInterface {
    fun getKeywords(): Flow<List<String>>

    suspend fun insertNewKeyword(keyword: String)

    suspend fun deleteKeyword(keyword: String)
}
