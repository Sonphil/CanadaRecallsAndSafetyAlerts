package com.sonphil.canadarecallsandsafetyalerts.di

import android.app.Application
import androidx.room.Room
import com.sonphil.canadarecallsandsafetyalerts.db.AppDatabase
import com.sonphil.canadarecallsandsafetyalerts.db.BookmarkDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDetailsDao
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
                    .fallbackToDestructiveMigration()
                    .build()

    @Singleton @Provides
    fun provideRecallDao(db: AppDatabase): RecallDao = db.recallDao()

    @Singleton @Provides
    fun provideBookmarkDao(db: AppDatabase): BookmarkDao = db.bookmarkDao()

    @Singleton @Provides
    fun provideRecallDetailsDao(db: AppDatabase): RecallDetailsDao = db.recallDetailsDao()
}