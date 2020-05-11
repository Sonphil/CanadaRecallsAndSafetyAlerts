package com.sonphil.canadarecallsandsafetyalerts.receiver

import android.content.Context
import androidx.annotation.StringRes
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.CheckIfRecallIsBookmarkedUseCase
import com.sonphil.canadarecallsandsafetyalerts.ext.toast
import com.sonphil.canadarecallsandsafetyalerts.domain.bookmark.UpdateBookmarkUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.read_status.CheckIfRecallHasBeenReadUseCase
import com.sonphil.canadarecallsandsafetyalerts.domain.read_status.MarkRecallAsReadUseCase
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Sonphil on 08-03-20.
 */

@Singleton
class NotificationActionHandler @Inject constructor(
    private val notificationUtils: NotificationsUtils,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    private val markRecallAsReadUseCase: MarkRecallAsReadUseCase,
    private val checkIfRecallHasBeenReadUseCase: CheckIfRecallHasBeenReadUseCase,
    private val checkIfRecallIsBookmarkedUseCase: CheckIfRecallIsBookmarkedUseCase
) {
    fun handleRecallAction(context: Context, recall: Recall?, action: String?) {
        if (recall != null && action != null) {
            GlobalScope.launch(Dispatchers.IO) {
                action.handleRecallAction(context, recall)
            }
        }
    }

    private suspend fun String.handleRecallAction(context: Context, recall: Recall) {
        if (this == context.getString(R.string.action_mark_recall_as_read)) {
            markRecallAsReadUseCase(recall)
            notificationUtils.notifyRecall(
                recall,
                isMarkAsReadActionEnabled = false,
                isBookmarkActionEnabled = !checkIfRecallIsBookmarkedUseCase(recall)
            )
            showSuccessMessage(context, R.string.message_recall_marked_as_read)
        } else if (this == context.getString(R.string.action_bookmark_recall)) {
            updateBookmarkUseCase(recall, true)
            notificationUtils.notifyRecall(
                recall,
                isMarkAsReadActionEnabled = !checkIfRecallHasBeenReadUseCase(recall),
                isBookmarkActionEnabled = false
            )
            showSuccessMessage(context, R.string.message_recall_bookmarked)
        }
    }

    private suspend fun showSuccessMessage(context: Context, @StringRes messageId: Int) {
        withContext(Dispatchers.Main) {
            context.toast(messageId)
        }
    }
}