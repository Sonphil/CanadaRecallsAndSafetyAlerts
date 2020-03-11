package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallAndBookmarkAndReadStatus
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
    suspend fun getAllRecallsAndBookmarksFilteredByCategories(): List<RecallAndBookmarkAndReadStatus>

    @Transaction
    @Query(
        """
        SELECT * FROM recall 
        WHERE category IN (SELECT category FROM categoryfilter) 
        ORDER BY datePublished DESC, id DESC
        """
    )
    fun getAllRecallsAndBookmarksFilteredByCategoriesFlow(): Flow<List<RecallAndBookmarkAndReadStatus>>

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
            LIMIT 1
    """
    )
    suspend fun getOneRecall(): Recall?

    @Transaction
    suspend fun isEmpty() = getOneRecall() == null

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

    @Transaction
    @Query("SELECT * FROM recall WHERE id = :recallId")
    suspend fun getRecallAndSectionsAndImagesById(recallId: String): RecallAndBasicInformationAndDetailsSectionsAndImages?

    @Transaction
    @Query("SELECT * FROM recall WHERE id = :recallId")
    fun getRecallAndSectionsAndImagesByIdFlow(recallId: String): Flow<RecallAndBasicInformationAndDetailsSectionsAndImages>
}