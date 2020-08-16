package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.read_status

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.ReadStatusRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class MarkRecallAsReadUseCase @Inject constructor(
    private val readStatusRepository: ReadStatusRepositoryInterface
) {
    suspend operator fun invoke(recall: Recall) {
        val readStatus = ReadStatus(recall.id)

        readStatusRepository.insertReadStatus(readStatus)
    }
}
