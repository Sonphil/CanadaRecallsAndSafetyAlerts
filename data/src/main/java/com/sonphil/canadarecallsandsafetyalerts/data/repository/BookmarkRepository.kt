package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.BookmarkDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toBookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.BookmarkRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class BookmarkRepository @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val dao: BookmarkDao
) : BookmarkRepositoryInterface {
    override suspend fun insertBookmark(bookmark: Bookmark) = withContext(appDispatchers.io) {
        dao.insertBookmark(bookmark.toBookmark())
    }

    override fun getBookmark(recall: Recall) = dao.getBookmarkByRecallId(recall.id).map {
        it?.toBookmark()
    }.flowOn(appDispatchers.io)

    override suspend fun deleteBookmark(recallId: String) = withContext(appDispatchers.io) {
        dao.deleteBookmark(recallId)
    }
}
