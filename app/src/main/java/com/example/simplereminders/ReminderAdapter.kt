package com.example.simplereminders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReminderAdapter(
    private var reminders: List<Reminder>,
    private val onToggle: (Long) -> Unit,
    private val onDelete: (Long) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.reminder_title)
        val frequencyText: TextView = view.findViewById(R.id.reminder_frequency)
        val daysText: TextView = view.findViewById(R.id.reminder_days)
        val activeSwitch: Switch = view.findViewById(R.id.reminder_switch)
        val deleteButton: View = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        
        holder.titleText.text = reminder.title
        
        // Format frequency text with custom intervals and time
        val frequencyText = when (reminder.frequency) {
            Frequency.DAILY -> if (reminder.customInterval == 1) "Daily" else "Every ${reminder.customInterval} days"
            Frequency.WEEKLY -> if (reminder.customInterval == 1) "Weekly" else "Every ${reminder.customInterval} weeks"
            Frequency.MONTHLY -> if (reminder.customInterval == 1) "Monthly" else "Every ${reminder.customInterval} months"
            Frequency.CUSTOM_DAYS -> "Every ${reminder.customInterval} days"
        }
        
        val timeText = reminder.reminderTime.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"))
        holder.frequencyText.text = "$frequencyText at $timeText"
        
        // Format days of week
        if (reminder.frequency == Frequency.WEEKLY && reminder.daysOfWeek.isNotEmpty()) {
            val sortedDays = reminder.daysOfWeek.sortedBy { it.ordinal }
            holder.daysText.text = sortedDays.joinToString(", ") { it.displayName }
            holder.daysText.visibility = View.VISIBLE
        } else {
            holder.daysText.visibility = View.GONE
        }
        
        holder.activeSwitch.isChecked = reminder.isActive
        holder.activeSwitch.setOnCheckedChangeListener { _, _ ->
            onToggle(reminder.id)
        }
        
        holder.deleteButton.setOnClickListener {
            onDelete(reminder.id)
        }
    }

    override fun getItemCount() = reminders.size

    fun updateReminders(newReminders: List<Reminder>) {
        reminders = newReminders
        notifyDataSetChanged()
    }
}
