package com.anima.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

class SchedulerService(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleDailyTask(taskTitle: String, hour: Int, minute: Int) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_DAILY_TASK
            putExtra(EXTRA_TASK_TITLE, taskTitle)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_TASK_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = calculateTriggerTime(hour, minute)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun scheduleMeditationReminder(hour: Int, minute: Int) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_MEDITATION_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MEDITATION_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = calculateTriggerTime(hour, minute)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun scheduleMoodCheck(hour: Int, minute: Int) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_MOOD_CHECK
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MOOD_CHECK_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = calculateTriggerTime(hour, minute)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelDailyTask() {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_DAILY_TASK
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_TASK_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun cancelMeditationReminder() {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_MEDITATION_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MEDITATION_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun cancelMoodCheck() {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_MOOD_CHECK
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MOOD_CHECK_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun calculateTriggerTime(hour: Int, minute: Int): Long {
        val now = LocalDateTime.now()
        var triggerTime = now.withHour(hour).withMinute(minute).withSecond(0)
        
        if (triggerTime.isBefore(now)) {
            triggerTime = triggerTime.plusDays(1)
        }

        return triggerTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    companion object {
        const val ACTION_DAILY_TASK = "com.anima.action.DAILY_TASK"
        const val ACTION_MEDITATION_REMINDER = "com.anima.action.MEDITATION_REMINDER"
        const val ACTION_MOOD_CHECK = "com.anima.action.MOOD_CHECK"
        const val EXTRA_TASK_TITLE = "task_title"

        private const val DAILY_TASK_REQUEST_CODE = 1
        private const val MEDITATION_REMINDER_REQUEST_CODE = 2
        private const val MOOD_CHECK_REQUEST_CODE = 3
    }
}

class NotificationReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationService = NotificationService(context)

        when (intent.action) {
            SchedulerService.ACTION_DAILY_TASK -> {
                val taskTitle = intent.getStringExtra(SchedulerService.EXTRA_TASK_TITLE) ?: "Новое задание"
                notificationService.showDailyTaskNotification(taskTitle)
            }
            SchedulerService.ACTION_MEDITATION_REMINDER -> {
                notificationService.showMeditationReminder()
            }
            SchedulerService.ACTION_MOOD_CHECK -> {
                notificationService.showMoodCheckReminder()
            }
        }
    }
} 