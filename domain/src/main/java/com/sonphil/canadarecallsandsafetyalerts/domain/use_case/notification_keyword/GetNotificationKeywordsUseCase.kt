package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification_keyword

import com.sonphil.canadarecallsandsafetyalerts.domain.repository.NotificationKeywordsRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class GetNotificationKeywordsUseCase @Inject constructor(
    private val notificationKeywordsRepository: NotificationKeywordsRepositoryInterface
) {
    operator fun invoke() = notificationKeywordsRepository.getKeywords()
}
