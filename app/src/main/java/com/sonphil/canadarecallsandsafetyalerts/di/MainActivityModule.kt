package com.sonphil.canadarecallsandsafetyalerts.di

import androidx.lifecycle.ViewModel
import com.sonphil.canadarecallsandsafetyalerts.presentation.recent.RecentFragment
import com.sonphil.canadarecallsandsafetyalerts.presentation.recent.RecentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by Sonphil on 15-03-18.
 */
@Module
interface MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecentViewModel::class)
    fun bindRecentViewModel(recentViewModel: RecentViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeRecentFragment(): RecentFragment
}