package com.sonphil.canadarecallsandsafetyalerts.domain.repository

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.utils.Result
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
    ): Flow<Result<List<RecallAndBookmarkAndReadStatus>>>

    suspend fun refreshRecallsAndBookmarks(lang: String)

    fun getBookmarkedRecalls(): Flow<Result<List<RecallAndBookmarkAndReadStatus>>>

    suspend fun getNewRecallsFromApi(lang: String): List<Recall>

    suspend fun isThereAnyRecall(): Boolean

    suspend fun recallExists(recallId: String): Boolean

    suspend fun insertRecalls(recalls: List<Recall>)
}
