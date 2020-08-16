package com.sonphil.canadarecallsandsafetyalerts.domain.repository

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 15-08-20.
 */

interface RecallDetailsRepositoryInterface {
    fun getRecallAndDetailsSectionsAndImages(
        recall: Recall,
        lang: String
    ): Flow<Result<RecallAndBasicInformationAndDetailsSectionsAndImages>>

    suspend fun refreshRecallAndDetailsSectionsAndImages(recall: Recall, lang: String)
}
