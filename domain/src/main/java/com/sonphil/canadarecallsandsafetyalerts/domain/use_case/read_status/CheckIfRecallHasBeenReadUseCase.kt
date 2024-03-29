package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.read_status

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.ReadStatusRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 10-05-20.
 */

class CheckIfRecallHasBeenReadUseCase @Inject constructor(
    private val readStatusRepository: ReadStatusRepositoryInterface
) {
    suspend operator fun invoke(
        recall: Recall
    ): Boolean {
        val readStatus = readStatusRepository.getReadStatus(recall.id)

        return readStatus != null
    }
}
