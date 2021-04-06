package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall

import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging.RecordNonFatalExceptionUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepositoryInterface,
    private val recordNonFatalExceptionUseCase: RecordNonFatalExceptionUseCase
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            recallRepository.refreshRecallsAndBookmarks(localeUtils.getCurrentLanguage())
            Result.success(Unit)
        } catch (t: Throwable) {
            recordNonFatalExceptionUseCase(t)
            Result.failure(t)
        }
    }
}
