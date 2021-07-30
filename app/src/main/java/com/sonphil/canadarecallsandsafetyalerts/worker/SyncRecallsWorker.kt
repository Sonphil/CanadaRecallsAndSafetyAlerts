package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification.GetRecallsToNotifyUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

/**
 * Created by Sonphil on 12-02-20.
 */

@HiltWorker
class SyncRecallsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val getRecallsToNotifyUseCase: GetRecallsToNotifyUseCase,
    private val notificationsUtils: NotificationsUtils,
    private val appDispatchers: AppDispatchers
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = withContext(appDispatchers.default) {
        try {
            val isKeywordNotificationsEnabled = inputData.getBoolean(
                SyncRecallsWorkerScheduler.KEY_KEYWORD_NOTIFICATIONS_ENABLED,
                false
            )

            run loop@{
                getRecallsToNotifyUseCase(isKeywordNotificationsEnabled).forEach { recall ->
                    if (isActive) {
                        notificationsUtils.notifyRecall(recall)
                    } else {
                        return@loop
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            val data = Data.Builder()
                .putString("Exception", e.toString())
                .build()
            Result.failure(data)
        }
    }
}
