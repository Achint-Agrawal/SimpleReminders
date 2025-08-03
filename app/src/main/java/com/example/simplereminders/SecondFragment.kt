package com.example.simplereminders

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.simplereminders.databinding.FragmentSecondBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var reminderManager: ReminderManager
    private var selectedTime = LocalTime.of(9, 0) // Default 9:00 AM

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
        
        setupFrequencyRadioButtons()
        setupTimePickerButton()
        setupButtons()
        updateIntervalUnitText()
    }
    
    private fun setupFrequencyRadioButtons() {
        binding.frequencyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.weekly_radio -> {
                    binding.daysOfWeekContainer.visibility = View.VISIBLE
                }
                else -> {
                    binding.daysOfWeekContainer.visibility = View.GONE
                }
            }
            updateIntervalUnitText()
        }
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
                    selectedTime = LocalTime.of(hourOfDay, minute)
                    updateTimeButtonText()
                },
                selectedTime.hour,
                selectedTime.minute,
                false // Use 12-hour format
            ).show()
        }
    }
    
    private fun updateTimeButtonText() {
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        binding.timePickerButton.text = selectedTime.format(formatter)
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
            R.id.daily_radio -> Frequency.DAILY
            R.id.weekly_radio -> Frequency.WEEKLY
            R.id.monthly_radio -> Frequency.MONTHLY
            R.id.custom_days_radio -> Frequency.CUSTOM_DAYS
            else -> Frequency.DAILY
        }
        
        val customInterval = binding.intervalInput.text?.toString()?.toIntOrNull() ?: 1
        if (customInterval < 1) {
            Toast.makeText(context, "Interval must be at least 1", Toast.LENGTH_SHORT).show()
            return
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
        
        val reminder = Reminder(
            title = title,
            frequency = frequency,
            customInterval = customInterval,
            daysOfWeek = daysOfWeek,
            reminderTime = selectedTime
        )
        
        reminderManager.addReminder(reminder)
        Toast.makeText(context, "Reminder saved!", Toast.LENGTH_SHORT).show()
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