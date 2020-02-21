package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.crashlytics.android.Crashlytics
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Sonphil on 12-02-20.
 */

class AppWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        try {
            val foundEntry = workerFactories
                .entries
                .find {
                    Class.forName(workerClassName).isAssignableFrom(it.key)
                }
            val factoryProvider = foundEntry?.value

            if (factoryProvider == null) {
                val msg = "Unknown worker class name: $workerClassName"
                Crashlytics.logException(IllegalArgumentException(msg))
            }
            return factoryProvider?.get()?.create(appContext, workerParameters)
        } catch (e: Exception) {
            Crashlytics.logException(e)

            return null
        }
    }
}