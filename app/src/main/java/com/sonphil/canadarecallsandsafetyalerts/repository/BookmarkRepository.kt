package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.db.BookmarkDao
import com.sonphil.canadarecallsandsafetyalerts.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class BookmarkRepository @Inject constructor(
    private val dao: BookmarkDao
) {
    suspend fun updateBookmark(recall: Recall, bookmarked: Boolean) {
        if (bookmarked) {
            val bookmark = Bookmark(recall.id, System.currentTimeMillis() / 1000L)

            dao.insertBookmark(bookmark)
        } else {
            dao.deleteBookmark(recall.id)
        }
    }
}