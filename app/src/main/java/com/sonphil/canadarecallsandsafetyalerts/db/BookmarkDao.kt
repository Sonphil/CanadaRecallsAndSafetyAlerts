package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonphil.canadarecallsandsafetyalerts.entity.Bookmark

/**
 * Created by Sonphil on 01-02-20.
 */

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmark WHERE recallId = :recallId")
    suspend fun deleteBookmark(recallId: String)
}