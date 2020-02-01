package com.sonphil.canadarecallsandsafetyalerts.db;

import androidx.room.*

import com.sonphil.canadarecallsandsafetyalerts.entity.RecallDetails
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 01-02-20.
 */

@Dao
@TypeConverters(CategoryTypeConverter::class)
interface RecallDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recall: RecallDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recallDetails: List<RecallDetails>)

    @Query("SELECT * FROM recall")
    fun getAll(): Flow<List<RecallDetails>>
}
