package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.work.*
import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import com.sonphil.canadarecallsandsafetyalerts.repository.mapper.toRecalls
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
    private val api: CanadaGovernmentApi,
    private val recallDao: RecallDao
) : CoroutineWorker(appContext, workerParameters) {

    companion object {
        private const val UNIQUE_WORK_NAME = "SyncRecallsWork"

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

            workManager.cancelAllWork()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val syncRequest =
                PeriodicWorkRequestBuilder<SyncRecallsWorker>(repeatInterval, timeUnit)
                    .setConstraints(constraints)
                    .build()

            workManager.enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                syncRequest
            )
        }
    }

    override suspend fun doWork(): Result {
        try {
            val recalls = api.recentRecalls("fr")
                .results
                .all
                ?.toRecalls()

            if (!recalls.isNullOrEmpty()) {
                NotificationsUtils.notifyRecall(appContext, recalls[0])
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    class Factory @Inject constructor(
        private val api: Provider<CanadaGovernmentApi>,
        private val recallDao: Provider<RecallDao>
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SyncRecallsWorker(appContext, params, api.get(), recallDao.get())
        }
    }
}