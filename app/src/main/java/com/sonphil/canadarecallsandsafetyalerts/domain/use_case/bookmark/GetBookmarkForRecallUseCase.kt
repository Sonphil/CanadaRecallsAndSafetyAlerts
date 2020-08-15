package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.BookmarkRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetBookmarkForRecallUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepositoryInterface
) {
    operator fun invoke(recall: Recall) = bookmarkRepository.getBookmark(recall)
}
