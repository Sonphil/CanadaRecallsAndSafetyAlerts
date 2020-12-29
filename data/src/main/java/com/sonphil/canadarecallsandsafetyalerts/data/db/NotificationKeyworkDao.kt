package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.NotificationKeyword
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 13-02-20.
 */

@Dao
interface NotificationKeyworkDao {
    @Query("SELECT value FROM notificationkeyword")
    suspend fun getKeywords(): List<String>

    @Query("SELECT value FROM notificationkeyword")
    fun getKeywordsFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: NotificationKeyword)

    @Delete
    suspend fun deleteKeyword(keyword: NotificationKeyword)
}
