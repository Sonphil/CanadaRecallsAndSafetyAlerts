package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
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
        lang: String,
        categories: List<Category>
    ): Flow<StateData<List<RecallAndBookmark>>> = flow {

        val currentValues = dao.getAllRecallsAndBookmarksByCategories(categories)
            .catch { }
            .first()

        emit(StateData.loading(currentValues))

        try {
            val apiValues = api
                .recentRecalls(lang)
                .toRecalls()

            dao.insertAll(apiValues)

            emitAll(dao.getAllRecallsAndBookmarksByCategories(categories).map { recalls ->
                StateData.success(recalls)
            })
        } catch (cause: Throwable) {
            emit(StateData.error(cause.message, currentValues))
        }
    }

    suspend fun refreshRecallsAndBookmarks(lang: String) {
        val apiValues = api
            .recentRecalls(lang)
            .toRecalls()

        dao.insertAll(apiValues)
    }
}