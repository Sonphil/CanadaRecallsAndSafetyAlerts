package com.sonphil.canadarecallsandsafetyalerts.di

import com.sonphil.canadarecallsandsafetyalerts.data.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.data.repository.CategoryFilterRepository
import com.sonphil.canadarecallsandsafetyalerts.data.repository.NotificationKeywordsRepository
import com.sonphil.canadarecallsandsafetyalerts.data.repository.ReadStatusRepository
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallDetailsRepository
import com.sonphil.canadarecallsandsafetyalerts.data.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.BookmarkRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.CategoryFilterRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.NotificationKeywordsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.ReadStatusRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallDetailsRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.RecallRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Sonphil on 15-08-20.
 */

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideBookmarkRepository(
        bookmarkRepository: BookmarkRepository
    ): BookmarkRepositoryInterface

    @Binds
    fun provideCategoryFilterRepository(
        categoryFilterRepository: CategoryFilterRepository
    ): CategoryFilterRepositoryInterface

    @Binds
    fun provideNotificationKeywordsRepository(
        notificationKeywordsRepository: NotificationKeywordsRepository
    ): NotificationKeywordsRepositoryInterface

    @Binds
    fun provideReadStatusRepository(
        readStatusRepository: ReadStatusRepository
    ): ReadStatusRepositoryInterface

    @Binds
    fun provideRecallDetailsRepository(
        recallDetailsRepository: RecallDetailsRepository
    ): RecallDetailsRepositoryInterface

    @Binds
    fun provideRecallRepository(
        recallRepository: RecallRepository
    ): RecallRepositoryInterface
}
