package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import javax.inject.Inject

/**
 * Created by Sonphil on 10-03-20.
 */

class GetRecallsToNotifyUseCase @Inject constructor(
    private val getNewRecallsUseCase: GetNewRecallsUseCase,
    private val checkIfShouldNotifyAboutRecallUseCase: CheckIfShouldNotifyAboutRecallUseCase
) {
    suspend operator fun invoke(isKeywordNotificationsEnabled: Boolean): List<Recall> {
        val newRecalls = getNewRecallsUseCase()

        return if (newRecalls.isEmpty()) {
            emptyList()
        } else {
            newRecalls.filter { recall ->
                runCatching {
                    checkIfShouldNotifyAboutRecallUseCase(recall, isKeywordNotificationsEnabled)
                }.getOrDefault(false)
            }
        }
    }
}
