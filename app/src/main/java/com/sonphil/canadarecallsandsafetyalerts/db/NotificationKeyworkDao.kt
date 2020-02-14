package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.entity.NotificationKeyword
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