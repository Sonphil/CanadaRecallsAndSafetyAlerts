package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.bookmark

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.BookmarkRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class AddBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepositoryInterface
) {
    suspend operator fun invoke(
        bookmark: Bookmark
    ) = bookmarkRepository.insertBookmark(bookmark)
}
