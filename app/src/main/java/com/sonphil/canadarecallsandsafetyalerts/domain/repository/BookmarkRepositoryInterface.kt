package com.sonphil.canadarecallsandsafetyalerts.domain.repository

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.Recall
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 15-08-20.
 */

interface BookmarkRepositoryInterface {
    suspend fun insertBookmark(bookmark: Bookmark)

    fun getBookmark(recall: Recall): Flow<Bookmark?>

    suspend fun deleteBookmark(recallId: String)
}
