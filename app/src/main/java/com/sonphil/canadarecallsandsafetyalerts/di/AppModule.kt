package com.sonphil.canadarecallsandsafetyalerts.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Sonphil on 28-02-18.
 */
@Module
internal object AppModule {
    @Singleton
    @Provides
    @JvmStatic
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    @JvmStatic
    fun provideApp(application: Application): App = application as App

    @Singleton
    @Provides
    @JvmStatic
    fun provideSharedPreferences(context: Context): SharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)
}
