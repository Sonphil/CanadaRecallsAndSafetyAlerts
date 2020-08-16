package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.data.api.mapper.toRecalls
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.data.ext.getRefreshedDatabaseFlow
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallRepository @Inject constructor(
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
    ): Flow<Result<List<RecallAndBookmarkAndReadStatus>>> = getRefreshedDatabaseFlow(
        initialDbCall = recallDao::getAllRecallsAndBookmarksFilteredByCategories,
        refreshCall = { refreshRecallsAndBookmarks(lang) },
        dbFlow = recallDao::getAllRecallsAndBookmarksFilteredByCategoriesFlow
    )

    override suspend fun refreshRecallsAndBookmarks(lang: String) {
        val apiValues = api
            .searchRecall(SEARCH_DEFAULT_TEXT, lang, SEARCH_CATEGORY, SEARCH_LIMIT, SEARCH_OFFSET)
            .toRecalls()

        recallDao.refreshRecalls(apiValues)
    }

    override fun getBookmarkedRecalls(): Flow<Result<List<RecallAndBookmarkAndReadStatus>>> = flow {
        emitAll(
            recallDao.getBookmarkedRecalls().map { recalls ->
                Result.Success(recalls)
            }
        )
    }

    override suspend fun getNewRecallsFromApi(lang: String): List<Recall> {
        return api.recentRecalls(lang)
            .results
            .all
            .orEmpty()
            .toRecalls()
    }

    override suspend fun isThereAnyRecall() = !recallDao.isEmpty()

    override suspend fun recallExists(recallId: String) = recallDao.getRecallsWithIdCount(recallId) == 1

    override suspend fun insertRecalls(recalls: List<Recall>) = recallDao.insertAll(recalls)

    companion object {
        const val SEARCH_DEFAULT_TEXT = ""
        const val SEARCH_CATEGORY = ""
        const val SEARCH_LIMIT = 200
        const val SEARCH_OFFSET = 0
    }
}
