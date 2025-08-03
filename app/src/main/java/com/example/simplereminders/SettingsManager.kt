package com.example.simplereminders

import android.content.Context
import androidx.preference.PreferenceManager

class SettingsManager(context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    
    companion object {
        private const val DEFAULT_SNOOZE_DURATION_KEY = "default_snooze_duration"
        private const val DEFAULT_SNOOZE_DURATION_VALUE = 5
    }
    
    fun getDefaultSnoozeDuration(): Int {
        val value = prefs.getString(DEFAULT_SNOOZE_DURATION_KEY, DEFAULT_SNOOZE_DURATION_VALUE.toString())
        return try {
            value?.toInt()?.coerceIn(1, 999) ?: DEFAULT_SNOOZE_DURATION_VALUE
        } catch (e: NumberFormatException) {
            DEFAULT_SNOOZE_DURATION_VALUE
        }
    }
    
    fun setDefaultSnoozeDuration(minutes: Int) {
        prefs.edit()
            .putString(DEFAULT_SNOOZE_DURATION_KEY, minutes.coerceIn(1, 999).toString())
            .apply()
    }
}
