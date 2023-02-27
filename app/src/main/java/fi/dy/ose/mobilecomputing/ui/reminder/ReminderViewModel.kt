package fi.dy.ose.mobilecomputing.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.core_worker.ReminderWorker.ReminderWorker
import fi.dy.ose.mobilecomputing.R
import fi.dy.ose.mobilecomputing.ui.Graph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
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
            val id = reminderRepository.addReminder(reminder)

            if (reminder.reminder_time.isBefore(LocalDateTime.now())) {
                reminderRepository.setReminderSeen(id, true)
            }
            setReminder(reminder)
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

    private fun setReminder(reminder: Reminder) {
        val timeZoneId = ZoneId.systemDefault()
        val timeNow = Calendar.getInstance()
        val reminderDate = Date.from(reminder.reminder_time.atZone(timeZoneId).toInstant())
        val reminderTime = Calendar.getInstance()
        reminderTime.time = reminderDate
        val reminderDelay = reminderTime.timeInMillis/1000L - timeNow.timeInMillis/1000L
        val reminderWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(reminderDelay, TimeUnit.SECONDS)
            .setInputData(workDataOf(
                "title" to reminder.title,
                "reminderTime" to reminder.reminder_time.toString(),
                "message" to reminder.message
            )).build()

        WorkManager.getInstance(Graph.appContext).enqueue(reminderWorkRequest)
        Toast.makeText(Graph.appContext, "Reminder: ${reminder.title} set, which will occur in $reminderDelay seconds", Toast.LENGTH_SHORT).show()
    }

    init {
        _loaded = false
    }
}