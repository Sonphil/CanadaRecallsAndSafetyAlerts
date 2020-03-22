package com.sonphil.canadarecallsandsafetyalerts.domain.recent_recall

import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetRecallsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepository
) {
    operator fun invoke(): Flow<Result<List<RecallAndBookmarkAndReadStatus>>> {
        val lang = localeUtils.getCurrentLanguage()

        return recallRepository.getRecallAndBookmarkAndReadStatus(lang)
    }
}