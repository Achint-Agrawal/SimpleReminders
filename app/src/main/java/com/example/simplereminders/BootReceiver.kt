package com.example.simplereminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {
                try {
                    val reminderManager = ReminderManager(context)
                    val scheduler = ReminderScheduler(context)
                    reminderManager.getReminders()
                        .filter { it.isActive }
                        .forEach { scheduler.scheduleReminder(it) }
                    Log.i("BootReceiver", "Rescheduled active reminders after ${intent.action}")
                } catch (e: Exception) {
                    Log.e("BootReceiver", "Failed to reschedule reminders: ${e.message}", e)
                }
            }
        }
    }
}
