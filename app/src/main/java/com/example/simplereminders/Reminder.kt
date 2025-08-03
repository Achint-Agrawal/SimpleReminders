package com.example.simplereminders

data class Reminder(
    val id: Long = 0,
    val title: String,
    val frequency: Frequency,
    val customInterval: Int = 1, // For every X days/weeks/months
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val dayOfMonth: Int = 1, // Day of month for monthly reminders (1-31)
    val monthlyPattern: MonthlyPattern = MonthlyPattern.SPECIFIC_DATE, // Pattern type for monthly reminders
    val monthlyOrdinal: MonthlyOrdinal = MonthlyOrdinal.FIRST, // First, second, third, fourth, last
    val monthlyDayOfWeek: DayOfWeek = DayOfWeek.MONDAY, // Day of week for relative monthly patterns
    val reminderHour: Int = 9, // Hour in 24-hour format (0-23)
    val reminderMinute: Int = 0, // Minute (0-59)
    val snoozeDurationMinutes: Int = 5, // Default snooze duration in minutes
    val isActive: Boolean = true
) {
    // Helper function to format time for display
    fun getFormattedTime(): String {
        val hour12 = if (reminderHour == 0) 12 else if (reminderHour > 12) reminderHour - 12 else reminderHour
        val amPm = if (reminderHour < 12) "AM" else "PM"
        return String.format("%d:%02d %s", hour12, reminderMinute, amPm)
    }
    
    // Helper function to format day of month with ordinal suffix
    fun getFormattedDayOfMonth(): String {
        val suffix = when {
            dayOfMonth in 11..13 -> "th"
            dayOfMonth % 10 == 1 -> "st"
            dayOfMonth % 10 == 2 -> "nd"
            dayOfMonth % 10 == 3 -> "rd"
            else -> "th"
        }
        return "$dayOfMonth$suffix"
    }
    
    // Helper function to format monthly pattern for display
    fun getFormattedMonthlyPattern(): String {
        return when (monthlyPattern) {
            MonthlyPattern.SPECIFIC_DATE -> "On the ${getFormattedDayOfMonth()} of each month"
            MonthlyPattern.RELATIVE_DAY -> "${monthlyOrdinal.displayName} ${monthlyDayOfWeek.displayName} of each month"
        }
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

enum class MonthlyPattern(val displayName: String) {
    SPECIFIC_DATE("Specific date"), // e.g., 15th of each month
    RELATIVE_DAY("Relative day") // e.g., first Friday of each month
}

enum class MonthlyOrdinal(val displayName: String) {
    FIRST("First"),
    SECOND("Second"),
    THIRD("Third"),
    FOURTH("Fourth"),
    LAST("Last")
}
