package com.example.ahima.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ahima.system.AppNotificationManager

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AppNotificationManager.showNotification(
            context,
            title = "Напоминание",
            message = "Пора выполнить задание на сегодня"
        )
    }
}
