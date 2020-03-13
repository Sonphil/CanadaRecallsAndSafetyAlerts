package com.sonphil.canadarecallsandsafetyalerts.domain.read_status

import com.sonphil.canadarecallsandsafetyalerts.data.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.repository.ReadStatusRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class MarkRecallAsReadUseCase @Inject constructor(
    private val readStatusRepository: ReadStatusRepository
) {
    suspend operator fun invoke(recall: Recall) {
        val readStatus = ReadStatus(recall.id)

        readStatusRepository.insertReadStatus(readStatus)
    }
}