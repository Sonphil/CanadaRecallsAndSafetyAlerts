package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.model.Category
import com.sonphil.canadarecallsandsafetyalerts.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.model.StateData
import com.sonphil.canadarecallsandsafetyalerts.repository.mapper.toRecalls
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
     * Returns the recent recalls
     *
     * @param lang Whether the response is in English (en) or French (fr)
     * @param categories Categories to filter the recalls by
     */
    fun getRecentRecalls(
        lang: String,
        categories: List<Category>
    ): Flow<StateData<List<Recall>>> = flow {

        val currentValues = dao.getAll()
            .catch {
                emit(StateData.error(it.message, emptyList()))
            }
            .first()
            .filter {
                it.category in categories
            }

        emit(StateData.loading(currentValues))

        try {
            val apiValues = api
                .recentRecalls(lang)
                .toRecalls()

            dao.deleteNotBookmarked()
            dao.insertAll(apiValues)

            emitAll(dao.getAll().map { recalls ->
                StateData.success(recalls)
            })
        } catch (cause: Throwable) {
            emit(StateData.error(cause.message, currentValues))
        }
    }
}