package com.example.simplereminders

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationHelper(private val context: Context) {
    
    companion object {
        const val CHANNEL_ID = "reminder_notifications"
        const val CHANNEL_NAME = "Reminder Notifications"
        const val CHANNEL_DESCRIPTION = "Notifications for scheduled reminders"
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
        
        // Notification action IDs
        const val ACTION_MARK_DONE = "action_mark_done"
        const val ACTION_SNOOZE = "action_snooze"
        
        // Intent extras
        const val EXTRA_REMINDER_ID = "reminder_id"
        const val EXTRA_REMINDER_TITLE = "reminder_title"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showReminderNotification(reminder: Reminder) {
        // Create intent for marking as done
        val markDoneIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_MARK_DONE
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            putExtra(EXTRA_REMINDER_TITLE, reminder.title)
        }
        val markDonePendingIntent = PendingIntent.getBroadcast(
            context, 
            (reminder.id * 2).toInt(), // Unique request code
            markDoneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Create intent for snoozing
        val snoozeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            putExtra(EXTRA_REMINDER_TITLE, reminder.title)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            (reminder.id * 2 + 1).toInt(), // Unique request code
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Create intent for tapping the notification (open app)
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            reminder.id.toInt(),
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText(reminder.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(false) // Don't auto-cancel, let user decide
            .setContentIntent(contentPendingIntent)
            .addAction(
                R.drawable.ic_launcher_foreground, // TODO: Use proper icon
                "Mark as Done",
                markDonePendingIntent
            )
            .addAction(
                R.drawable.ic_launcher_foreground, // TODO: Use proper icon
                "Snooze (${reminder.snoozeDurationMinutes}m)",
                snoozePendingIntent
            )
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
        
        // Show the notification
        NotificationManagerCompat.from(context).notify(reminder.id.toInt(), notification)
    }
    
    fun cancelNotification(reminderId: Long) {
        NotificationManagerCompat.from(context).cancel(reminderId.toInt())
    }
    
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } else {
            true // Pre-Android 13 doesn't require explicit permission
        }
    }
}
