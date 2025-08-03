package com.example.simplereminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderBroadcastReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra("reminder_id", -1)
        val isSnoozed = intent.getBooleanExtra("is_snoozed", false)
        
        if (reminderId == -1L) {
            Log.e("ReminderBroadcastReceiver", "Invalid reminder ID")
            return
        }
        
        val reminderManager = ReminderManager(context)
        val reminder = reminderManager.getReminder(reminderId)
        
        if (reminder == null) {
            Log.e("ReminderBroadcastReceiver", "Reminder not found: $reminderId")
            return
        }
        
        if (!reminder.isActive) {
            Log.d("ReminderBroadcastReceiver", "Reminder is inactive: ${reminder.title}")
            return
        }
        
        // Show the notification
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showReminderNotification(reminder)
        
        // If this is not a snoozed reminder, schedule the next occurrence for recurring reminders
        if (!isSnoozed && reminder.frequency != Frequency.ONE_TIME) {
            val reminderScheduler = ReminderScheduler(context)
            reminderScheduler.scheduleReminder(reminder)
        }
        
        Log.d("ReminderBroadcastReceiver", "Triggered notification for: ${reminder.title}")
    }
}
