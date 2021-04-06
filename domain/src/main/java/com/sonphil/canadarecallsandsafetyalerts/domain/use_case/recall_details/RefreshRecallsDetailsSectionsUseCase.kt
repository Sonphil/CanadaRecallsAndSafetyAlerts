package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallDetailsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging.RecordNonFatalExceptionUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsDetailsSectionsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallDetailsRepository: RecallDetailsRepositoryInterface,
    private val recordNonFatalExceptionUseCase: RecordNonFatalExceptionUseCase
) {
    suspend operator fun invoke(recall: Recall): Result<Unit> {
        val lang = localeUtils.getCurrentLanguage()

        return try {
            recallDetailsRepository.refreshRecallAndDetailsSectionsAndImages(recall, lang)
            Result.success(Unit)
        } catch (t: Throwable) {
            recordNonFatalExceptionUseCase(t)
            Result.failure(t)
        }
    }
}
