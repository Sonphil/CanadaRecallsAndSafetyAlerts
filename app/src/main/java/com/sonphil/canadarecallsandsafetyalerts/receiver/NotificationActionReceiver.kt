package com.sonphil.canadarecallsandsafetyalerts.receiver

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.ext.toast
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivityArgs
import com.sonphil.canadarecallsandsafetyalerts.repository.BookmarkRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.ReadStatusRepository
import com.sonphil.canadarecallsandsafetyalerts.repository.RecallRepository
import com.sonphil.canadarecallsandsafetyalerts.utils.NotificationsUtils
import dagger.android.DaggerBroadcastReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 26-02-20.
 */

class NotificationActionReceiver : DaggerBroadcastReceiver() {
    @Inject
    lateinit var notificationUtils: NotificationsUtils
    @Inject
    lateinit var recallRepository: RecallRepository
    @Inject
    lateinit var bookmarkRepository: BookmarkRepository
    @Inject
    lateinit var readStatusRepository: ReadStatusRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val action = intent.action

        val recall = intent.extras?.run {
            RecallDetailsActivityArgs.fromBundle(this).recall
        }

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