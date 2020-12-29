package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallDetailsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsDetailsSectionsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallDetailsRepository: RecallDetailsRepositoryInterface
) {
    suspend operator fun invoke(recall: Recall) {
        val lang = localeUtils.getCurrentLanguage()

        recallDetailsRepository.refreshRecallAndDetailsSectionsAndImages(recall, lang)
    }
}