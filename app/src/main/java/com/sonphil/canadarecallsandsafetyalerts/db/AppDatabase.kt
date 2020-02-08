package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sonphil.canadarecallsandsafetyalerts.entity.*

/**
 * Created by Sonphil on 01-02-20.
 */

@Database(
    entities = [
        Bookmark::class,
        Recall::class,
        RecallDetails::class,
        RecallDetailsImage::class,
        CategoryFilter::class,
        ReadStatus::class
    ],
    version = 1
)
@TypeConverters(CategoryTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recallDao(): RecallDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun recallDetailsDao(): RecallDetailsDao
    abstract fun categoryFilterDao(): CategoryFilterDao
}