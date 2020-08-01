package com.sonphil.canadarecallsandsafetyalerts.domain.notification

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class GetRecallsToNotifyUseCase @Inject constructor(
    private val getNewRecallsUseCase: GetNewRecallsUseCase,
    private val checkIfShouldNotifyAboutRecallUseCase: CheckIfShouldNotifyAboutRecallUseCase
) {
    suspend operator fun invoke(keywordNotificationsEnabled: Boolean): List<Recall> {
        val newRecalls = getNewRecallsUseCase()

        return if (newRecalls.isEmpty()) {
            emptyList()
        } else {
            newRecalls.filter { recall ->
                checkIfShouldNotifyAboutRecallUseCase(recall, keywordNotificationsEnabled)
            }
        }
    }
}
