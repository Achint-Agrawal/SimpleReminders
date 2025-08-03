package com.example.simplereminders

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ReminderManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("reminders", Context.MODE_PRIVATE)
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .create()
    private var nextId = 1L

    init {
        nextId = prefs.getLong("next_id", 1L)
    }

    fun getReminders(): List<Reminder> {
        val json = prefs.getString("reminders_list", "[]")
        val type = object : TypeToken<List<Reminder>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            // Handle migration from old format
            emptyList()
        }
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

class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime {
        return LocalTime.parse(json?.asString, formatter)
    }
}
