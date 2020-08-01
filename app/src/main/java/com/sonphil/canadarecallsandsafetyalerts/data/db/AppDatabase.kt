package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.data.entity.CategoryFilter
import com.sonphil.canadarecallsandsafetyalerts.data.entity.NotificationKeyword
import com.sonphil.canadarecallsandsafetyalerts.data.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallDetailsBasicInformation
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallDetailsSection
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallImage

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
