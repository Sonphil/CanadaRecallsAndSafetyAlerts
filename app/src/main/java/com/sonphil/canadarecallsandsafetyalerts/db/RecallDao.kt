package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallAndBookmark
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 01-02-20.
 */

@Dao
interface RecallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recalls: List<Recall>)

    @Transaction
    @Query(
        """
        SELECT * FROM recall 
        WHERE category IN (:categories) 
        ORDER BY datePublished DESC
        """
    )
    fun getAllRecallsAndBookmarksByCategories(categories: List<Category>): Flow<List<RecallAndBookmark>>

    @Query(
        """
            SELECT * FROM recall 
            WHERE EXISTS (SELECT 1 FROM bookmark WHERE recall.id = recallId) 
            ORDER BY (SELECT date FROM bookmark WHERE recall.id = recallId) DESC
        """
    )
    fun getBookmarkedRecalls(): Flow<List<RecallAndBookmark>>

    @Query("DELETE FROM recall")
    suspend fun deleteAll()

    @Transaction
    suspend fun refreshRecalls(recalls: List<Recall>) {
        deleteAll()
        insertAll(recalls)
    }
}