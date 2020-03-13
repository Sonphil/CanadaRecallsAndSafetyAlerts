package com.sonphil.canadarecallsandsafetyalerts.domain.recall_details

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallDetailsRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsDetailsSectionsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallDetailsRepository: RecallDetailsRepository
) {
    suspend operator fun invoke(recall: Recall) {
        val lang = localeUtils.getCurrentLanguage()

        recallDetailsRepository.refreshRecallAndDetailsSectionsAndImages(recall, lang)
    }
}