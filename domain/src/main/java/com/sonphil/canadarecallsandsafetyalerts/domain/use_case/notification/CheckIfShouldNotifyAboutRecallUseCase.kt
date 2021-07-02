package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging.RecordNonFatalExceptionUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification_keyword.GetNotificationKeywordsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details.GetRecallsDetailsSectionsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class CheckIfShouldNotifyAboutRecallUseCase @Inject constructor(
    private val getNotificationKeywordsUseCase: GetNotificationKeywordsUseCase,
    private val getRecallsDetailsSectionsUseCase: GetRecallsDetailsSectionsUseCase,
    private val recordNonFatalExceptionUseCase: RecordNonFatalExceptionUseCase
) {
    suspend operator fun invoke(recall: Recall, isKeywordNotificationsEnabled: Boolean): Boolean {
        if (!isKeywordNotificationsEnabled) {
            return true
        }

        val keywords = runCatching {
            getNotificationKeywordsUseCase().first()
        }.getOrNull().orEmpty()

        return when {
            keywords.isEmpty() -> {
                false
            }
            isAnyKeywordInRecallTitle(recall, keywords) -> {
                true
            }
            else -> {
                isAnyKeywordInRecallDetails(recall, keywords)
            }
        }
    }

    private fun isAnyKeywordInRecallTitle(recall: Recall, keywords: List<String>): Boolean {
        return isAnyKeywordInText(recall.title, keywords)
    }

    private suspend fun isAnyKeywordInRecallDetails(
        recall: Recall,
        keywords: List<String>
    ): Boolean {
        val result = getRecallsDetailsSectionsUseCase(recall).first()

        if (result is LoadResult.Error) {
            recordNonFatalExceptionUseCase(result.throwable)
            throw result.throwable
        } else {
            return result.data?.detailsSections.orEmpty().any {
                it.text.findAnyOf(keywords, ignoreCase = true) != null
            }
        }
    }

    private fun isAnyKeywordInText(text: String?, keywords: List<String>): Boolean {
        return text?.findAnyOf(keywords, ignoreCase = true) != null
    }
}
