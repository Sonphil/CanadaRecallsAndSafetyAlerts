package com.sonphil.canadarecallsandsafetyalerts.presentation

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var notificationsUtils: NotificationsUtils

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        notificationsUtils.createNotificationChannels()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }
}
