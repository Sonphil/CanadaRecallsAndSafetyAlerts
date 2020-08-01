package com.sonphil.canadarecallsandsafetyalerts.domain.notification

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.notification_keyword.GetNotificationKeywordsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class CheckIfShouldNotifyAboutRecallUseCase @Inject constructor(
    private val getNotificationKeywordsUseCase: GetNotificationKeywordsUseCase
) {
    suspend operator fun invoke(recall: Recall, keywordNotificationsEnabled: Boolean): Boolean {
        val keywords = if (keywordNotificationsEnabled) {
            runCatching { getNotificationKeywordsUseCase().first() }.getOrNull().orEmpty()
        } else {
            emptyList()
        }

        if (keywords.isEmpty()) {
            return true
        }

        val firstMatch = recall
            .title
            ?.findAnyOf(keywords, ignoreCase = true)

        return firstMatch != null
    }
}
