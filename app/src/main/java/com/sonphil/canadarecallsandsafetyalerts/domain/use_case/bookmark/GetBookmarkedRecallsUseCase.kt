package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark

import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetBookmarkedRecallsUseCase @Inject constructor(
    private val recallRepository: RecallRepositoryInterface
) {
    operator fun invoke() = recallRepository.getBookmarkedRecalls()
}
