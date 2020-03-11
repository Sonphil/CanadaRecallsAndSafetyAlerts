package com.sonphil.canadarecallsandsafetyalerts.domain

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class GetNewRecallsUseCase @Inject constructor(
    private val recallRepository: RecallRepository,
    private val localeUtils: LocaleUtils
) {
    suspend operator fun invoke(): List<Recall> {
        val lang = localeUtils.getCurrentLanguage()

        return recallRepository.getNewRecalls(lang)
    }
}