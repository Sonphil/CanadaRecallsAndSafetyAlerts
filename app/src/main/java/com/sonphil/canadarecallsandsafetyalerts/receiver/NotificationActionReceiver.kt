package com.sonphil.canadarecallsandsafetyalerts.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivityArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Sonphil on 26-02-20.
 */

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationActionHandler: NotificationActionHandler

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        val recall = intent.extras?.run {
            RecallDetailsActivityArgs.fromBundle(this).recall
        }

        notificationActionHandler.handleRecallAction(context, recall, action)
    }
}
