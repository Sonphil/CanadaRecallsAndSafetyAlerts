package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification.GetRecallsToNotifyUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Sonphil on 12-02-20.
 */

class SyncRecallsWorker @Inject constructor(
    appContext: Context,
    workerParameters: WorkerParameters,
    private val getRecallsToNotifyUseCase: GetRecallsToNotifyUseCase,
    private val notificationsUtils: NotificationsUtils
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        try {
            val keywordNotificationsEnabled = inputData.getBoolean(
                SyncRecallsWorkerScheduler.KEY_KEYWORD_NOTIFICATIONS_ENABLED,
                false
            )

            run loop@{
                getRecallsToNotifyUseCase(keywordNotificationsEnabled).forEach { recall ->
                    if (isActive) {
                        notificationsUtils.notifyRecall(recall)
                    } else {
                        return@loop
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    class Factory @Inject constructor(
        private val recallsToNotifyUseCase: Provider<GetRecallsToNotifyUseCase>,
        private val notificationsUtils: Provider<NotificationsUtils>
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SyncRecallsWorker(
                appContext,
                params,
                recallsToNotifyUseCase.get(),
                notificationsUtils.get()
            )
        }
    }
}
