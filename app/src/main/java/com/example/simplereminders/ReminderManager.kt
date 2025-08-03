package com.example.simplereminders

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReminderManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("reminders", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var nextId = 1L
    private val reminderScheduler = ReminderScheduler(context)

    init {
        nextId = prefs.getLong("next_id", 1L)
    }

    fun getReminders(): List<Reminder> {
        val json = prefs.getString("reminders_list", "[]")
        val type = object : TypeToken<List<Reminder>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            // Handle migration from old format or any parsing errors
            emptyList()
        }
    }

    fun addReminder(reminder: Reminder): Reminder {
        val newReminder = reminder.copy(id = nextId++)
        val reminders = getReminders().toMutableList()
        reminders.add(newReminder)
        saveReminders(reminders)
        saveNextId()
        
        // Schedule the reminder if it's active
        if (newReminder.isActive) {
            reminderScheduler.scheduleReminder(newReminder)
        }
        
        return newReminder
    }

    fun updateReminder(reminder: Reminder) {
        val reminders = getReminders().toMutableList()
        val index = reminders.indexOfFirst { it.id == reminder.id }
        if (index != -1) {
            reminders[index] = reminder
            saveReminders(reminders)
            
            // Reschedule the reminder
            reminderScheduler.cancelReminder(reminder.id)
            if (reminder.isActive) {
                reminderScheduler.scheduleReminder(reminder)
            }
        }
    }

    fun deleteReminder(id: Long) {
        val reminders = getReminders().toMutableList()
        reminders.removeAll { it.id == id }
        saveReminders(reminders)
        
        // Cancel the scheduled reminder
        reminderScheduler.cancelReminder(id)
    }

    fun toggleReminder(id: Long) {
        val reminders = getReminders().toMutableList()
        val index = reminders.indexOfFirst { it.id == id }
        if (index != -1) {
            val updatedReminder = reminders[index].copy(isActive = !reminders[index].isActive)
            reminders[index] = updatedReminder
            saveReminders(reminders)
            
            // Update scheduling based on active state
            reminderScheduler.cancelReminder(id)
            if (updatedReminder.isActive) {
                reminderScheduler.scheduleReminder(updatedReminder)
            }
        }
    }

    fun getReminder(id: Long): Reminder? {
        return getReminders().find { it.id == id }
    }

    private fun saveReminders(reminders: List<Reminder>) {
        val json = gson.toJson(reminders)
        prefs.edit().putString("reminders_list", json).apply()
    }

    private fun saveNextId() {
        prefs.edit().putLong("next_id", nextId).apply()
    }
}
