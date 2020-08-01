package com.sonphil.canadarecallsandsafetyalerts.domain.bookmark

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.repository.BookmarkRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetBookmarkForRecallUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    operator fun invoke(recall: Recall) = bookmarkRepository.getBookmark(recall)
}
