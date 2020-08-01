package com.sonphil.canadarecallsandsafetyalerts.domain.bookmark

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.repository.BookmarkRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class UpdateBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
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
