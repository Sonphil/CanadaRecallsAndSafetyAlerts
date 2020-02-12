package com.sonphil.canadarecallsandsafetyalerts.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivityArgs

/**
 * Created by Sonphil on 11-02-20.
 */

object NotificationsUtils {
    /**
     * Returns a new [NotificationChannel]
     *
     * @param context
     * @param channelId The id of the channel
     * @param name The name of the channel
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelForCategory(
        context: Context,
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
     * @param context
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

    /**
     * Creates a notification for a given [Recall
     *
     * The [Recall]'s category for the notification's channel on Android 8 and higher.
     *
     * @param context
     * @param recall
     */
    fun notifyRecall(context: Context, recall: Recall) {
        val channelId = recall.category.name
        val categoryResources = CategoryResources(recall.category)
        val notificationId = try {
            recall.id.toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val categoryTitle = context.getString(categoryResources.labelId)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(categoryTitle)
            .setContentText(recall.title)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(recall.title))
            .setContentIntent(createDetailsPendingIntentForRecall(context, recall, notificationId))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = createNotificationChannelForCategory(
                context,
                recall.category.name,
                categoryTitle
            )

            notificationBuilder.setChannelId(notificationChannel.id)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notificationBuilder.build())
        }
    }
}