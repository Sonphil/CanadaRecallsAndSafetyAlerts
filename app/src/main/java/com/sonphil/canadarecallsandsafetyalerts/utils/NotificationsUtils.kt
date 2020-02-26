package com.sonphil.canadarecallsandsafetyalerts.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.ext.getColorCompat
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivityArgs
import com.sonphil.canadarecallsandsafetyalerts.receiver.NotificationActionReceiver
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Sonphil on 11-02-20.
 */

@Singleton
class NotificationsUtils @Inject constructor(private val context: Context) {
    /**
     * Returns a new [NotificationChannel]
     *
     * @param channelId The id of the channel
     * @param name The name of the channel
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelForCategory(
        channelId: String,
        name: String
    ): NotificationChannel {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return channel
    }

    /**
     * Returns a new [PendingIntent] that allows the user to navigate to the details screen of a
     * [Recall]
     *
     * @param recall
     * @param requestCode The request code for the [PendingIntent]
     */
    private fun createDetailsPendingIntentForRecall(
        context: Context,
        recall: Recall,
        requestCode: Int
    ): PendingIntent {
        val args = RecallDetailsActivityArgs(recall).toBundle()
        val intent = Intent(context, RecallDetailsActivity::class.java).apply {
            putExtras(args)
        }

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun createAction(
        context: Context,
        recall: Recall,
        requestCode: Int,
        @StringRes actionId: Int,
        @StringRes labelId: Int
    ): NotificationCompat.Action {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = context.getString(actionId)

            val args = RecallDetailsActivityArgs(recall).toBundle()

            putExtras(args)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action = NotificationCompat.Action(null, context.getString(labelId), pendingIntent)

        return NotificationCompat.Action.Builder(action).build()
    }

    private fun Recall.getNotificationId() = try {
        id.toInt()
    } catch (e: NumberFormatException) {
        0
    }

    /**
     * Creates a notification for a given [Recall
     *
     * The [Recall]'s category for the notification's channel on Android 8 and higher.
     *
     * @param recall
     */
    fun notifyRecall(recall: Recall) {
        val channelId = recall.category.name
        val categoryResources = CategoryResources(recall.category)
        val notificationId = recall.getNotificationId()
        val tapPendingIntent = createDetailsPendingIntentForRecall(
            context,
            recall,
            notificationId
        )
        val categoryTitle = context.getString(categoryResources.labelId)
        val markAsReadAction = createAction(
            context,
            recall,
            notificationId,
            R.string.action_mark_recall_as_read,
            R.string.label_action_mark_recall_as_read
        )
        val bookmarkAction = createAction(
            context,
            recall,
            notificationId,
            R.string.action_bookmark_recall,
            R.string.label_action_bookmark_recall
        )
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(categoryTitle)
            .setContentText(recall.title)
            .setAutoCancel(true)
            .setVisibility(VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.BigTextStyle().bigText(recall.title))
            .setContentIntent(tapPendingIntent)
            .setColor(context.getColorCompat(R.color.colorPrimary))
            .addAction(markAsReadAction)
            .addAction(bookmarkAction)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = createNotificationChannelForCategory(
                recall.category.name,
                categoryTitle
            )

            notificationBuilder.setChannelId(notificationChannel.id)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notificationBuilder.build())
        }
    }

    fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Category.values().forEach { category ->
                val categoryResources = CategoryResources(category)

                createNotificationChannelForCategory(
                    channelId = category.name,
                    name = context.getString(categoryResources.labelId)
                )
            }
        }
    }

    fun dismissNotification(context: Context, recall: Recall) {
        val notificationId = recall.getNotificationId()

        NotificationManagerCompat.from(context).cancel(notificationId)
    }
}