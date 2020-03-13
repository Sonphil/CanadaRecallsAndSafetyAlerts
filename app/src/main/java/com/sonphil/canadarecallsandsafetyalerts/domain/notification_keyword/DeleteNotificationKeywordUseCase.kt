package com.sonphil.canadarecallsandsafetyalerts.domain.notification_keyword

import com.sonphil.canadarecallsandsafetyalerts.data.repository.NotificationKeywordsRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class DeleteNotificationKeywordUseCase @Inject constructor(
    private val notificationKeywordsRepository: NotificationKeywordsRepository
) {
    suspend operator fun invoke(keyword: String) = notificationKeywordsRepository
        .deleteKeyword(keyword)
}