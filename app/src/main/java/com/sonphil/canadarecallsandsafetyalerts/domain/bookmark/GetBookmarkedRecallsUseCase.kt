package com.sonphil.canadarecallsandsafetyalerts.domain.bookmark

import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetBookmarkedRecallsUseCase @Inject constructor(
    private val recallRepository: RecallRepository
) {
    operator fun invoke() = recallRepository.getBookmarkedRecalls()
}
