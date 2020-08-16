package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.NotificationKeyworkDao
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.NotificationKeyword
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.NotificationKeywordsRepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 13-02-20.
 */

class NotificationKeywordsRepository @Inject constructor(
    private val dao: NotificationKeyworkDao
) : NotificationKeywordsRepositoryInterface {
    override fun getKeywords(): Flow<List<String>> = dao.getKeywordsFlow()

    override suspend fun insertNewKeyword(keyword: String) = dao
        .insertKeyword(NotificationKeyword(keyword))

    override suspend fun deleteKeyword(keyword: String) = dao
        .deleteKeyword(NotificationKeyword(keyword))
}
