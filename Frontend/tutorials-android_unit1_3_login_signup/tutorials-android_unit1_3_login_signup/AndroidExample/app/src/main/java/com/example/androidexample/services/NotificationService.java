package com.example.androidexample.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.androidexample.R;
import com.example.androidexample.main_five_pages.WorkoutActivity;

public class NotificationService {
    private static final String TAG = "NotificationService";

    // Notification Channels
    private static final String WORKOUT_CHANNEL_ID = "workout_notifications";
    private static final String PROGRESS_CHANNEL_ID = "progress_notifications";
    private static final String REMINDER_CHANNEL_ID = "reminder_notifications";

    // Notification IDs
    private static final int WORKOUT_NOTIFICATION_ID = 1001;
    private static final int PROGRESS_NOTIFICATION_ID = 1002;
    private static final int REMINDER_NOTIFICATION_ID = 1003;

    private final Context context;
    private final NotificationManagerCompat notificationManager;

    public NotificationService(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Workout Channel
            NotificationChannel workoutChannel = new NotificationChannel(
                    WORKOUT_CHANNEL_ID,
                    "Workout Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            workoutChannel.setDescription("Notifications about workout completion and tracking");
            workoutChannel.enableLights(true);
            workoutChannel.setLightColor(Color.BLUE);
            workoutChannel.setShowBadge(true);

            // Progress Channel
            NotificationChannel progressChannel = new NotificationChannel(
                    PROGRESS_CHANNEL_ID,
                    "Progress Updates",
                    NotificationManager.IMPORTANCE_HIGH
            );
            progressChannel.setDescription("Notifications about achieving fitness goals and milestones");
            progressChannel.enableLights(true);
            progressChannel.setLightColor(Color.GREEN);
            progressChannel.enableVibration(true);
            progressChannel.setShowBadge(true);

            // Reminder Channel
            NotificationChannel reminderChannel = new NotificationChannel(
                    REMINDER_CHANNEL_ID,
                    "Workout Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            reminderChannel.setDescription("Reminders for scheduled workouts and consistency");
            reminderChannel.enableLights(true);
            reminderChannel.setLightColor(Color.YELLOW);
            reminderChannel.setShowBadge(false);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(workoutChannel);
            manager.createNotificationChannel(progressChannel);
            manager.createNotificationChannel(reminderChannel);
        }
    }

    public void showWorkoutComplete(String workoutName, int exerciseCount) {
        Intent intent = new Intent(context, WorkoutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, WORKOUT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_workout_complete)
                .setContentTitle("Workout Complete!")
                .setContentText(String.format("Great job completing %s with %d exercises!", workoutName, exerciseCount))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        notify(WORKOUT_NOTIFICATION_ID, builder.build());
    }

    public void showProgressUpdate(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PROGRESS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_achievement)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 250, 250, 250});

        notify(PROGRESS_NOTIFICATION_ID, builder.build());
    }

    public void showWorkoutReminder(String title, String message) {
        Intent intent = new Intent(context, WorkoutActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_reminder)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notify(REMINDER_NOTIFICATION_ID, builder.build());
    }

    public void showWorkoutStreak(int streakDays) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PROGRESS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_streak)
                .setContentTitle("Workout Streak!")
                .setContentText(String.format("Amazing! You've worked out %d days in a row!", streakDays))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 250, 250, 250});

        notify(PROGRESS_NOTIFICATION_ID + 1, builder.build());
    }

    public void showPersonalRecord(String exerciseName, String achievement) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PROGRESS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_trophy)
                .setContentTitle("New Personal Record!")
                .setContentText(String.format("New PR on %s: %s", exerciseName, achievement))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 250, 250, 250});

        notify(PROGRESS_NOTIFICATION_ID + 2, builder.build());
    }

    private void notify(int id, android.app.Notification notification) {
        try {
            notificationManager.notify(id, notification);
        } catch (SecurityException e) {
            // Handle notification permission not granted
            Log.e(TAG, "Notification permission not granted: " + e.getMessage());
        }
    }

    // Utility method to cancel notifications
    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }

    // Cancel all notifications
    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }
}