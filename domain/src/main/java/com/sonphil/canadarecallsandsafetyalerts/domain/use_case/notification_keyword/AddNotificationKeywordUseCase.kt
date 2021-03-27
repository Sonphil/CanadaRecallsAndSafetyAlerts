package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification_keyword

import com.sonphil.canadarecallsandsafetyalerts.domain.repository.NotificationKeywordsRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class AddNotificationKeywordUseCase @Inject constructor(
    private val notificationKeywordsRepository: NotificationKeywordsRepositoryInterface
) {
    suspend operator fun invoke(keyword: String) = notificationKeywordsRepository
        .insertNewKeyword(keyword)
}
