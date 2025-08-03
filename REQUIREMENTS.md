# Simple Reminders - Requirements Document

## 1. Overview
Simple Reminders is a focused Android application that provides notification-based reminders with completion and snooze functionality. The app emphasizes simplicity and reliability for managing personal tasks and recurring events.

## 2. Functional Requirements

### 2.1 Core Features

#### 2.1.1 Reminder Management
- **FR-2.1.1.1**: Users shall be able to create reminders with custom titles
- **FR-2.1.1.2**: Users shall be able to edit existing reminders by tapping on them
- **FR-2.1.1.3**: Users shall be able to delete reminders
- **FR-2.1.1.4**: Users shall be able to toggle reminders on/off (active/inactive)
- **FR-2.1.1.5**: Users shall be able to view all reminders in a list format

#### 2.1.2 Frequency Configuration
- **FR-2.1.2.1**: Users shall be able to set one-time reminders
- **FR-2.1.2.2**: Users shall be able to set daily reminders
- **FR-2.1.2.3**: Users shall be able to set weekly reminders with specific days selection
- **FR-2.1.2.4**: Users shall be able to set monthly reminders with specific day of month
- **FR-2.1.2.5**: Users shall be able to set monthly reminders with relative patterns (e.g., "first Friday", "second Tuesday", "last Monday")
- **FR-2.1.2.6**: Users shall be able to set custom interval reminders (every X days/weeks/months)

#### 2.1.3 Time Configuration
- **FR-2.1.3.1**: Users shall be able to set specific times for reminders (hour and minute)
- **FR-2.1.3.2**: Time picker shall support 12-hour format display
- **FR-2.1.3.3**: Default reminder time shall be 9:00 AM for new reminders

#### 2.1.4 Notification System
- **FR-2.1.4.1**: Active reminders shall trigger system notifications at scheduled times
- **FR-2.1.4.2**: Notifications shall display the reminder title
- **FR-2.1.4.3**: Notifications shall include "Mark as Done" action button
- **FR-2.1.4.4**: Notifications shall include "Snooze" action button
- **FR-2.1.4.5**: Notifications shall respect Android system notification settings
- **FR-2.1.4.6**: Notifications shall use high priority for timely delivery

#### 2.1.5 Completion Management
- **FR-2.1.5.1**: Users shall be able to mark reminders as "Done" from notifications
- **FR-2.1.5.2**: Marking as "Done" shall dismiss the notification
- **FR-2.1.5.3**: One-time reminders shall be automatically disabled after completion
- **FR-2.1.5.4**: Recurring reminders shall remain active after completion

#### 2.1.6 Snooze Functionality
- **FR-2.1.6.1**: Users shall be able to snooze reminders from notifications
- **FR-2.1.6.2**: Each reminder shall have configurable snooze duration
- **FR-2.1.6.3**: Default snooze duration shall be 5 minutes
- **FR-2.1.6.4**: Users shall be able to input custom snooze duration in minutes (1-999)
- **FR-2.1.6.5**: Snoozed reminders shall re-trigger after snooze duration
- **FR-2.1.6.6**: Users shall be able to snooze multiple times
- **FR-2.1.6.7**: App shall provide settings to configure default snooze duration

#### 2.1.7 Data Persistence
- **FR-2.1.7.1**: All reminder data shall be stored locally on device
- **FR-2.1.7.2**: App shall maintain data between app restarts
- **FR-2.1.7.3**: App shall handle data migration for app updates
- **FR-2.1.7.4**: No internet connection required for core functionality

### 2.2 User Interface Requirements

#### 2.2.1 Main Screen
- **FR-2.2.1.1**: Display list of all reminders with title, frequency, and time
- **FR-2.2.1.2**: Show empty state message when no reminders exist
- **FR-2.2.1.3**: Provide floating action button (FAB) to add new reminders
- **FR-2.2.1.4**: Each reminder item shall show toggle switch and delete button
- **FR-2.2.1.5**: Tapping reminder item shall open edit screen

#### 2.2.2 Add/Edit Reminder Screen
- **FR-2.2.2.1**: Provide text input for reminder title
- **FR-2.2.2.2**: Provide frequency selection (radio buttons)
- **FR-2.2.2.3**: Provide time picker for reminder time
- **FR-2.2.2.4**: Provide snooze duration input (minutes)
- **FR-2.2.2.5**: Show interval input only for relevant frequencies
- **FR-2.2.2.6**: Show day-of-week selection only for weekly frequency
- **FR-2.2.2.7**: Show day-of-month input only for monthly frequency (specific date)
- **FR-2.2.2.8**: Show relative pattern selection for monthly frequency (ordinal + day of week)
- **FR-2.2.2.9**: Provide ordinal selection (first, second, third, fourth, last) for relative monthly patterns
- **FR-2.2.2.10**: Provide day-of-week selection for relative monthly patterns
- **FR-2.2.2.11**: Validate all inputs before saving

