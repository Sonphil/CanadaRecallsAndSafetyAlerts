package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sonphil.canadarecallsandsafetyalerts.data.entity.*

/**
 * Created by Sonphil on 01-02-20.
 */

@Database(
    entities = [
        Bookmark::class,
        Recall::class,
        RecallDetailsBasicInformation::class,
        RecallDetailsSection::class,
        RecallImage::class,
        CategoryFilter::class,
        ReadStatus::class,
        NotificationKeyword::class
    ],
    version = 1
)
@TypeConverters(value = [CategoryTypeConverter::class, RecallDetailsSectionTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun recallDao(): RecallDao
    abstract fun recallDetailsBasicInformationDao(): RecallDetailsBasicInformationDao
    abstract fun recallDetailsSectionDao(): RecallDetailsSectionDao
    abstract fun recallDetailsImageDao(): RecallDetailsImageDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun readStatusDao(): ReadStatusDao
    abstract fun categoryFilterDao(): CategoryFilterDao
    abstract fun notificationKeywordDao(): NotificationKeyworkDao
}