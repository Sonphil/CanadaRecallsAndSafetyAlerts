package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.ReadStatusDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.repository.mapper.toRecalls
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallRepository @Inject constructor(
    private val api: CanadaGovernmentApi,
    private val recallDao: RecallDao,
    private val readStatusDao: ReadStatusDao
) {
    /**
     * Returns the recent recalls and their bookmarks
     *
     * @param lang Whether the response is in English (en) or French (fr)
     */
    fun getRecallsAndBookmarks(
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

    suspend fun markRecallAsRead(recall: Recall) {
        val readStatus = ReadStatus(recall.id)

        readStatusDao.insertReadStatus(readStatus)
    }

    /**
     * Fetches recent recalls from the server and returns [Recall]s that are more recent than the
     * most recent one of the DB
     *
     * If a recall from the server has a date whose value is null or is equal to the date of the DB,
     * this function considers that the recall is new if the recall doesn't exist in the DB.
     *
     * @param lang Whether the response from the server should be in English (en) or French (fr)
     */
    suspend fun getNewRecalls(lang: String): List<Recall> {
        return if (recallDao.isEmpty()) {
            emptyList()
        } else {
            api.recentRecalls(lang)
                .results
                .all
                .orEmpty()
                .filter { apiRecall ->
                    val existsInDb = recallDao.getRecallsWithIdCount(apiRecall.recallId) == 1

                    !existsInDb
                }
                .toRecalls()
                .also { newRecalls ->
                    recallDao.insertAll(newRecalls)
                }
        }
    }

    companion object {
        const val SEARCH_DEFAULT_TEXT = ""
        const val SEARCH_CATEGORY = ""
        const val SEARCH_LIMIT = 200
        const val SEARCH_OFFSET = 0
    }
}