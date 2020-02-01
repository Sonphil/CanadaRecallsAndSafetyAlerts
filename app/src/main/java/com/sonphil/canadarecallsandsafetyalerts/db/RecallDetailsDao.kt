package com.sonphil.canadarecallsandsafetyalerts.db;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallDetails
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 01-02-20.
 */

@Dao
interface RecallDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recall: RecallDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recallDetails: List<RecallDetails>)

    @Query("SELECT * FROM recalldetails")
    fun getAll(): Flow<List<RecallDetails>>
}
