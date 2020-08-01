package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.BookmarkDao
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class BookmarkRepository @Inject constructor(
    private val dao: BookmarkDao
) {
    suspend fun insertBookmark(bookmark: Bookmark) = dao.insertBookmark(bookmark)

    fun getBookmark(recall: Recall) = dao.getBookmarkByRecallId(recall.id)

    suspend fun deleteBookmark(recallId: String) = dao.deleteBookmark(recallId)
}
