package com.example.simplereminders

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReminderManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("reminders", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var nextId = 1L

    init {
        nextId = prefs.getLong("next_id", 1L)
    }

    fun getReminders(): List<Reminder> {
        val json = prefs.getString("reminders_list", "[]")
        val type = object : TypeToken<List<Reminder>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addReminder(reminder: Reminder): Reminder {
        val newReminder = reminder.copy(id = nextId++)
        val reminders = getReminders().toMutableList()
        reminders.add(newReminder)
        saveReminders(reminders)
        saveNextId()
        return newReminder
    }

    fun deleteReminder(id: Long) {
        val reminders = getReminders().toMutableList()
        reminders.removeAll { it.id == id }
        saveReminders(reminders)
    }

    fun toggleReminder(id: Long) {
        val reminders = getReminders().toMutableList()
        val index = reminders.indexOfFirst { it.id == id }
        if (index != -1) {
            reminders[index] = reminders[index].copy(isActive = !reminders[index].isActive)
            saveReminders(reminders)
        }
    }

    private fun saveReminders(reminders: List<Reminder>) {
        val json = gson.toJson(reminders)
        prefs.edit().putString("reminders_list", json).apply()
    }

    private fun saveNextId() {
        prefs.edit().putLong("next_id", nextId).apply()
    }
}
