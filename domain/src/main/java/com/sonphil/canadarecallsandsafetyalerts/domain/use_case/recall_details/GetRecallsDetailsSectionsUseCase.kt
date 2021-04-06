package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallDetailsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetRecallsDetailsSectionsUseCase @Inject constructor(
    private val localeUtils: LocaleUtils,
    private val recallRepository: RecallRepositoryInterface,
    private val recallDetailsRepository: RecallDetailsRepositoryInterface
) {
    operator fun invoke(recall: Recall): Flow<LoadResult<RecallAndBasicInformationAndDetailsSectionsAndImages>> {
        val lang = localeUtils.getCurrentLanguage()

        return flow {
            if (!recallRepository.recallExistsInDatabase(recall.id)) {
                recallRepository.insertRecalls(listOf(recall))
            }

            emitAll(recallDetailsRepository.getRecallAndDetailsSectionsAndImages(recall, lang))
        }
    }
}
