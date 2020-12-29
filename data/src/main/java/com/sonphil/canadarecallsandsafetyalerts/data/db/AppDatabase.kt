package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.CategoryFilter
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.NotificationKeyword
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallDetailsBasicInformation
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallDetailsSection
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallImage

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
