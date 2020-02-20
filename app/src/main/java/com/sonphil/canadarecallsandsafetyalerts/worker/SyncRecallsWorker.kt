package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.work.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.repository.NotificationKeywordsRepository
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
    appContext: Context,
    workerParameters: WorkerParameters,
    private val recallRepository: RecallRepository,
    private val notificationKeywordsRepository: NotificationKeywordsRepository,
    private val localeUtils: LocaleUtils,
    private val notificationsUtils: NotificationsUtils
) : CoroutineWorker(appContext, workerParameters) {

    companion object {
        private const val UNIQUE_WORK_NAME = "SyncRecallsWork"
        private const val INITIAL_DELAY_IN_MINUTES = 5L
        private const val KEY_KEYWORD_NOTIFICATIONS_ENABLED = "KeywordNotificationsEnabled"

        /**
         * Schedules a worker that synchronize recalls with the API and notify the user about new
         * ones according to his settings
         *
         * @param context
         * @param keywordNotificationsEnabled Whether or not the worker should notify the user about
         * a recall or an alert only if it contains at least a keyword
         * @param repeatInterval The repeat interval
         * @param timeUnit The [TimeUnit] for the [repeatInterval]
         */
        fun schedule(
            context: Context,
            keywordNotificationsEnabled: Boolean,
            repeatInterval: Long,
            timeUnit: TimeUnit = TimeUnit.MINUTES
        ) {
            val workManager = WorkManager.getInstance(context)

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val inputData = Data.Builder()
                .putBoolean(
                    KEY_KEYWORD_NOTIFICATIONS_ENABLED,
                    keywordNotificationsEnabled
                )
                .build()

            val syncRequest =
                PeriodicWorkRequestBuilder<SyncRecallsWorker>(repeatInterval, timeUnit)
                    .setConstraints(constraints)
                    .setInitialDelay(INITIAL_DELAY_IN_MINUTES, timeUnit)
                    .setInputData(inputData)
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
            val keywordNotificationsEnabled = inputData.getBoolean(
                KEY_KEYWORD_NOTIFICATIONS_ENABLED,
                false
            )

            if (newRecalls.isNotEmpty()) {
                val keywords = if (keywordNotificationsEnabled) {
                    notificationKeywordsRepository.getKeywords()
                } else {
                    emptyList()
                }

                newRecalls.forEach { recall ->
                    if (shouldNotifyAboutRecall(recall, keywordNotificationsEnabled, keywords)) {
                        notificationsUtils.notifyRecall(recall)
                    }
                }
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun shouldNotifyAboutRecall(
        recall: Recall,
        keywordNotificationsEnabled: Boolean,
        keywords: List<String>
    ): Boolean {
        if (!keywordNotificationsEnabled || keywords.isEmpty()) {
            return true
        }

        val firstMatch = recall
            .title
            ?.findAnyOf(keywords, ignoreCase = true)

        return firstMatch != null
    }

    class Factory @Inject constructor(
        private val recallRepository: Provider<RecallRepository>,
        private val notificationKeywordsRepository: Provider<NotificationKeywordsRepository>,
        private val localeUtils: Provider<LocaleUtils>,
        private val notificationsUtils: Provider<NotificationsUtils>
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SyncRecallsWorker(
                appContext,
                params,
                recallRepository.get(),
                notificationKeywordsRepository.get(),
                localeUtils.get(),
                notificationsUtils.get()
            )
        }
    }
}