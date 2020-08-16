package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.BookmarkRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class UpdateBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepositoryInterface
) {
    suspend operator fun invoke(recall: Recall, bookmarked: Boolean) {
        if (bookmarked) {
            val bookmark = Bookmark(recall.id, System.currentTimeMillis())

            bookmarkRepository.insertBookmark(bookmark)
        } else {
            bookmarkRepository.deleteBookmark(recall.id)
        }
    }
}
