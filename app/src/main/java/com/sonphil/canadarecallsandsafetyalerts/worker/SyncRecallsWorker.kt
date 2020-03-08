package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.work.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.repository.NotificationKeywordsRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.LocaleUtils
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import kotlinx.coroutines.CoroutineScope
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
    private val recallRepository: RecallRepository,
    private val notificationKeywordsRepository: NotificationKeywordsRepository,
    private val localeUtils: LocaleUtils,
    private val notificationsUtils: NotificationsUtils
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        try {
            val lang = localeUtils.getCurrentLanguage()
            val newRecalls = withContext(Dispatchers.IO) {
                recallRepository.getNewRecalls(lang)
            }
            val keywordNotificationsEnabled = inputData.getBoolean(
                SyncRecallsWorkerScheduler.KEY_KEYWORD_NOTIFICATIONS_ENABLED,
                false
            )

            newRecalls.notify(this, keywordNotificationsEnabled)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
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

    private suspend fun List<Recall>.notify(
        coroutineScope: CoroutineScope,
        keywordNotificationsEnabled: Boolean
    ) {
        if (this.isNotEmpty()) {
            val keywords = if (keywordNotificationsEnabled) {
                notificationKeywordsRepository.getKeywords()
            } else {
                emptyList()
            }

            run loop@{
                this.forEach { recall ->
                    if (coroutineScope.isActive) {
                        if (shouldNotifyAboutRecall(recall, keywordNotificationsEnabled, keywords)) {
                            notificationsUtils.notifyRecall(recall)
                        }
                    } else {
                        return@loop
                    }
                }
            }
        }
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