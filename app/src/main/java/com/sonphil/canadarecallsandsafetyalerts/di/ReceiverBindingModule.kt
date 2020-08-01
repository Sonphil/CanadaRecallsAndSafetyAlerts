package com.sonphil.canadarecallsandsafetyalerts.di

import com.sonphil.canadarecallsandsafetyalerts.receiver.NotificationActionReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Sonphil on 26-02-20.
 */

@Module
interface ReceiverBindingModule {

    @ContributesAndroidInjector
    fun provideNotificationActionReceiver(): NotificationActionReceiver
}
