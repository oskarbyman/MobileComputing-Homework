package fi.dy.ose.core_worker.ReminderWorker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.core_worker.NotificationHelper.NotificationHelper

// Source: https://dev.to/blazebrain/building-a-reminder-app-with-local-notifications-using-workmanager-api-385f
class ReminderWorker(val context: Context, val params: WorkerParameters) : Worker(context, params){

    override fun doWork(): Result {
        NotificationHelper(context).notifyUserOfReminder(
            inputData.getString("title").toString(),
            inputData.getString("reminderTime").toString(),
            inputData.getString("message").toString()
        )

        return Result.success()
    }
}