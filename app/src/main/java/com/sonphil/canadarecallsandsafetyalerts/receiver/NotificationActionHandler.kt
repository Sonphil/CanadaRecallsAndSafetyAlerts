package com.sonphil.canadarecallsandsafetyalerts.receiver

import android.content.Context
import androidx.annotation.StringRes
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.ext.toast
import com.sonphil.canadarecallsandsafetyalerts.data.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.data.repository.ReadStatusRepository
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
    private val bookmarkRepository: BookmarkRepository,
    private val readStatusRepository: ReadStatusRepository
) {
    fun handleRecallAction(context: Context, recall: Recall?, action: String?) {
        if (recall != null) {
            notificationUtils.dismissNotification(context, recall)

            GlobalScope.launch(Dispatchers.IO) {
                if (action == context.getString(R.string.action_mark_recall_as_read)) {
                    readStatusRepository.markRecallAsRead(recall)

                    showSuccessMessage(context, R.string.message_recall_marked_as_read)
                } else if (action == context.getString(R.string.action_bookmark_recall)) {
                    bookmarkRepository.updateBookmark(recall, true)

                    showSuccessMessage(context, R.string.message_recall_bookmarked)
                }
            }
        }
    }

    private suspend fun showSuccessMessage(context: Context, @StringRes messageId: Int) {
        withContext(Dispatchers.Main) {
            context.toast(messageId)
        }
    }
}