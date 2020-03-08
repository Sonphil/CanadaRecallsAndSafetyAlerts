package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDetailsBasicInformationDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDetailsImageDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDetailsSectionDao
import com.sonphil.canadarecallsandsafetyalerts.di.qualifier.CanadaApiBaseUrl
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
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
    private val recallDetailsBasicInformationDao: RecallDetailsBasicInformationDao,
    private val recallDetailsSectionDao: RecallDetailsSectionDao,
    private val recallDetailsImageDao: RecallDetailsImageDao,
    @CanadaApiBaseUrl private val apiBaseUrl: String
) {
    fun getRecallAndDetailsSectionsAndImages(
        recall: Recall,
        lang: String
    ): Flow<StateData<RecallAndBasicInformationAndDetailsSectionsAndImages>> = flow {
        emit(StateData.Loading<RecallAndBasicInformationAndDetailsSectionsAndImages>(null))

        val dbValue = recallDao
            .getRecallAndSectionsAndImagesById(recall.id)

        emit(StateData.Loading(dbValue))

        try {
            refreshRecallAndDetailsSectionsAndImages(recall, lang)
        } catch (cause: Throwable) {
            emit(StateData.Error(cause.message, dbValue))
        } finally {
            // Continue to emit DB value
            emitAll(recallDao.getRecallAndSectionsAndImagesByIdFlow(recall.id).map { recalls ->
                StateData.Success(recalls)
            })
        }
    }

    suspend fun refreshRecallAndDetailsSectionsAndImages(recall: Recall, lang: String) {
        val apiValue = api
            .recallDetails(recall.id, lang)
            .toRecallAndDetailsSectionsAndImages(recall, apiBaseUrl)

        refreshDb(apiValue, recall)
    }

    private suspend fun refreshDb(
        apiValue: RecallAndBasicInformationAndDetailsSectionsAndImages,
        recall: Recall
    ) {
        apiValue.basicInformation?.let { basicInfo ->
            recallDetailsBasicInformationDao.insert(basicInfo)
        }
        apiValue.detailsSections?.let { sections ->
            recallDetailsSectionDao.refreshRecallDetailsSectionsForRecall(
                sections,
                recall
            )
        }
        apiValue.images?.let { images ->
            recallDetailsImageDao.refreshRecallDetailsImagesForRecall(images, recall)
        }
    }
}