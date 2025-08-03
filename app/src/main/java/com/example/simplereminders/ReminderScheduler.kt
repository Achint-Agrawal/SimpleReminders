package com.example.simplereminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.*

class ReminderScheduler(private val context: Context) {
    
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    fun scheduleReminder(reminder: Reminder) {
        if (!reminder.isActive) {
            cancelReminder(reminder.id)
            return
        }
        
        val nextTriggerTime = calculateNextTriggerTime(reminder)
        if (nextTriggerTime != null) {
            val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
                putExtra("reminder_id", reminder.id)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // Use setExactAndAllowWhileIdle for precise timing even in doze mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextTriggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    nextTriggerTime,
                    pendingIntent
                )
            }
            
            Log.d("ReminderScheduler", "Scheduled reminder '${reminder.title}' for ${Date(nextTriggerTime)}")
        }
    }
    
    fun scheduleSnooze(reminder: Reminder, snoozeMinutes: Int) {
        val snoozeTime = System.currentTimeMillis() + (snoozeMinutes * 60 * 1000)
        
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("reminder_id", reminder.id)
            putExtra("is_snoozed", true)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (reminder.id * 1000).toInt(), // Different request code for snooze
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                snoozeTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                snoozeTime,
                pendingIntent
            )
        }
        
        Log.d("ReminderScheduler", "Snoozed reminder '${reminder.title}' for $snoozeMinutes minutes")
    }
    
    fun cancelReminder(reminderId: Long) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
        Log.d("ReminderScheduler", "Cancelled reminder with ID: $reminderId")
    }
    
    private fun calculateNextTriggerTime(reminder: Reminder): Long? {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        
        // Set the time
        calendar.set(Calendar.HOUR_OF_DAY, reminder.reminderHour)
        calendar.set(Calendar.MINUTE, reminder.reminderMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        when (reminder.frequency) {
            Frequency.ONE_TIME -> {
                // If the time today has passed, schedule for tomorrow
                if (calendar.timeInMillis <= currentTime) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                return calendar.timeInMillis
            }
            
            Frequency.DAILY -> {
                // If the time today has passed, schedule for tomorrow
                if (calendar.timeInMillis <= currentTime) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                return calendar.timeInMillis
            }
            
            Frequency.WEEKLY -> {
                if (reminder.daysOfWeek.isEmpty()) {
                    return null
                }
                
                return findNextWeeklyOccurrence(calendar, reminder.daysOfWeek, currentTime)
            }
            
            Frequency.MONTHLY -> {
                return findNextMonthlyOccurrence(calendar, reminder.dayOfMonth, currentTime)
            }
            
            Frequency.CUSTOM_DAYS -> {
                // If the time today has passed, schedule for next interval
                if (calendar.timeInMillis <= currentTime) {
                    calendar.add(Calendar.DAY_OF_YEAR, reminder.customInterval)
                }
                return calendar.timeInMillis
            }
        }
    }
    
    private fun findNextWeeklyOccurrence(calendar: Calendar, daysOfWeek: Set<DayOfWeek>, currentTime: Long): Long? {
        val targetDays = daysOfWeek.map { dayOfWeek ->
            when (dayOfWeek) {
                DayOfWeek.SUNDAY -> Calendar.SUNDAY
                DayOfWeek.MONDAY -> Calendar.MONDAY
                DayOfWeek.TUESDAY -> Calendar.TUESDAY
                DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
                DayOfWeek.THURSDAY -> Calendar.THURSDAY
                DayOfWeek.FRIDAY -> Calendar.FRIDAY
                DayOfWeek.SATURDAY -> Calendar.SATURDAY
            }
        }.sorted()
        
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        
        // Find the next occurrence within this week
        for (targetDay in targetDays) {
            calendar.set(Calendar.DAY_OF_WEEK, targetDay)
            if (calendar.timeInMillis > currentTime) {
                return calendar.timeInMillis
            }
        }
        
        // No more occurrences this week, find the first occurrence next week
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        calendar.set(Calendar.DAY_OF_WEEK, targetDays.first())
        return calendar.timeInMillis
    }
    
    private fun findNextMonthlyOccurrence(calendar: Calendar, dayOfMonth: Int, currentTime: Long): Long {
        calendar.set(Calendar.DAY_OF_MONTH, minOf(dayOfMonth, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
        
        // If this month's occurrence has passed, move to next month
        if (calendar.timeInMillis <= currentTime) {
            calendar.add(Calendar.MONTH, 1)
            calendar.set(Calendar.DAY_OF_MONTH, minOf(dayOfMonth, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
        }
        
        return calendar.timeInMillis
    }
}
