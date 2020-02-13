package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.work.*
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Sonphil on 12-02-20.
 */

class SyncRecallsWorker @Inject constructor(
    private val appContext: Context,
    workerParameters: WorkerParameters,
    private val recallRepository: RecallRepository,
    private val localeUtils: LocaleUtils,
    private val notificationsUtils: NotificationsUtils
) : CoroutineWorker(appContext, workerParameters) {

    companion object {
        private const val UNIQUE_WORK_NAME = "SyncRecallsWork"
        private const val INITIAL_DELAY_IN_MINUTES = 5L

        /**
         * Schedules a worker that synchronize recalls with the API and notify the user about new
         * ones according to his settings
         *
         * @param context
         * @param repeatInterval The repeat interval
         * @param timeUnit The [TimeUnit] for the [repeatInterval]
         */
        fun schedule(
            context: Context,
            repeatInterval: Long,
            timeUnit: TimeUnit = TimeUnit.MINUTES
        ) {
            val workManager = WorkManager.getInstance(context)

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val syncRequest =
                PeriodicWorkRequestBuilder<SyncRecallsWorker>(repeatInterval, timeUnit)
                    .setConstraints(constraints)
                    .setInitialDelay(INITIAL_DELAY_IN_MINUTES, timeUnit)
                    .build()

            workManager.enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                syncRequest
            )
        }

        /**
         * Cancels scheduled worker
         *
         * @param context
         */
        fun cancel(context: Context) = WorkManager.getInstance(context).cancelAllWork()
    }

    override suspend fun doWork(): Result {
        try {
            val lang = localeUtils.getCurrentLanguage()
            val newRecalls = recallRepository.getNewRecalls(lang)

            if (newRecalls.isNotEmpty()) {
                newRecalls.forEach { recall ->
                    notificationsUtils.notifyRecall(recall)
                }
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    class Factory @Inject constructor(
        private val recallRepository: Provider<RecallRepository>,
        private val localeUtils: Provider<LocaleUtils>,
        private val notificationsUtils: Provider<NotificationsUtils>
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SyncRecallsWorker(
                appContext,
                params,
                recallRepository.get(),
                localeUtils.get(),
                notificationsUtils.get()
            )
        }
    }
}