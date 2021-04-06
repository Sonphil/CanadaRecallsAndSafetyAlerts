package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall

import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging.RecordNonFatalExceptionUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepositoryInterface,
    private val recordNonFatalExceptionUseCase: RecordNonFatalExceptionUseCase
) {
    suspend operator fun invoke(): Flow<LoadResult<Unit>> = flow {
        try {
            emit(LoadResult.Loading(Unit))

            recallRepository.refreshRecallsAndBookmarks(localeUtils.getCurrentLanguage())

            emit(LoadResult.Success(Unit))
        } catch (t: Throwable) {
            recordNonFatalExceptionUseCase(t)
            emit(LoadResult.Error(Unit, t))
        }
    }
}
