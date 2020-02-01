package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM recall")
    fun getAllRecallsAndBookmarks(): Flow<List<RecallAndBookmark>>

    @Query("SELECT * FROM recall WHERE id LIKE :id")
    fun getRecallById(id: Int): Flow<Recall>
}