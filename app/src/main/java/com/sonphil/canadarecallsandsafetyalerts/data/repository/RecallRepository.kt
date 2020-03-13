package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.repository.mapper.toRecalls
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallRepository @Inject constructor(
    private val api: CanadaGovernmentApi,
    private val recallDao: RecallDao
) {
    /**
     * Returns the recent recalls and their bookmarks
     *
     * @param lang Whether the response is in English (en) or French (fr)
     */
    fun getRecallAndBookmarkAndReadStatus(
        lang: String
    ): Flow<StateData<List<RecallAndBookmarkAndReadStatus>>> = flow {

        val dBValues = recallDao.getAllRecallsAndBookmarksFilteredByCategories()

        emit(StateData.Loading(dBValues))

        try {
            refreshRecallsAndBookmarks(lang)
        } catch (cause: Throwable) {
            emit(StateData.Error(cause.message, dBValues))
        } finally {
            // Always emit DB values because the user might try again on failure
            emitAll(recallDao.getAllRecallsAndBookmarksFilteredByCategoriesFlow().map { recalls ->
                StateData.Success(recalls)
            })
        }
    }

    suspend fun refreshRecallsAndBookmarks(lang: String) {
        val apiValues = api
            .searchRecall(SEARCH_DEFAULT_TEXT, lang, SEARCH_CATEGORY, SEARCH_LIMIT, SEARCH_OFFSET)
            .toRecalls()

        recallDao.refreshRecalls(apiValues)
    }

    fun getBookmarkedRecalls(): Flow<StateData<List<RecallAndBookmarkAndReadStatus>>> = flow {
        emitAll(recallDao.getBookmarkedRecalls().map { recalls ->
            StateData.Success(recalls)
        })
    }

    suspend fun getNewRecallsFromApi(lang: String): List<Recall> {
        return api.recentRecalls(lang)
            .results
            .all
            .orEmpty()
            .toRecalls()
    }

    suspend fun isThereAnyRecall() = !recallDao.isEmpty()

    suspend fun recallExists(recallId: String) = recallDao.getRecallsWithIdCount(recallId) == 1

    suspend fun insertRecalls(recalls: List<Recall>) = recallDao.insertAll(recalls)

    companion object {
        const val SEARCH_DEFAULT_TEXT = ""
        const val SEARCH_CATEGORY = ""
        const val SEARCH_LIMIT = 200
        const val SEARCH_OFFSET = 0
    }
}