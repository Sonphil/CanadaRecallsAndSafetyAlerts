package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmark
import com.sonphil.canadarecallsandsafetyalerts.repository.mapper.toRecalls
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallRepository @Inject constructor(
    private val api: CanadaGovernmentApi,
    private val dao: RecallDao
) {
    /**
     * Returns the recent recalls and their bookmarks
     *
     * @param lang Whether the response is in English (en) or French (fr)
     * @param categories Categories to filter the recalls by
     */
    fun getRecallsAndBookmarks(
        lang: String
    ): Flow<StateData<List<RecallAndBookmark>>> = flow {

        val dBValues = dao.getAllRecallsAndBookmarksFilteredByCategories()
            .catch { }
            .first()

        emit(StateData.loading(dBValues))

        try {
            val apiValues = api
                .searchRecall("", lang, "", 50, 0)
                .toRecalls()

            dao.refreshRecalls(apiValues)
        } catch (cause: Throwable) {
            emit(StateData.error(cause.message, dBValues))
        } finally {
            // Always emit DB values because the user might try again on failure
            emitAll(dao.getAllRecallsAndBookmarksFilteredByCategories().map { recalls ->
                StateData.success(recalls)
            })
        }
    }

    suspend fun refreshRecallsAndBookmarks(lang: String) {
        val apiValues = api
            .searchRecall("", lang, "", 80, 0)
            .toRecalls()

        dao.refreshRecalls(apiValues)
    }

    fun getBookmarkedRecalls(): Flow<StateData<List<RecallAndBookmark>>> = flow {
        emitAll(dao.getBookmarkedRecalls().map { recalls ->
            StateData.success(recalls)
        })
    }
}