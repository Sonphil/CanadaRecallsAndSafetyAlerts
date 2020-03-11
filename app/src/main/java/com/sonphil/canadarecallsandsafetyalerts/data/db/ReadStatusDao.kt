package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sonphil.canadarecallsandsafetyalerts.data.entity.ReadStatus

/**
 * Created by Sonphil on 11-02-20.
 */

@Dao
interface ReadStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadStatus(readStatus: ReadStatus)
}