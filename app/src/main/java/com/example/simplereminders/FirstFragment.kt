package com.example.simplereminders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplereminders.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var reminderManager: ReminderManager
    private lateinit var reminderAdapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderManager = ReminderManager(requireContext())
        
        setupRecyclerView()
        loadReminders()
    }
    
    private fun setupRecyclerView() {
        reminderAdapter = ReminderAdapter(
            reminders = emptyList(),
            onToggle = { id -> 
                reminderManager.toggleReminder(id)
                loadReminders()
            },
            onDelete = { id ->
                reminderManager.deleteReminder(id)
                loadReminders()
            }
        )
        
        binding.remindersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reminderAdapter
        }
    }
    
    private fun loadReminders() {
        val reminders = reminderManager.getReminders()
        reminderAdapter.updateReminders(reminders)
        
        // Show empty state if no reminders
        if (reminders.isEmpty()) {
            binding.emptyStateText.visibility = View.VISIBLE
            binding.remindersRecyclerView.visibility = View.GONE
        } else {
            binding.emptyStateText.visibility = View.GONE
            binding.remindersRecyclerView.visibility = View.VISIBLE
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadReminders() // Refresh when returning from add screen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}