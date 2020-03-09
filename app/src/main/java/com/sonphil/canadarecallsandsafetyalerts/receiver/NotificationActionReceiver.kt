package com.sonphil.canadarecallsandsafetyalerts.receiver

import android.content.Context
import android.content.Intent
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivityArgs
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject

/**
 * Created by Sonphil on 26-02-20.
 */

class NotificationActionReceiver : DaggerBroadcastReceiver() {
    @Inject
    lateinit var notificationActionHandler: NotificationActionHandler

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val action = intent.action

        val recall = intent.extras?.run {
            RecallDetailsActivityArgs.fromBundle(this).recall
        }

        notificationActionHandler.handleRecallAction(context, recall, action)
    }
}