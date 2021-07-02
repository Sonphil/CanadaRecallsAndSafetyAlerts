package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging.RecordNonFatalExceptionUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Sonphil on 04-03-20.
 */

@Singleton
class SyncRecallsWorkerScheduler @Inject constructor(
    private val context: Context,
    private val prefs: SharedPreferences,
    private val recordNonFatalExceptionUseCase: RecordNonFatalExceptionUseCase
) {
    companion object {
        private const val UNIQUE_WORK_NAME = "SyncRecallsWork"
        private const val INITIAL_DELAY_IN_MINUTES = 5L
        const val KEY_KEYWORD_NOTIFICATIONS_ENABLED = "KeywordNotificationsEnabled"
    }

    /**
     * Load user's preferences and schedule a worker according to the user's preferences
     */
    fun scheduleAccordingToPreferences() {
        val notificationsPrefKey = context.getString(R.string.key_notifications_pref)
        val notificationsPrefValue = prefs.getString(notificationsPrefKey, "")

        if (notificationsPrefValue == context.getString(R.string.value_notifications_pref_no)) {
            cancel(context)
        } else {
            val repeatIntervalPrefKey =
                context.getString(R.string.key_notifications_sync_frequency_in_minutes_pref)
            try {
                val repeatInterval = prefs
                    .getString(repeatIntervalPrefKey, null)
                    ?.toLong()
                val isKeywordNotificationsEnabled =
                    notificationsPrefValue == context.getString(R.string.value_notifications_pref_keyword)

                if (repeatInterval != null) {
                    schedule(
                        context.applicationContext,
                        isKeywordNotificationsEnabled,
                        repeatInterval
                    )
                }
            } catch (e: NumberFormatException) {
                recordNonFatalExceptionUseCase(e)
            }
        }
    }

    /**
     * Schedules a worker that synchronize recalls with the API and notify the user about new
     * ones
     *
     * @param context
     * @param keywordNotificationsEnabled Whether or not the worker should notify the user about
     * a recall or an alert only if it contains at least a keyword
     * @param repeatInterval The repeat interval
     */
    private fun schedule(
        context: Context,
        keywordNotificationsEnabled: Boolean,
        repeatInterval: Long
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
            PeriodicWorkRequestBuilder<SyncRecallsWorker>(repeatInterval, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(INITIAL_DELAY_IN_MINUTES, TimeUnit.MINUTES)
                .setInputData(inputData)
                .build()

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            syncRequest
        )
    }

    /**
     * Cancels all scheduled workers
     *
     * @param context
     */
    private fun cancel(context: Context) = WorkManager.getInstance(context).cancelAllWork()
}
