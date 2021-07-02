package com.sonphil.canadarecallsandsafetyalerts.domain

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBasicInformationAndDetailsSectionsAndImages
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallDetailsSection
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallDetailsSectionType
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging.RecordNonFatalExceptionUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification.CheckIfShouldNotifyAboutRecallUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.notification_keyword.GetNotificationKeywordsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.use_case.recall_details.GetRecallsDetailsSectionsUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Created by Sonphil on 24-10-20.
 */

@ExperimentalCoroutinesApi
class CheckIfShouldNotifyAboutRecallUseCaseTest {
    private val getNotificationKeywordsUseCase: GetNotificationKeywordsUseCase = mockk()
    private val getRecallDetailsUseCase: GetRecallsDetailsSectionsUseCase = mockk()
    private val recordNonFatalExceptionUseCase: RecordNonFatalExceptionUseCase =
        mockk(relaxUnitFun = true)
    private val checkIfShouldNotifyAboutRecallUseCase = CheckIfShouldNotifyAboutRecallUseCase(
        getNotificationKeywordsUseCase,
        getRecallDetailsUseCase,
        recordNonFatalExceptionUseCase
    )
    private val newRecall = Recall(
        category = Category.FOOD,
        datePublished = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5),
        id = "12345",
        title = "Dark Chocolate Enrobed fruit products recalled due to undeclared milk",
        apiUrl = "foo"
    )

    @Test
    fun givenKeywordNotificationsAreDisabled_WhenCheck_ThenConsiderShouldNotify() =
        runBlockingTest {
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
    fun giveKeywordNotificationsAreEnabledAndTitleContainsAKeywordWithMatchingCase_WhenCheck_ThenConsiderShouldNotify() =
        runBlockingTest {
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
    fun givenKeywordNotificationsAreEnabledAndTitleContainsAKeywordThatDoesNotMatchingCase_WhenCheck_ThenConsiderShouldNotify() =
        runBlockingTest {
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
    fun givenUserDoesNotHaveAnyKeywordAndKeywordNotificationsAreEnabled_WhenCheck_ThenConsiderShouldNotNotify() =
        runBlockingTest {
            // Given
            coEvery { getNotificationKeywordsUseCase.invoke() } returns flowOf(emptyList())
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
    fun givenKeywordNotificationsAreEnabledAndTitleDoesNotContainAKeywordAndSectionsTextsContainsAKeyword_WhenCheck_ThenConsiderShouldNotify() =
        runBlockingTest {
            // Given
            val result =
                LoadResult.Success(createRecallAndBasicInformationAndDetailsSectionsAndImages())
            coEvery {
                getRecallDetailsUseCase.invoke(newRecall)
            } returns flowOf(result)
            coEvery { getNotificationKeywordsUseCase.invoke() } returns flowOf(listOf("mILK"))
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
    fun givenKeywordNotificationsAreEnabledAndTitleDoesNotContainAKeywordAndSectionsTextsDoNotContainsAKeyword_WhenCheck_ThenConsiderShouldNotNotify() =
        runBlockingTest {
            // Given
            val result =
                LoadResult.Success(createRecallAndBasicInformationAndDetailsSectionsAndImages())
            coEvery {
                getRecallDetailsUseCase.invoke(newRecall)
            } returns flowOf(result)
            coEvery { getNotificationKeywordsUseCase.invoke() } returns flowOf(listOf("Peanut"))
            val isKeywordNotificationsEnabled = true

            // When
            val shouldNotify = checkIfShouldNotifyAboutRecallUseCase(
                recall = newRecall,
                isKeywordNotificationsEnabled
            )

            // Then
            assertFalse(shouldNotify)
        }

    @Test(expected = Throwable::class)
    fun givenKeywordNotificationsAreEnabledAndTitleDoesNotContainAKeywordButCanNotFetchSections_WhenCheck_ThenThrowException() =
        runBlockingTest {
            // Given
            val throwable = Throwable()
            val result = LoadResult.Error<RecallAndBasicInformationAndDetailsSectionsAndImages>(
                null,
                throwable
            )
            coEvery {
                getRecallDetailsUseCase.invoke(newRecall)
            } returns flowOf(result)
            coEvery { getNotificationKeywordsUseCase.invoke() } returns flowOf(listOf("foo"))
            val isKeywordNotificationsEnabled = true

            // When
            checkIfShouldNotifyAboutRecallUseCase(
                recall = newRecall,
                isKeywordNotificationsEnabled
            )
        }

    private fun createRecallAndBasicInformationAndDetailsSectionsAndImages() =
        RecallAndBasicInformationAndDetailsSectionsAndImages(
            recall = newRecall,
            basicInformation = null,
            detailsSections = listOf(createRecallDetailsSection()),
            images = listOf()
        )

    private fun createRecallDetailsSection(): RecallDetailsSection {
        return RecallDetailsSection(
            recallId = newRecall.id,
            panelName = "test",
            type = RecallDetailsSectionType.BASIC_DETAILS,
            title = "Test",
            text = "Check to see if you have the recalled product in your home."
        )
    }

    @AfterTest
    fun cleanUp() {
        clearAllMocks()
    }
}
