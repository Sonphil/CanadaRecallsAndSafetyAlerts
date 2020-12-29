package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.BookmarkDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toBookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.BookmarkRepositoryInterface
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class BookmarkRepository @Inject constructor(
    private val dao: BookmarkDao
) : BookmarkRepositoryInterface {
    override suspend fun insertBookmark(bookmark: Bookmark) {
        dao.insertBookmark(bookmark.toBookmark())
    }

    override fun getBookmark(recall: Recall) = dao.getBookmarkByRecallId(recall.id).map {
        it?.toBookmark()
    }

    override suspend fun deleteBookmark(recallId: String) = dao.deleteBookmark(recallId)
}
