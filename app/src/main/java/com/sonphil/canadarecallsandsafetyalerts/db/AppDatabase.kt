package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sonphil.canadarecallsandsafetyalerts.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallDetails
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallDetailsImage

/**
 * Created by Sonphil on 01-02-20.
 */

@Database(
    entities = [
        Bookmark::class,
        Recall::class,
        RecallDetails::class,
        RecallDetailsImage::class
    ],
    version = 1
)
@TypeConverters(CategoryTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recallDao(): RecallDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun recallDetailsDao(): RecallDetailsDao
}