## 3. Non-Functional Requirements

### 3.1 Performance
- **NFR-3.1.1**: App shall launch within 2 seconds on target devices
- **NFR-3.1.2**: Reminder list shall load within 1 second
- **NFR-3.1.3**: Notifications shall trigger within 30 seconds of scheduled time
- **NFR-3.1.4**: App shall consume minimal battery when running in background

### 3.2 Reliability
- **NFR-3.2.1**: App shall have 99.9% uptime for notification delivery
- **NFR-3.2.2**: App shall handle unexpected shutdowns gracefully
- **NFR-3.2.3**: Notifications shall work even when app is not actively running
- **NFR-3.2.4**: Data integrity shall be maintained across app crashes

### 3.3 Usability
- **NFR-3.3.1**: Interface shall follow Material Design guidelines
- **NFR-3.3.2**: App shall be accessible to users with disabilities
- **NFR-3.3.3**: Text shall be readable with minimum 12sp font size
- **NFR-3.3.4**: Touch targets shall be minimum 48dp for easy interaction
- **NFR-3.3.5**: App shall provide clear visual feedback for user actions

### 3.4 Compatibility
- **NFR-3.4.1**: App shall support Android API level 24 (Android 7.0) and above
- **NFR-3.4.2**: App shall work on phones and tablets
- **NFR-3.4.3**: App shall respect system dark/light theme preferences
- **NFR-3.4.4**: App shall handle different screen sizes and orientations

### 3.5 Security & Privacy
- **NFR-3.5.1**: All data shall be stored locally on device
- **NFR-3.5.2**: No personal data shall be transmitted over internet
- **NFR-3.5.3**: App shall request minimal permissions required for functionality
- **NFR-3.5.4**: Notification content shall not be logged to system

### 3.6 Maintainability
- **NFR-3.6.1**: Code shall follow Kotlin coding standards
- **NFR-3.6.2**: App shall use modern Android architecture patterns
- **NFR-3.6.3**: Dependencies shall be kept up to date
- **NFR-3.6.4**: Code shall be documented with clear comments

## 4. Technical Constraints

### 4.1 Platform Constraints
- **TC-4.1.1**: Must be developed for Android platform only
- **TC-4.1.2**: Must use Kotlin as primary programming language
- **TC-4.1.3**: Must use Android Jetpack components where applicable
- **TC-4.1.4**: Must use local storage (SharedPreferences/Room) for data

### 4.2 System Constraints
- **TC-4.2.1**: Must respect Android's battery optimization settings
- **TC-4.2.2**: Must handle doze mode and app standby correctly
- **TC-4.2.3**: Must use appropriate notification channels for Android 8.0+
- **TC-4.2.4**: Must handle notification permission requests for Android 13+

## 5. Assumptions and Dependencies

### 5.1 Assumptions
- **A-5.1.1**: Users have basic familiarity with Android notifications
- **A-5.1.2**: Users understand concepts of recurring vs one-time reminders
- **A-5.1.3**: Device has reliable system clock for scheduling
- **A-5.1.4**: Users will grant necessary permissions for notifications

### 5.2 Dependencies
- **D-5.2.1**: Android AlarmManager for precise timing
- **D-5.2.2**: Android NotificationManager for displaying notifications
- **D-5.2.3**: Android WorkManager for reliable background processing
- **D-5.2.4**: Material Design Components for UI consistency

## 6. Future Enhancements (Out of Scope)

### 6.1 Potential Features
- Cloud synchronization across devices
- Reminder categories and tags
- Location-based reminders
- Voice input for creating reminders
- Integration with calendar apps
- Advanced recurring patterns
- Reminder templates
- Export/import functionality
- Backup and restore features
- Widget support

## 7. Success Criteria

### 7.1 Primary Goals
- **SC-7.1.1**: 95% of notifications delivered on time
- **SC-7.1.2**: Zero data loss incidents
- **SC-7.1.3**: App rated 4.0+ stars by users
- **SC-7.1.4**: Less than 1% crash rate

### 7.2 User Experience Goals
- **SC-7.2.1**: Users can create reminder in under 30 seconds
- **SC-7.2.2**: Users can understand all features without tutorial
- **SC-7.2.3**: 90% task completion rate for reminder management
- **SC-7.2.4**: Positive feedback on notification functionality

---

*Document Version: 1.0*  
*Last Updated: August 3, 2025*  
*Status: Draft*
