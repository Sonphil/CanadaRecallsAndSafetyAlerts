package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonphil.canadarecallsandsafetyalerts.model.Recall
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 01-02-20.
 */

@Dao
interface RecallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recall: Recall)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recalls: List<Recall>)

    @Query("DELETE FROM recall WHERE bookMarked = 0")
    suspend fun deleteNotBookmarked()

    @Query("SELECT * FROM recall")
    fun getAll(): Flow<List<Recall>>

    @Query("SELECT * FROM recall WHERE bookMarked = 1")
    fun getAllBookmarked(): Flow<List<Recall>>
}