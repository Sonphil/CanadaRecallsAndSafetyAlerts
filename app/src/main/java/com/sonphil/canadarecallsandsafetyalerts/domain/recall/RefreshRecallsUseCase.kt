package com.sonphil.canadarecallsandsafetyalerts.domain.recall

import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.sonphil.canadarecallsandsafetyalerts.utils.Result

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepository
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