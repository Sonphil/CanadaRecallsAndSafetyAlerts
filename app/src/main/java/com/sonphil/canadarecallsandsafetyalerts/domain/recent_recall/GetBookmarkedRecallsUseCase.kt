package com.sonphil.canadarecallsandsafetyalerts.domain.recent_recall

import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.StateData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetBookmarkedRecallsUseCase @Inject constructor(
    private val recallRepository: RecallRepository
) {
    operator fun invoke(): Flow<StateData<List<RecallAndBookmarkAndReadStatus>>> {
        return recallRepository.getBookmarkedRecalls()
    }
}