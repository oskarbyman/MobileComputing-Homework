package fi.dy.ose.mobilecomputing.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.mobilecomputing.R
import fi.dy.ose.mobilecomputing.ui.Graph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel(){
    private var _loaded: Boolean

    fun saveReminder(navController: NavController, reminder_id: Long?, reminder: Reminder) {

        if (reminder_id != null) {
            reminder.reminderId = reminder_id
        }

        viewModelScope.launch {
            reminderRepository.addReminder(reminder).also {
                notifyUserOfReminder(reminder)
            }
            delay(100)
            navController.popBackStack()
        }
    }

    fun loadReminder(reminderId: Long): Reminder? {
        if (!_loaded) {
            _loaded = true
            return reminderRepository.loadReminder(reminderId)
        }
        return null
    }

    private fun notifyUserOfReminder(reminder: Reminder) {
        val notificationId = 10
        val builder = NotificationCompat.Builder(
            Graph.appContext,
            "reminders_id"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New reminder added")
            .setContentText("You set a reminder for ${reminder.title} for ${reminder.reminder_time}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(from(Graph.appContext)) {
            notify(
                notificationId, builder.build()
            )
        }
    }
    private fun createNotificationChannel() {
        val name = "ReminderChannel"
        val descriptionText = "Reminder channel for Reminderoo notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("reminders_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = Graph.appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    init {
        createNotificationChannel()
        _loaded = false
    }
}