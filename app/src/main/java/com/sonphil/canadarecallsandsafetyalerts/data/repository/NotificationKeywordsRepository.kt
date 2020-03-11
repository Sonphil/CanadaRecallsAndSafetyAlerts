package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.NotificationKeyworkDao
import com.sonphil.canadarecallsandsafetyalerts.data.entity.NotificationKeyword
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 13-02-20.
 */

class NotificationKeywordsRepository @Inject constructor(
    private val dao: NotificationKeyworkDao
) {
    suspend fun getKeywords(): List<String> = dao.getKeywords()

    fun getKeywordsFlow(): Flow<List<String>> = dao.getKeywordsFlow()

    suspend fun insertNewKeyword(keyword: String) = dao.insertKeyword(NotificationKeyword(keyword))

    suspend fun deleteKeyword(keyword: String) = dao.deleteKeyword(NotificationKeyword(keyword))
}