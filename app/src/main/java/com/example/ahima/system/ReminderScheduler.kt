package com.example.ahima.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log

object ReminderScheduler {

    fun scheduleDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val interval = AlarmManager.INTERVAL_DAY
        val triggerAtMillis = SystemClock.elapsedRealtime() + interval

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            interval,
            pendingIntent
        )

        Log.d("ReminderScheduler", "Напоминание запланировано раз в сутки")
    }
}
