package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 10-05-20.
 */

class CheckIfRecallIsBookmarkedUseCase @Inject constructor(
    private val getBookmarkedRecallsUseCase: GetBookmarkedRecallsUseCase
) {
    suspend operator fun invoke(
        recall: Recall
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val bookmarkedRecalls = getBookmarkedRecallsUseCase().first()

            withContext(Dispatchers.Default) {
                bookmarkedRecalls.data?.any {
                    it.recall == recall
                } ?: false
            }
        }
    }
}
