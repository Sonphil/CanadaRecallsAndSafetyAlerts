package com.sonphil.canadarecallsandsafetyalerts.di

import android.app.Application
import androidx.room.Room
import com.sonphil.canadarecallsandsafetyalerts.db.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Sonphil on 28-02-18.
 */
@Module
internal open class DatabaseModule {

    @Singleton @Provides
    open fun provideDb(app: Application): AppDatabase =
            Room.databaseBuilder(app, AppDatabase::class.java, "canadarecallsandsafetyalerts.db")
                    .createFromAsset("database/canadarecallsandsafetyalertsinit.db")
                    .build()

    @Singleton @Provides
    fun provideRecallDao(db: AppDatabase): RecallDao = db.recallDao()

    @Singleton @Provides
    fun provideRecallDetailsSectionDao(db: AppDatabase): RecallDetailsSectionDao = db.recallDetailsSectionDao()

    @Singleton @Provides
    fun provideRecallDetailsImageDao(db: AppDatabase): RecallDetailsImageDao = db.recallDetailsImageDao()

    @Singleton @Provides
    fun provideBookmarkDao(db: AppDatabase): BookmarkDao = db.bookmarkDao()

    @Singleton @Provides
    fun provideReadStatusDao(db: AppDatabase): ReadStatusDao = db.readStatusDao()

    @Singleton @Provides
    fun provideCategoryFilterDao(db: AppDatabase): CategoryFilterDao = db.categoryFilterDao()

    @Singleton @Provides
    fun provideNotificationKeywordDao(db: AppDatabase): NotificationKeyworkDao = db.notificationKeywordDao()
}