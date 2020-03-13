package com.sonphil.canadarecallsandsafetyalerts.domain.bookmark

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.data.repository.BookmarkRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class AddBookmarkUseCase @Inject constructor(private val bookmarkRepository: BookmarkRepository) {
    suspend operator fun invoke(
        bookmark: Bookmark
    ) = bookmarkRepository.insertBookmark(bookmark)
}