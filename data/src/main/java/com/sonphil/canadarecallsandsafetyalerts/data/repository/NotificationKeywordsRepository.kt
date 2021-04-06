package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.NotificationKeyworkDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.NotificationKeyword
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.NotificationKeywordsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 13-02-20.
 */

class NotificationKeywordsRepository @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val dao: NotificationKeyworkDao
) : NotificationKeywordsRepositoryInterface {
    override fun getKeywords(): Flow<List<String>> = dao.getKeywordsFlow()
        .flowOn(appDispatchers.io)

    override suspend fun insertNewKeyword(keyword: String) = withContext(appDispatchers.io) {
        dao.insertKeyword(NotificationKeyword(keyword))
    }

    override suspend fun deleteKeyword(keyword: String) = withContext(appDispatchers.io) {
        dao.deleteKeyword(NotificationKeyword(keyword))
    }
}
