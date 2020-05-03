package com.sonphil.canadarecallsandsafetyalerts.domain.recall

import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class RefreshRecallsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepository
) {
    suspend operator fun invoke() = recallRepository.refreshRecallsAndBookmarks(localeUtils.getCurrentLanguage())
}