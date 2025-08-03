package com.example.simplereminders

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.simplereminders.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var reminderManager: ReminderManager
    private lateinit var settingsManager: SettingsManager
    private var selectedHour = 9 // Default 9:00 AM
    private var selectedMinute = 0
    private var editingReminderId: Long = -1L // -1 means creating new reminder
    private var editingReminder: Reminder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderManager = ReminderManager(requireContext())
        settingsManager = SettingsManager(requireContext())
        
        // Set default snooze duration from settings
        binding.snoozeDurationInput.setText(settingsManager.getDefaultSnoozeDuration().toString())
        
        // Check if we're editing an existing reminder
        editingReminderId = arguments?.getLong("reminderId", -1L) ?: -1L
        if (editingReminderId != -1L) {
            try {
                editingReminder = reminderManager.getReminders().find { it.id == editingReminderId }
            } catch (e: Exception) {
                android.util.Log.e("SecondFragment", "Error loading reminder for editing: ${e.message}", e)
                Toast.makeText(context, "Error loading reminder. Creating new one instead.", Toast.LENGTH_SHORT).show()
                editingReminder = null
                editingReminderId = -1L
            }
        }
        
        setupFrequencyRadioButtons()
        setupMonthlyPatternRadioButtons()
        setupMonthlySpinners()
        setupTimePickerButton()
        setupButtons()
        updateIntervalUnitText()
        
        // Load reminder data after UI setup is complete
        if (editingReminder != null) {
            try {
                loadReminderForEditing()
            } catch (e: Exception) {
                android.util.Log.e("SecondFragment", "Error loading reminder data: ${e.message}", e)
                Toast.makeText(context, "Error loading reminder data. Some fields may be reset.", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Initialize UI state - hide interval container for default "One time" selection
        if (editingReminder == null) {
            binding.customIntervalContainer.visibility = View.GONE
            binding.daysOfWeekContainer.visibility = View.GONE
            binding.dayOfMonthContainer.visibility = View.GONE
        }
    }
    
    private fun loadReminderForEditing() {
        editingReminder?.let { reminder ->
            // Update screen title and button text for edit mode
            binding.screenTitle.text = "Edit Reminder"
            binding.saveButton.text = "Update Reminder"
            
            // Populate form with existing reminder data
            binding.reminderTitleInput.setText(reminder.title)
            
            // Set frequency radio button
            when (reminder.frequency) {
                Frequency.ONE_TIME -> binding.oneTimeRadio.isChecked = true
                Frequency.DAILY -> binding.dailyRadio.isChecked = true
                Frequency.WEEKLY -> binding.weeklyRadio.isChecked = true
                Frequency.MONTHLY -> binding.monthlyRadio.isChecked = true
                Frequency.CUSTOM_DAYS -> binding.customDaysRadio.isChecked = true
            }
            
            // Set custom interval
            binding.intervalInput.setText(reminder.customInterval.toString())
            
            // Set day of month
            binding.dayOfMonthInput.setText(reminder.dayOfMonth.toString())
            
            // Set monthly pattern
            when (reminder.monthlyPattern) {
                MonthlyPattern.SPECIFIC_DATE -> binding.specificDateRadio.isChecked = true
                MonthlyPattern.RELATIVE_DAY -> binding.relativeDayRadio.isChecked = true
            }
            
            // Set monthly ordinal and day of week for spinners
            val ordinalIndex = MonthlyOrdinal.values().indexOf(reminder.monthlyOrdinal)
            if (ordinalIndex >= 0) {
                binding.monthlyOrdinalSpinner.setSelection(ordinalIndex)
            }
            
            val dayIndex = DayOfWeek.values().indexOf(reminder.monthlyDayOfWeek)
            if (dayIndex >= 0) {
                binding.monthlyDayOfWeekSpinner.setSelection(dayIndex)
            }
            
            // Set time
            selectedHour = reminder.reminderHour
            selectedMinute = reminder.reminderMinute
            updateTimeButtonText()
            
            // Set snooze duration
            binding.snoozeDurationInput.setText(reminder.snoozeDurationMinutes.toString())
            
            // Set days of week
            reminder.daysOfWeek.forEach { day ->
                when (day) {
                    DayOfWeek.MONDAY -> binding.mondayCheckbox.isChecked = true
                    DayOfWeek.TUESDAY -> binding.tuesdayCheckbox.isChecked = true
                    DayOfWeek.WEDNESDAY -> binding.wednesdayCheckbox.isChecked = true
                    DayOfWeek.THURSDAY -> binding.thursdayCheckbox.isChecked = true
                    DayOfWeek.FRIDAY -> binding.fridayCheckbox.isChecked = true
                    DayOfWeek.SATURDAY -> binding.saturdayCheckbox.isChecked = true
                    DayOfWeek.SUNDAY -> binding.sundayCheckbox.isChecked = true
                }
            }
            
            // Update UI visibility based on frequency
            setupFrequencyRadioButtons()
            setupMonthlyPatternRadioButtons()
            // Trigger the radio button change to show/hide appropriate sections
            binding.frequencyRadioGroup.check(binding.frequencyRadioGroup.checkedRadioButtonId)
            binding.monthlyPatternRadioGroup.check(binding.monthlyPatternRadioGroup.checkedRadioButtonId)
        }
    }
    
    private fun setupFrequencyRadioButtons() {
        binding.frequencyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.weekly_radio -> {
                    binding.daysOfWeekContainer.visibility = View.VISIBLE
                    binding.customIntervalContainer.visibility = View.VISIBLE
                    binding.dayOfMonthContainer.visibility = View.GONE
                }
                R.id.monthly_radio -> {
                    binding.daysOfWeekContainer.visibility = View.GONE
                    binding.customIntervalContainer.visibility = View.VISIBLE
                    binding.dayOfMonthContainer.visibility = View.VISIBLE
                }
                R.id.custom_days_radio -> {
                    binding.daysOfWeekContainer.visibility = View.GONE
                    binding.customIntervalContainer.visibility = View.VISIBLE
                    binding.dayOfMonthContainer.visibility = View.GONE
                }
                else -> {
                    // One time and Daily don't show interval, days, or day of month
                    binding.daysOfWeekContainer.visibility = View.GONE
                    binding.customIntervalContainer.visibility = View.GONE
                    binding.dayOfMonthContainer.visibility = View.GONE
                }
            }
            updateIntervalUnitText()
        }
    }
    
    private fun setupMonthlyPatternRadioButtons() {
        binding.monthlyPatternRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.specific_date_radio -> {
                    binding.specificDateContainer.visibility = View.VISIBLE
                    binding.relativeDayContainer.visibility = View.GONE
                }
                R.id.relative_day_radio -> {
                    binding.specificDateContainer.visibility = View.GONE
                    binding.relativeDayContainer.visibility = View.VISIBLE
                }
            }
        }
    }
    
    private fun setupMonthlySpinners() {
        // Setup ordinal spinner
        val ordinalOptions = MonthlyOrdinal.values().map { it.displayName }
        val ordinalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ordinalOptions)
        ordinalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthlyOrdinalSpinner.adapter = ordinalAdapter
        
        // Setup day of week spinner
        val dayOptions = DayOfWeek.values().map { it.displayName }
        val dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dayOptions)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthlyDayOfWeekSpinner.adapter = dayAdapter
    }
    
    private fun updateIntervalUnitText() {
        val unitText = when (binding.frequencyRadioGroup.checkedRadioButtonId) {
            R.id.daily_radio -> "days"
            R.id.weekly_radio -> "weeks"
            R.id.monthly_radio -> "months"
            R.id.custom_days_radio -> "days"
            else -> "days"
        }
        binding.intervalUnitText.text = unitText
    }
    
    private fun setupTimePickerButton() {
        updateTimeButtonText()
        
        binding.timePickerButton.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    updateTimeButtonText()
                },
                selectedHour,
                selectedMinute,
                false // Use 12-hour format
            ).show()
        }
    }
    
    private fun updateTimeButtonText() {
        val hour12 = if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour
        val amPm = if (selectedHour < 12) "AM" else "PM"
        binding.timePickerButton.text = String.format("%d:%02d %s", hour12, selectedMinute, amPm)
    }
    
    private fun setupButtons() {
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.saveButton.setOnClickListener {
            saveReminder()
        }
    }
    
    private fun saveReminder() {
        val title = binding.reminderTitleInput.text?.toString()?.trim()
        
        if (title.isNullOrBlank()) {
            Toast.makeText(context, "Please enter a reminder title", Toast.LENGTH_SHORT).show()
            return
        }
        
        val frequency = when (binding.frequencyRadioGroup.checkedRadioButtonId) {
            R.id.one_time_radio -> Frequency.ONE_TIME
            R.id.daily_radio -> Frequency.DAILY
            R.id.weekly_radio -> Frequency.WEEKLY
            R.id.monthly_radio -> Frequency.MONTHLY
            R.id.custom_days_radio -> Frequency.CUSTOM_DAYS
            else -> Frequency.ONE_TIME
        }
        
        // Only get custom interval for frequencies that need it
        val customInterval = if (frequency in listOf(Frequency.WEEKLY, Frequency.MONTHLY, Frequency.CUSTOM_DAYS)) {
            val interval = binding.intervalInput.text?.toString()?.toIntOrNull() ?: 1
            if (interval < 1) {
                Toast.makeText(context, "Interval must be at least 1", Toast.LENGTH_SHORT).show()
                return
            }
            interval
        } else {
            1 // Default for one-time and daily
        }
        
        // Get day of month and monthly pattern for monthly reminders
        val dayOfMonth: Int
        val monthlyPattern: MonthlyPattern
        val monthlyOrdinal: MonthlyOrdinal
        val monthlyDayOfWeek: DayOfWeek
        
        if (frequency == Frequency.MONTHLY) {
            monthlyPattern = when (binding.monthlyPatternRadioGroup.checkedRadioButtonId) {
                R.id.specific_date_radio -> MonthlyPattern.SPECIFIC_DATE
                R.id.relative_day_radio -> MonthlyPattern.RELATIVE_DAY
                else -> MonthlyPattern.SPECIFIC_DATE
            }
            
            if (monthlyPattern == MonthlyPattern.SPECIFIC_DATE) {
                val day = binding.dayOfMonthInput.text?.toString()?.toIntOrNull() ?: 1
                if (day < 1 || day > 31) {
                    Toast.makeText(context, "Day of month must be between 1 and 31", Toast.LENGTH_SHORT).show()
                    return
                }
                dayOfMonth = day
                monthlyOrdinal = MonthlyOrdinal.FIRST // Default values for specific date
                monthlyDayOfWeek = DayOfWeek.MONDAY
            } else {
                // Relative day pattern
                dayOfMonth = 1 // Default value for relative day
                val ordinalIndex = binding.monthlyOrdinalSpinner.selectedItemPosition
                monthlyOrdinal = if (ordinalIndex >= 0 && ordinalIndex < MonthlyOrdinal.values().size) {
                    MonthlyOrdinal.values()[ordinalIndex]
                } else {
                    MonthlyOrdinal.FIRST
                }
                
                val dayIndex = binding.monthlyDayOfWeekSpinner.selectedItemPosition
                monthlyDayOfWeek = if (dayIndex >= 0 && dayIndex < DayOfWeek.values().size) {
                    DayOfWeek.values()[dayIndex]
                } else {
                    DayOfWeek.MONDAY
                }
            }
        } else {
            dayOfMonth = 1 // Default for other frequencies
            monthlyPattern = MonthlyPattern.SPECIFIC_DATE
            monthlyOrdinal = MonthlyOrdinal.FIRST
            monthlyDayOfWeek = DayOfWeek.MONDAY
        }
        
        val daysOfWeek = if (frequency == Frequency.WEEKLY) {
            getSelectedDaysOfWeek()
        } else {
            emptySet()
        }
        
        // Validate weekly frequency has at least one day selected
        if (frequency == Frequency.WEEKLY && daysOfWeek.isEmpty()) {
            Toast.makeText(context, "Please select at least one day for weekly reminders", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Get and validate snooze duration
        val snoozeDurationText = binding.snoozeDurationInput.text?.toString()?.trim()
        val snoozeDuration = try {
            snoozeDurationText?.toInt()?.coerceIn(1, 999) ?: settingsManager.getDefaultSnoozeDuration()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Please enter a valid snooze duration (1-999 minutes)", Toast.LENGTH_SHORT).show()
            return
        }
        
        val reminder = Reminder(
            id = if (editingReminderId != -1L) editingReminderId else 0,
            title = title,
            frequency = frequency,
            customInterval = customInterval,
            daysOfWeek = daysOfWeek,
            dayOfMonth = dayOfMonth,
            monthlyPattern = monthlyPattern,
            monthlyOrdinal = monthlyOrdinal,
            monthlyDayOfWeek = monthlyDayOfWeek,
            reminderHour = selectedHour,
            reminderMinute = selectedMinute,
            snoozeDurationMinutes = snoozeDuration
        )
        
        if (editingReminderId != -1L) {
            // Update existing reminder
            reminderManager.updateReminder(reminder)
            Toast.makeText(context, "Reminder updated!", Toast.LENGTH_SHORT).show()
        } else {
            // Add new reminder
            reminderManager.addReminder(reminder)
            Toast.makeText(context, "Reminder saved!", Toast.LENGTH_SHORT).show()
        }
        findNavController().navigateUp()
    }
    
    private fun getSelectedDaysOfWeek(): Set<DayOfWeek> {
        val selected = mutableSetOf<DayOfWeek>()
        
        val checkboxes = listOf(
            binding.mondayCheckbox to DayOfWeek.MONDAY,
            binding.tuesdayCheckbox to DayOfWeek.TUESDAY,
            binding.wednesdayCheckbox to DayOfWeek.WEDNESDAY,
            binding.thursdayCheckbox to DayOfWeek.THURSDAY,
            binding.fridayCheckbox to DayOfWeek.FRIDAY,
            binding.saturdayCheckbox to DayOfWeek.SATURDAY,
            binding.sundayCheckbox to DayOfWeek.SUNDAY
        )
        
        checkboxes.forEach { (checkbox, day) ->
            if (checkbox.isChecked) {
                selected.add(day)
            }
        }
        
        return selected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}