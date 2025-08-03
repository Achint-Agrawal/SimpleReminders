package com.example.simplereminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationActionReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(NotificationHelper.EXTRA_REMINDER_ID, -1)
        val reminderTitle = intent.getStringExtra(NotificationHelper.EXTRA_REMINDER_TITLE) ?: ""
        
        if (reminderId == -1L) {
            Log.e("NotificationActionReceiver", "Invalid reminder ID")
            return
        }
        
        val reminderManager = ReminderManager(context)
        val notificationHelper = NotificationHelper(context)
        val reminderScheduler = ReminderScheduler(context)
        
        when (intent.action) {
            NotificationHelper.ACTION_MARK_DONE -> {
                handleMarkAsDone(reminderId, reminderManager, notificationHelper, reminderScheduler)
            }
            NotificationHelper.ACTION_SNOOZE -> {
                handleSnooze(reminderId, reminderManager, notificationHelper, reminderScheduler)
            }
        }
    }
    
    private fun handleMarkAsDone(
        reminderId: Long,
        reminderManager: ReminderManager,
        notificationHelper: NotificationHelper,
        reminderScheduler: ReminderScheduler
    ) {
        val reminder = reminderManager.getReminder(reminderId)
        if (reminder != null) {
            // Cancel the notification
            notificationHelper.cancelNotification(reminderId)
            
            // If it's a one-time reminder, disable it
            if (reminder.frequency == Frequency.ONE_TIME) {
                val updatedReminder = reminder.copy(isActive = false)
                reminderManager.updateReminder(updatedReminder)
            }
            // For recurring reminders, just schedule the next occurrence
            else {
                reminderScheduler.scheduleReminder(reminder)
            }
            
            Log.d("NotificationActionReceiver", "Marked reminder as done: ${reminder.title}")
        }
    }
    
    private fun handleSnooze(
        reminderId: Long,
        reminderManager: ReminderManager,
        notificationHelper: NotificationHelper,
        reminderScheduler: ReminderScheduler
    ) {
        val reminder = reminderManager.getReminder(reminderId)
        if (reminder != null) {
            // Cancel the current notification
            notificationHelper.cancelNotification(reminderId)
            
            // Schedule the snoozed reminder
            reminderScheduler.scheduleSnooze(reminder, reminder.snoozeDurationMinutes)
            
            Log.d("NotificationActionReceiver", "Snoozed reminder for ${reminder.snoozeDurationMinutes} minutes: ${reminder.title}")
        }
    }
}
