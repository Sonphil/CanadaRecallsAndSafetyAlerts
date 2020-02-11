package com.sonphil.canadarecallsandsafetyalerts.di

import com.sonphil.canadarecallsandsafetyalerts.presentation.MainActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Sonphil on 10-02-20.
 */

@Module
interface ActivityBindingModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun contributeRecallDetailsActivity(): RecallDetailsActivity
}