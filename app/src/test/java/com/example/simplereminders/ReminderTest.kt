package com.example.simplereminders

import org.junit.Assert.assertEquals
import org.junit.Test

class ReminderTest {

    @Test
    fun `formatted time uses 12-hour clock with AM PM`() {
        val r1 = Reminder(title = "t", frequency = Frequency.DAILY, reminderHour = 0, reminderMinute = 5)
        val r2 = Reminder(title = "t", frequency = Frequency.DAILY, reminderHour = 9, reminderMinute = 0)
        val r3 = Reminder(title = "t", frequency = Frequency.DAILY, reminderHour = 15, reminderMinute = 30)

        assertEquals("12:05 AM", r1.getFormattedTime())
        assertEquals("9:00 AM", r2.getFormattedTime())
        assertEquals("3:30 PM", r3.getFormattedTime())
    }

    @Test
    fun `formatted day of month adds ordinal suffix`() {
        val r1 = Reminder(title = "t", frequency = Frequency.MONTHLY, dayOfMonth = 1)
        val r2 = Reminder(title = "t", frequency = Frequency.MONTHLY, dayOfMonth = 2)
        val r3 = Reminder(title = "t", frequency = Frequency.MONTHLY, dayOfMonth = 3)
        val r4 = Reminder(title = "t", frequency = Frequency.MONTHLY, dayOfMonth = 11)
        val r5 = Reminder(title = "t", frequency = Frequency.MONTHLY, dayOfMonth = 22)

        assertEquals("1st", r1.getFormattedDayOfMonth())
        assertEquals("2nd", r2.getFormattedDayOfMonth())
        assertEquals("3rd", r3.getFormattedDayOfMonth())
        assertEquals("11th", r4.getFormattedDayOfMonth())
        assertEquals("22nd", r5.getFormattedDayOfMonth())
    }
}
