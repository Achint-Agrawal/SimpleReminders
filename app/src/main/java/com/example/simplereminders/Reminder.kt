package com.example.simplereminders

import java.time.LocalTime

data class Reminder(
    val id: Long = 0,
    val title: String,
    val frequency: Frequency,
    val customInterval: Int = 1, // For every X days/weeks/months
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val reminderTime: LocalTime = LocalTime.of(9, 0), // Default 9:00 AM
    val isActive: Boolean = true
)

enum class Frequency(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    CUSTOM_DAYS("Every X Days")
}

enum class DayOfWeek(val displayName: String) {
    MONDAY("Mon"),
    TUESDAY("Tue"),
    WEDNESDAY("Wed"),
    THURSDAY("Thu"),
    FRIDAY("Fri"),
    SATURDAY("Sat"),
    SUNDAY("Sun")
}
