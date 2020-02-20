package com.sonphil.canadarecallsandsafetyalerts.presentation

import androidx.work.Configuration
import androidx.work.WorkManager
import com.sonphil.canadarecallsandsafetyalerts.di.DaggerAppComponent
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import com.sonphil.canadarecallsandsafetyalerts.worker.AppWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class App : DaggerApplication() {

    @Inject
    lateinit var notificationsUtils: NotificationsUtils

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        initWorkerManager(appComponent.workerFactory())

        return appComponent
    }

    private fun initWorkerManager(workerFactory: AppWorkerFactory) {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

        WorkManager.initialize(this, config)
    }

    override fun onCreate() {
        super.onCreate()

        notificationsUtils.initNotificationChannels()
    }
}