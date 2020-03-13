package com.sonphil.canadarecallsandsafetyalerts.domain.recall_details

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallDetailsRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetRecallsDetailsSectionsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallDetailsRepository: RecallDetailsRepository
) {
    operator fun invoke(recall: Recall): Flow<StateData<RecallAndBasicInformationAndDetailsSectionsAndImages>> {
        val lang = localeUtils.getCurrentLanguage()

        return recallDetailsRepository.getRecallAndDetailsSectionsAndImages(recall, lang)
    }
}