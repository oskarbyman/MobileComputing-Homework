package fi.dy.ose.mobilecomputing.ui.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.core_worker.NotificationHelper.NotificationHelper
import fi.dy.ose.core_worker.ReminderWorker.ReminderWorker
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
    private val geofencingClient = LocationServices.getGeofencingClient(Graph.appContext)

    fun saveReminder(
        navController: NavController,
        reminder_id: Long?,
        reminder: Reminder,
        useTime: Boolean
    ) {

        if (reminder_id != null) {
            reminder.reminderId = reminder_id
        }

        viewModelScope.launch {
            val id = reminderRepository.addReminder(reminder)

            if (reminder.reminder_time.isBefore(LocalDateTime.now())) {
                reminderRepository.setReminderSeen(id, true)
            }
            setReminder(reminder, useTime)
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

    private fun setReminder(reminder: Reminder, useTime: Boolean) {
        if (reminder.location_x != null && reminder.location_y != null) {
            _setLocationReminder(reminder)
        }
        if (useTime) {
            _setTimeReminder(reminder)
        }

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun _setLocationReminder(reminder: Reminder) {
        val geofence = Geofence.Builder()
            .setRequestId(reminder.title)
            .setCircularRegion(reminder.location_x!!.toDouble(), reminder.location_y!!.toDouble(), 100.0f)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(2)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setNotificationResponsiveness(0)
            .build()
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_DWELL)
            .addGeofence(geofence)
            .build()
        val intent = Intent(Graph.appContext, GeofenceBroadcastReceiver::class.java).apply{
                putExtra("reminder_title", reminder.title)
                putExtra("reminder_message", reminder.message)
            }

        val geofencePendingIntent = PendingIntent.getBroadcast(
            Graph.appContext,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if (ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Log.d(TAG, "${reminder.location_x!!.toDouble()}, ${reminder.location_y!!.toDouble()}, ${100.0f}")
        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                Toast.makeText(Graph.appContext, "Reminder: ${reminder.title} set with a geofence", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Log.e(TAG, "Failed to add geofence: ${it.localizedMessage}")
            }
        }

    }

    private fun _setTimeReminder(reminder: Reminder) {
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

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }
        val reminderTitle = intent!!.getStringExtra("reminder_title")
        val reminderMessage = intent.getStringExtra("reminder_message")
        Log.e(TAG, "Got the following values: $reminderTitle and $reminderMessage")
        if (reminderTitle != null && reminderMessage != null) {
            val notificationHelper = NotificationHelper(Graph.appContext)

            notificationHelper.notifyUserOfReminder(
                reminderTitle,
                "your current location",
                reminderMessage
            )
            val geofencingClient = LocationServices.getGeofencingClient(Graph.appContext)
            geofencingClient.removeGeofences(listOf(reminderTitle)).run {
                addOnSuccessListener {
                    Log.d(TAG, "Geofence Removed for $reminderTitle")
                }
                addOnFailureListener {
                    Log.e(TAG, "Failed to remove geofence for $reminderTitle: ${it.localizedMessage}")
                }
            }
        }

    }
}