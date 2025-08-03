package com.example.simplereminders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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
        setupButtons()
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
        }
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
            else -> Frequency.DAILY
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
            daysOfWeek = daysOfWeek
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