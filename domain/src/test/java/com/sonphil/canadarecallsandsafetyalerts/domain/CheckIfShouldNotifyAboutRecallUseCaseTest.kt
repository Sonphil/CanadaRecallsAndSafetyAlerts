package com.sonphil.canadarecallsandsafetyalerts.domain

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification.CheckIfShouldNotifyAboutRecallUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification_keyword.GetNotificationKeywordsUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Created by Sonphil on 24-10-20.
 */

@ExperimentalCoroutinesApi
class CheckIfShouldNotifyAboutRecallUseCaseTest {
    private val getNotificationKeywordsUseCase: GetNotificationKeywordsUseCase = mockk()
    private val checkIfShouldNotifyAboutRecallUseCase = CheckIfShouldNotifyAboutRecallUseCase(getNotificationKeywordsUseCase)
    private val newRecall = Recall(
        category = Category.FOOD,
        datePublished = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5),
        id = "12345",
        title = "Dark Chocolate Enrobed fruit products recalled due to undeclared milk",
        apiUrl = "foo"
    )

    @Test
    fun keywordNotificationsIsDisabled_Check_ReturnsTrue() = runBlockingTest {
        // Given
        val isKeywordNotificationsEnabled = false

        // When
        val shouldNotify = checkIfShouldNotifyAboutRecallUseCase(
            recall = newRecall,
            isKeywordNotificationsEnabled
        )

        // Then
        assertTrue(shouldNotify)
    }

    @Test
    fun keywordNotificationsIsEnabledAndTitleContainsAKeywordWithMatchingCase_Check_ReturnsTrue() = runBlockingTest {
        // Given
        coEvery {
            getNotificationKeywordsUseCase.invoke()
        } returns flowOf(listOf("peanut", "Chocolate"))
        val isKeywordNotificationsEnabled = true

        // When
        val shouldNotify = checkIfShouldNotifyAboutRecallUseCase(
            recall = newRecall,
            isKeywordNotificationsEnabled
        )

        // Then
        assertTrue(shouldNotify)
    }

    @Test
    fun keywordNotificationsIsEnabledAndTitleContainsAKeywordNotMatchingCase_Check_ReturnsTrue() = runBlockingTest {
        // Given
        coEvery {
            getNotificationKeywordsUseCase.invoke()
        } returns flowOf(listOf("peanut", "chOcoLate"))
        val isKeywordNotificationsEnabled = true

        // When
        val shouldNotify = checkIfShouldNotifyAboutRecallUseCase(
            recall = newRecall,
            isKeywordNotificationsEnabled
        )

        // Then
        assertTrue(shouldNotify)
    }

    @Test
    fun keywordNotificationsIsEnabledAndTitleDoesNotContainAKeyword_Check_ReturnsFalse() = runBlockingTest {
        // Given
        coEvery { getNotificationKeywordsUseCase.invoke() } returns flowOf(listOf("foo", "Bar"))
        val isKeywordNotificationsEnabled = true

        // When
        val shouldNotify = checkIfShouldNotifyAboutRecallUseCase(
            recall = newRecall,
            isKeywordNotificationsEnabled
        )

        // Then
        assertFalse(shouldNotify)
    }

    @Test
    fun emptyKeywordLisAndKeywordNotificationsIsEnabled_Check_ReturnsTrue() = runBlockingTest {
        // Given
        coEvery { getNotificationKeywordsUseCase.invoke() } returns flowOf(emptyList())
        val isKeywordNotificationsEnabled = true

        // When
        val shouldNotify = checkIfShouldNotifyAboutRecallUseCase(
            recall = newRecall,
            isKeywordNotificationsEnabled
        )

        // Then
        assertTrue(shouldNotify)
    }

    @After
    fun cleanUp() {
        clearAllMocks()
    }
}
