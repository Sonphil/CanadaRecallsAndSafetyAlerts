package com.sonphil.canadarecallsandsafetyalerts.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.DebouncingRecallItemClickHandler
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.RecallItemClickHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Created by Sonphil on 28-02-18.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideApp(application: Application): App = application as App

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)

    @Singleton
    @Provides
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers(
        main = Dispatchers.Main,
        default = Dispatchers.Default,
        io = Dispatchers.IO
    )

    @Provides
    fun provideRecallClickHandler(
        debouncingRecallClickHandler: DebouncingRecallItemClickHandler
    ): RecallItemClickHandler = debouncingRecallClickHandler
}
