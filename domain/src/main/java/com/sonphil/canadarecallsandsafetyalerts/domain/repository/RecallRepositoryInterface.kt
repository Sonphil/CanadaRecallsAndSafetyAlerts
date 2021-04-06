package com.sonphil.canadarecallsandsafetyalerts.domain.repository

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 15-08-20.
 */

interface RecallRepositoryInterface {
    /**
     * Returns the recent recalls and their bookmarks
     *
     * @param lang Whether the response is in English (en) or French (fr)
     */
    fun getRecallAndBookmarkAndReadStatus(
        lang: String
    ): Flow<LoadResult<List<RecallAndBookmarkAndReadStatus>>>

    suspend fun refreshRecallsAndBookmarks(lang: String)

    fun getBookmarkedRecalls(): Flow<LoadResult<List<RecallAndBookmarkAndReadStatus>>>

    suspend fun getNewRecalls(lang: String): List<Recall>

    suspend fun isThereAnyRecall(): Boolean

    suspend fun recallExistsInDatabase(recallId: String): Boolean

    suspend fun insertRecalls(recalls: List<Recall>)
}
