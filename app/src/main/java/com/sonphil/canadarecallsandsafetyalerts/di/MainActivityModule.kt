package com.sonphil.canadarecallsandsafetyalerts.di

import androidx.lifecycle.ViewModel
import com.sonphil.canadarecallsandsafetyalerts.presentation.more.MoreFragment
import com.sonphil.canadarecallsandsafetyalerts.presentation.notification.AddNotificationKeywordDialogFragment
import com.sonphil.canadarecallsandsafetyalerts.presentation.notification.AddNotificationKeywordViewModel
import com.sonphil.canadarecallsandsafetyalerts.presentation.notification.NotificationKeywordsFragment
import com.sonphil.canadarecallsandsafetyalerts.presentation.notification.NotificationKeywordsViewModel
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls.MyRecallsFragment
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.my_recalls.MyRecallsViewModel
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent.RecentFragment
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent.RecentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecentViewModel::class)
    fun bindRecentViewModel(recentViewModel: RecentViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeRecentFragment(): RecentFragment

    @Binds
    @IntoMap
    @ViewModelKey(MyRecallsViewModel::class)
    fun bindMyRecallsViewModel(myRecallsViewModel: MyRecallsViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeMyRecallsFragment(): MyRecallsFragment

    @ContributesAndroidInjector
    fun contributeMoreFragment(): MoreFragment

    @Binds
    @IntoMap
    @ViewModelKey(NotificationKeywordsViewModel::class)
    fun bindNotificationKeywordsViewModel(notificationKeywordsViewModel: NotificationKeywordsViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeNotificationKeywordsFragment(): NotificationKeywordsFragment

    @ContributesAndroidInjector
    fun contributeAddNotificationKeywordDialogFragment(): AddNotificationKeywordDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(AddNotificationKeywordViewModel::class)
    fun bindAddNotificationKeywordViewModel(addNotificationKeywordViewModel: AddNotificationKeywordViewModel): ViewModel
}