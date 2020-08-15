package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall

import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepositoryInterface
) {
    suspend operator fun invoke(): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading(Unit))

            recallRepository.refreshRecallsAndBookmarks(localeUtils.getCurrentLanguage())

            emit(Result.Success(Unit))
        } catch (t: Throwable) {
            emit(Result.Error(Unit, t))
        }
    }
}
