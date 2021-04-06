package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.data.api.mapper.toRecallAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDetailsBasicInformationDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDetailsImageDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.RecallDetailsSectionDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toDbRecall
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toDbRecallDetailsBasicInformation
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toDbRecallDetailsSectionList
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toDbRecallImages
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toRecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.data.ext.getRefreshedDatabaseFlow
import com.sonphil.canadarecallsandsafetyalerts.domain.di.qualifier.CanadaApiBaseUrl
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallDetailsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallDetailsRepository @Inject constructor(
    private val appDispatchers: AppDispatchers,
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
    ): Flow<LoadResult<RecallAndBasicInformationAndDetailsSectionsAndImages>> =
        getRefreshedDatabaseFlow(
            initialDbCall = {
                recallDao.getRecallAndSectionsAndImagesById(recall.id)
                    ?.toRecallAndBasicInformationAndDetailsSectionsAndImages()
            },
            refreshCall = { refreshRecallAndDetailsSectionsAndImages(recall, lang) },
            dbFlow = {
                recallDao.getRecallAndSectionsAndImagesByIdFlow(recall.id).map {
                    it.toRecallAndBasicInformationAndDetailsSectionsAndImages()
                }
            }
        ).flowOn(appDispatchers.io)

    override suspend fun refreshRecallAndDetailsSectionsAndImages(recall: Recall, lang: String) = withContext(appDispatchers.io) {
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
            recallDetailsBasicInformationDao.insert(basicInfo.toDbRecallDetailsBasicInformation())
        }
        apiValue.detailsSections?.let { sections ->
            recallDetailsSectionDao.refreshRecallDetailsSectionsForRecall(
                sections.toDbRecallDetailsSectionList(),
                recall.toDbRecall()
            )
        }
        apiValue.images?.let { images ->
            recallDetailsImageDao.refreshRecallDetailsImagesForRecall(
                images.toDbRecallImages(),
                recall.toDbRecall()
            )
        }
    }
}
