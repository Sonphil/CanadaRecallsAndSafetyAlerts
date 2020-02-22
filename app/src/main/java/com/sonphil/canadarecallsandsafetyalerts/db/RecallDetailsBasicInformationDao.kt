package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallDetailsBasicInformation

/**
 * Created by Sonphil on 22-02-20.
 */

@Dao
interface RecallDetailsBasicInformationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(basicInformation: RecallDetailsBasicInformation)
}