package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall

import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetBookmarkedRecallsUseCase @Inject constructor(
    private val recallRepository: RecallRepositoryInterface
) {
    operator fun invoke(): Flow<LoadResult<List<RecallAndBookmarkAndReadStatus>>> {
        return recallRepository.getBookmarkedRecalls()
    }
}
