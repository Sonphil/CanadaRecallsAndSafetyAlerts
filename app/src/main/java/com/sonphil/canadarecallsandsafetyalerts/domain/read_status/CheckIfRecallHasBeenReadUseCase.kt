package com.sonphil.canadarecallsandsafetyalerts.domain.read_status

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.repository.ReadStatusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 10-05-20.
 */

class CheckIfRecallHasBeenReadUseCase @Inject constructor(
    private val readStatusRepository: ReadStatusRepository
) {
    suspend operator fun invoke(
        recall: Recall
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val readStatus = readStatusRepository.getReadStatus(recall.id)

            readStatus != null
        }
    }
}
