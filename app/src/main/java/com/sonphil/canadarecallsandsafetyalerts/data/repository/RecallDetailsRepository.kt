package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.data.api.mapper.toRecallAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDetailsBasicInformationDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDetailsImageDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDetailsSectionDao
import com.sonphil.canadarecallsandsafetyalerts.di.qualifier.CanadaApiBaseUrl
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallDetailsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.ext.getRefreshedDatabaseFlow
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.flow.Flow
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
) : RecallDetailsRepositoryInterface {
    override fun getRecallAndDetailsSectionsAndImages(
        recall: Recall,
        lang: String
    ): Flow<Result<RecallAndBasicInformationAndDetailsSectionsAndImages>> = getRefreshedDatabaseFlow(
        initialDbCall = { recallDao.getRecallAndSectionsAndImagesById(recall.id) },
        refreshCall = { refreshRecallAndDetailsSectionsAndImages(recall, lang) },
        dbFlow = { recallDao.getRecallAndSectionsAndImagesByIdFlow(recall.id) }
    )

    override suspend fun refreshRecallAndDetailsSectionsAndImages(recall: Recall, lang: String) {
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
