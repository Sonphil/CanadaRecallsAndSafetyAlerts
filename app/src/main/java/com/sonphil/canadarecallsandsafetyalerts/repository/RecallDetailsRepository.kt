package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDetailsImageDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDetailsSectionDao
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.repository.mapper.toRecallAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallDetailsRepository @Inject constructor(
    private val api: CanadaGovernmentApi,
    private val recallDao: RecallDao,
    private val recallDetailsSectionDao: RecallDetailsSectionDao,
    private val recallDetailsImageDao: RecallDetailsImageDao
) {
    fun getRecallAndDetailsSectionsAndImages(
        recall: Recall,
        lang: String
    ): Flow<StateData<RecallAndDetailsSectionsAndImages>> = flow {
        emit(StateData.Loading<RecallAndDetailsSectionsAndImages>(null))

        val dbValue = recallDao
            .getRecallAndSectionsAndImagesById(recall.id)
            .catch { }
            .first()

        emit(StateData.Loading(dbValue))

        try {
            val apiValue = api.recallDetails(recall.id, lang)
                .toRecallAndDetailsSectionsAndImages(recall)

            recallDetailsSectionDao.refreshRecallDetailsSectionsForRecall(apiValue.sections, recall)
            recallDetailsImageDao.refreshRecallDetailsImagesForRecall(apiValue.images, recall)
        } catch (cause: Throwable) {
            emit(StateData.Error(cause.message, dbValue))
        } finally {
            emitAll(recallDao.getRecallAndSectionsAndImagesById(recall.id).map { recalls ->
                StateData.Success(recalls)
            })
        }
    }
}