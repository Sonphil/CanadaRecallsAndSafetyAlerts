package com.sonphil.canadarecallsandsafetyalerts.domain.notification_keyword

import com.sonphil.canadarecallsandsafetyalerts.data.repository.NotificationKeywordsRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class GetNotificationKeywordsUseCase @Inject constructor(
    private val notificationKeywordsRepository: NotificationKeywordsRepository
) {
    operator fun invoke() = notificationKeywordsRepository.getKeywords()
}