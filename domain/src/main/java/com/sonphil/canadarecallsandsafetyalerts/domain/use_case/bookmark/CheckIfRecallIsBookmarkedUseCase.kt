package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 10-05-20.
 */

class CheckIfRecallIsBookmarkedUseCase @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val getBookmarkedRecallsUseCase: GetBookmarkedRecallsUseCase
) {
    suspend operator fun invoke(
        recall: Recall
    ): Boolean {
        val bookmarkedRecalls = getBookmarkedRecallsUseCase().first()

        return withContext(appDispatchers.default) {
            bookmarkedRecalls.data?.any {
                it.recall == recall
            } ?: false
        }
    }
}
