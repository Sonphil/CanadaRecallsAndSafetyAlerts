package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndDetailsSectionsAndImages
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 01-02-20.
 */

@Dao
interface RecallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recalls: List<Recall>)

    @Query("SELECT COUNT(id) FROM recall WHERE id = :recallId")
    suspend fun getRecallsWithIdCount(recallId: String): Int

    @Transaction
    @Query(
        """
        SELECT * FROM recall 
        WHERE category IN (SELECT category FROM categoryfilter) 
        ORDER BY datePublished DESC, id DESC
        """
    )
    fun getAllRecallsAndBookmarksFilteredByCategories(): Flow<List<RecallAndBookmarkAndReadStatus>>

    @Transaction
    @Query("SELECT * FROM recall WHERE id = :recallId")
    fun getRecallAndSectionsAndImagesById(recallId: String): Flow<RecallAndDetailsSectionsAndImages>

    @Transaction
    @Query(
        """
            SELECT * FROM recall 
            WHERE EXISTS (SELECT 1 FROM bookmark WHERE recall.id = recallId) 
            ORDER BY (SELECT date FROM bookmark WHERE recall.id = recallId) DESC
        """
    )
    fun getBookmarkedRecalls(): Flow<List<RecallAndBookmarkAndReadStatus>>

    @Query(
        """
            SELECT * 
            FROM recall 
            WHERE datePublished = (SELECT MAX(datePublished) FROM recall)
            LIMIT 1
    """
    )
    suspend fun getMostRecentRecall(): Recall?

    @Query(
        """
            DELETE FROM recall 
            WHERE NOT EXISTS (SELECT 1 FROM bookmark WHERE recall.id = recallId) 
            """
    )
    suspend fun deleteNotBookmarkedRecalls()

    @Transaction
    suspend fun refreshRecalls(recalls: List<Recall>) {
        deleteNotBookmarkedRecalls()
        insertAll(recalls)
    }
}