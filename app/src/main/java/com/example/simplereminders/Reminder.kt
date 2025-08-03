package com.example.simplereminders

data class Reminder(
    val id: Long = 0,
    val title: String,
    val frequency: Frequency,
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val isActive: Boolean = true
)

enum class Frequency {
    DAILY,
    WEEKLY,
    MONTHLY
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
