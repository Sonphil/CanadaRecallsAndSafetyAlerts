package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallDetailsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetRecallsDetailsSectionsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallDetailsRepository: RecallDetailsRepositoryInterface
) {
    operator fun invoke(recall: Recall): Flow<Result<RecallAndBasicInformationAndDetailsSectionsAndImages>> {
        val lang = localeUtils.getCurrentLanguage()

        return recallDetailsRepository.getRecallAndDetailsSectionsAndImages(recall, lang)
    }
}