package fi.dy.ose.core_worker.NotificationHelper

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import fi.dy.ose.core_worker.R

class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "reminder_id"
    private val NOTIFICATION_ID = 1

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = "ReminderChannel"
        val descriptionText = "Reminder channel for Reminderoo notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun notifyUserOfReminder(title: String, reminderTime: String, message: String) {

        createNotificationChannel()

        val intent = Intent()
        intent.setClassName(context, "fi.dy.ose.mobilecomputing.ui.MainActivity")
            .apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("reminder_title", title)
            putExtra("reminder_message", message)
        }


        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(
            context,
            "reminder_id"
        )
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Reminder for $title at $reminderTime")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(
                NOTIFICATION_ID, builder.build()
            )
        }
    }

}