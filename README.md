_Very simple. Very reminders._

# Simple Reminders

A focused Android app for creating and managing notification-based reminders with completion and snooze functionality.

## Features

- Create reminders with custom titles and snooze duration (1–999 minutes)
- Frequencies: One-time, Daily, Weekly (specific days), Monthly (specific date or relative e.g., "first Friday"), or Every X days
- System notifications with actions: Mark as Done and Snooze
- Toggle reminders on/off; edit and delete reminders easily
- Local-only storage; Material Design UI

## How to Use

1. **View Reminders**: See all your reminders on the main screen
2. **Add New Reminder**: Tap the + button to create a reminder
   - Enter a title for your reminder
   - Choose frequency (One-time, Daily, Weekly, Monthly, or custom interval)
   - Set the reminder time using the time picker
   - Enter custom snooze duration in minutes (1–999)
   - For weekly reminders, select specific days
   - For monthly reminders, set the day of month
   - Tap "Save Reminder"
3. **Configure Settings**: Access settings from the menu
   - Set default snooze duration for new reminders
4. **Manage Reminders**:
   - Toggle reminders on/off using the switch
   - Edit reminders by tapping on them
   - Delete reminders using the trash icon
5. **Handle Notifications**:
   - When a reminder triggers, you'll get a notification
   - Tap "Mark as Done" to complete the reminder
   - Tap "Snooze" to delay the reminder by your configured duration
   - One-time reminders are automatically disabled when marked as done
   - Recurring reminders continue after being marked as done

## Notification Features

- **System Integration**: Notifications respect your device's notification settings
- **Actions**: Each notification includes "Mark as Done" and "Snooze" buttons
- **Smart Scheduling**: Notifications are delivered even when the app isn't running
- **Snooze Control**: Custom snooze duration input per reminder (1–999 minutes)
- **Configurable Defaults**: Set default snooze duration in app settings
- **Completion Logic**: One-time reminders auto-disable; recurring reminders continue
- **Battery Optimized**: Uses Android's AlarmManager for efficient scheduling

## Screenshots

The app features:

- Main screen with reminder list and empty state
- Create/edit reminder form with frequency and time selection
- Day-of-week selection for weekly reminders
- Day-of-month input for monthly reminders
- Custom snooze duration input field
- Settings screen for default configurations
- System notifications with action buttons
- Card-based layout for easy viewing

## Getting Started

### Prerequisites

- Android Studio
- Android SDK (API level as specified in build.gradle)
- Kotlin

### Building and Running

Per repo policy, builds are performed in Android Studio (not via CLI in this workspace):

1. Open Android Studio > Open this project folder
2. Let Gradle sync complete
3. Build > Make Project
4. Run on a device/emulator (API 24+)
5. On first run, allow permissions/prompts:
   - Notifications (Android 13+)
   - Exact alarms (Android 12+)
   - Ignore battery optimizations (Android 6+)

## Project Structure

- `app/` - Main application module
- `app/src/main/java/` - Kotlin source files
- `app/src/main/res/` - Android resources (layouts, drawables, etc.)

## License

MIT — see [LICENSE](LICENSE).
