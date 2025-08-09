# Copilot Instructions for SimpleReminders

Purpose: guide assistants working in this repo. The developer will build and run the app in Android Studio. Do not build from here.

## Do not run builds or Gradle

- Never invoke Gradle or Android build tasks from this workspace.
- Do not call tasks like "Build Android App" or run gradlew in a terminal.
- When verification is needed, provide steps for Android Studio instead of executing commands.

## What you should do

- Read and edit code and resources with minimal diffs; preserve style and public APIs.
- Add/update Android components (activities, receivers, services) and manifest entries as needed.
- Provide optional, copyable commands in fenced blocks only when documenting, and mark them as optional. Do not execute them.
- When asked to “test”, explain how to run the test in Android Studio and what to look for.

## Notification reliability policy (project-specific)

This app uses AlarmManager and broadcast receivers for reminders. Ensure changes follow these:

- Use AlarmManager.setExactAndAllowWhileIdle for precise triggers on API ≥ 23; fallback to setExact otherwise.
- PendingIntent MUST include FLAG_IMMUTABLE and FLAG_UPDATE_CURRENT where relevant.
- Manifest must include:
  - POST_NOTIFICATIONS (Android 13+ runtime permission handled in code)
  - SCHEDULE_EXACT_ALARM/USE_EXACT_ALARM (Android 12+ exact alarms)
  - RECEIVE_BOOT_COMPLETED (to reschedule after reboot)
  - REQUEST_IGNORE_BATTERY_OPTIMIZATIONS (to show system prompt)
- Receivers:
  - ReminderBroadcastReceiver for firing notifications
  - NotificationActionReceiver for actions (snooze/mark done)
  - BootReceiver (exported=true) listening to BOOT_COMPLETED, MY_PACKAGE_REPLACED, TIME_SET, TIMEZONE_CHANGED, reschedules all active reminders
- UX prompts:
  - On resume, prompt users to allow exact alarms (Android 12+) and to ignore battery optimizations (Android 6+). Handle exceptions gracefully.
- Settings:
  - Include a manual "Reschedule all reminders" action that calls ReminderManager.rescheduleAllActive().

## When asked to verify functionality

- Provide Android Studio steps (Build > Make Project, Run on device/emulator, grant permissions, lock screen, wait for reminder).
- Suggest practical checks: notification channels enabled, app not battery-optimized, exact alarms allowed, BOOT_COMPLETED delivered.
- Offer adb or system-setting pointers as optional, but do not run here.

## Coding conventions

- Kotlin target: JVM 11; minSdk 24; targetSdk 36.
- Keep changes focused; avoid large refactors unless requested.
- Add small, safe improvements (null checks, logs) when useful. Document them succinctly.

## Security and privacy

- Do not add network calls or external analytics.
- Do not exfiltrate secrets; none should be present.

## If something cannot be done without a build

- State the limitation briefly and provide accurate Android Studio steps for the developer to perform.

## Quick Android Studio checklist (for the human developer)

- Open project, let Gradle sync.
- Run on device/emulator (API 24+). On first run:
  - Grant notification permission (Android 13+)
  - Grant exact alarms (Android 12+)
  - Allow ignoring battery optimizations (Android 6+)
- Create a reminder a few minutes ahead; lock screen; observe notification.
- Reboot or change timezone/time; reminders should still fire (BootReceiver reschedules).
- Use Settings > "Reschedule all reminders" if schedules were lost.
