package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.data.api.mapper.toRecalls
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toDbRecallList
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toRecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toRecallAndBookmarkAndReadStatusList
import com.sonphil.canadarecallsandsafetyalerts.data.ext.getRefreshedDatabaseFlow
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallRepository @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val api: CanadaGovernmentApi,
    private val recallDao: RecallDao
) : RecallRepositoryInterface {
    /**
     * Returns the recent recalls and their bookmarks
     *
     * @param lang Whether the response is in English (en) or French (fr)
     */
    override fun getRecallAndBookmarkAndReadStatus(
        lang: String
    ): Flow<LoadResult<List<RecallAndBookmarkAndReadStatus>>> = getRefreshedDatabaseFlow(
        initialDbCall = {
            recallDao.getAllRecallsAndBookmarksFilteredByCategories().map {
                it.toRecallAndBookmarkAndReadStatus()
            }
        },
        refreshCall = { refreshRecallsAndBookmarks(lang) },
        dbFlow = {
            recallDao.getAllRecallsAndBookmarksFilteredByCategoriesFlow().map {
                it.toRecallAndBookmarkAndReadStatusList()
            }
        }
    ).flowOn(appDispatchers.io)

    override suspend fun refreshRecallsAndBookmarks(lang: String) = withContext(appDispatchers.io) {
        val apiValues = api
            .searchRecall(SEARCH_DEFAULT_TEXT, lang, SEARCH_CATEGORY, SEARCH_LIMIT, SEARCH_OFFSET)
            .toRecalls()

        recallDao.refreshRecalls(apiValues.toDbRecallList())
    }

    override fun getBookmarkedRecalls(): Flow<LoadResult<List<RecallAndBookmarkAndReadStatus>>> = flow {
        emitAll(
            recallDao.getBookmarkedRecalls().map { recalls ->
                LoadResult.Success(recalls.toRecallAndBookmarkAndReadStatusList())
            }
        )
    }.flowOn(appDispatchers.io)

    override suspend fun getNewRecalls(lang: String): List<Recall> = withContext(appDispatchers.io) {
        api.recentRecalls(lang)
            .results
            .all
            .orEmpty()
            .toRecalls()
    }

    override suspend fun isThereAnyRecall() = withContext(appDispatchers.io) {
        !recallDao.isEmpty()
    }

    override suspend fun recallExists(recallId: String) = withContext(appDispatchers.io) {
        recallDao.getRecallsWithIdCount(recallId) == 1
    }

    override suspend fun insertRecalls(recalls: List<Recall>) = withContext(appDispatchers.io) {
        recallDao.insertAll(recalls.toDbRecallList())
    }

    companion object {
        const val SEARCH_DEFAULT_TEXT = ""
        const val SEARCH_CATEGORY = ""
        const val SEARCH_LIMIT = 200
        const val SEARCH_OFFSET = 0
    }
}
