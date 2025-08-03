# Simple Reminders

A focused Android app for creating and managing notification-based reminders with completion and snooze functionality.

## Features

- ✅ **Create reminders** with custom titles and snooze duration
- ⏰ **Set frequency**: One-time, Daily, Weekly, Monthly, or custom intervals
- 📅 **Choose specific days** for weekly reminders (Mon, Tue, Wed, etc.)
- 📅 **Choose day of month** for monthly reminders (1st, 15th, etc.)
- 🔔 **System notifications** that respect your device's notification settings
- ✅ **Mark as Done** directly from notifications
- 😴 **Snooze reminders** with custom duration input (1-999 minutes)
- ⚙️ **Configurable settings** for default snooze duration
- 🔄 **Toggle reminders** on/off with a simple switch
- ✏️ **Edit reminders** by tapping on them
- 🗑️ **Delete reminders** easily
- 💾 **Local storage** - all data stored securely on device
- 🎨 **Clean, intuitive interface** with Material Design
- 📅 **Choose specific days** for weekly reminders (Mon, Tue, Wed, etc.)
- � **Choose day of month** for monthly reminders (1st, 15th, etc.)
- 🔔 **System notifications** that respect your device's notification settings
- ✅ **Mark as Done** directly from notifications
- 😴 **Snooze reminders** with configurable duration (5min to 2hrs)
- �🔄 **Toggle reminders** on/off with a simple switch
- ✏️ **Edit reminders** by tapping on them
- 🗑️ **Delete reminders** easily
- 💾 **Local storage** - all data stored securely on device
- 🎨 **Clean, intuitive interface** with Material Design

## How to Use

1. **View Reminders**: See all your reminders on the main screen
2. **Add New Reminder**: Tap the + button to create a reminder
   - Enter a title for your reminder
   - Choose frequency (One-time, Daily, Weekly, Monthly, or custom interval)
   - Set the reminder time using the time picker
   - Enter custom snooze duration in minutes (1-999)
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
- **Snooze Control**: Custom snooze duration input per reminder (1-999 minutes)
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

### Building the Project

1. Clone this repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Run the app on an emulator or physical device

## Project Structure

- `app/` - Main application module
- `app/src/main/java/` - Kotlin source files
- `app/src/main/res/` - Android resources (layouts, drawables, etc.)

## License

This project is open source and available under the [MIT License](LICENSE).
