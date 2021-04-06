package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall

import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetRecallsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepositoryInterface
) {
    operator fun invoke(): Flow<LoadResult<List<RecallAndBookmarkAndReadStatus>>> {
        val lang = localeUtils.getCurrentLanguage()

        return recallRepository.getRecallAndBookmarkAndReadStatus(lang)
    }
}
