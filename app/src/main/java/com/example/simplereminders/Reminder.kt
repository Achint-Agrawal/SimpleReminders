package com.example.simplereminders

data class Reminder(
    val id: Long = 0,
    val title: String,
    val frequency: Frequency,
    val customInterval: Int = 1, // For every X days/weeks/months
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val reminderHour: Int = 9, // Hour in 24-hour format (0-23)
    val reminderMinute: Int = 0, // Minute (0-59)
    val isActive: Boolean = true
) {
    // Helper function to format time for display
    fun getFormattedTime(): String {
        val hour12 = if (reminderHour == 0) 12 else if (reminderHour > 12) reminderHour - 12 else reminderHour
        val amPm = if (reminderHour < 12) "AM" else "PM"
        return String.format("%d:%02d %s", hour12, reminderMinute, amPm)
    }
}

enum class Frequency(val displayName: String) {
    ONE_TIME("One Time"),
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
