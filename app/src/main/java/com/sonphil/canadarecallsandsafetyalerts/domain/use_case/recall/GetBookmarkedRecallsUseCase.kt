package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetBookmarkedRecallsUseCase @Inject constructor(
    private val recallRepository: RecallRepositoryInterface
) {
    operator fun invoke(): Flow<Result<List<RecallAndBookmarkAndReadStatus>>> {
        return recallRepository.getBookmarkedRecalls()
    }
}
