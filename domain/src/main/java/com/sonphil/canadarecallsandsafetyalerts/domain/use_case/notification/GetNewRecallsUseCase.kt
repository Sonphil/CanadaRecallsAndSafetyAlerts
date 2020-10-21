package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LocaleUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class GetNewRecallsUseCase @Inject constructor(
    private val recallRepository: RecallRepositoryInterface,
    private val localeUtils: LocaleUtils
) {
    suspend operator fun invoke(): List<Recall> {
        return if (!recallRepository.isThereAnyRecall()) {
            emptyList()
        } else {
            newRecalls()
        }
    }

    private suspend fun newRecalls(): List<Recall> {
        val lang = localeUtils.getCurrentLanguage()
        val recentRecalls = recallRepository.getNewRecalls(lang)
        val newRecalls = recentRecalls.filter { recall ->
            !recallRepository.recallExists(recall.id)
        }

        coroutineScope {
            launch {
                recallRepository.insertRecalls(newRecalls)
            }
        }

        return newRecalls
    }
}
