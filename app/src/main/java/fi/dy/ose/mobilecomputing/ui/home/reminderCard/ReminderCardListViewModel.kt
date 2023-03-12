package fi.dy.ose.mobilecomputing.ui.home.reminderCard

import android.content.ContentValues.TAG
import android.location.Location
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.mobilecomputing.data.entity.Category
import fi.dy.ose.mobilecomputing.ui.home.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sqrt

@HiltViewModel
class ReminderCardListViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReminderCardViewState())

    val state: StateFlow<ReminderCardViewState>
        get() = _state

    private suspend fun _reloadReminders(seen: Boolean, all: Boolean, location: Location?) {
        val list: List<Reminder> = if (all) {
            reminderRepository.loadReminders()
        } else if (seen) {
            reminderRepository.loadSeenReminders(seen)
        } else {
            reminderRepository.loadSeenReminders(seen)
        }
        val filteredList = if (location != null) {
            list.filter { reminder ->
                reminder.location_y != null && reminder.location_x != null &&
                        isWithinRadius(reminder.location_x, reminder.location_y, location, 100.0f)
            }
        } else if (all) {
            list
        } else {
            list.filter { reminder ->
                reminder.location_y == null && reminder.location_x == null
            }
        }
        val listButSorted: List<Reminder> = filteredList.sortedByDescending { it.reminder_time }
        _state.value = ReminderCardViewState(
            reminders = listButSorted,
            tabs = listOf("Occurred", "Upcoming", "All")
        )
    }

    private fun isWithinRadius(reminderLatitude: Float?, reminderLongitude: Float?, location: Location, radius: Float): Boolean {
        if (reminderLatitude == null || reminderLongitude == null) {
            return false
        }
        val reminderLocation = Location("")
        reminderLocation.latitude = reminderLatitude.toDouble()
        reminderLocation.longitude = reminderLongitude.toDouble()
        val distanceInMeters = location.distanceTo(reminderLocation)
        Log.d(TAG, "Distance between points ($reminderLatitude,$reminderLongitude) and (${location.latitude},${location.longitude}) is ${distanceInMeters} = ${distanceInMeters <= radius}")
        return distanceInMeters <= radius
    }

    private fun distanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0 // meters
        val x = (lon2 - lon1) * cos((lat1 + lat2) / 2)
        val y = lat2 - lat1
        return earthRadius * sqrt(x * x + y * y)
    }

    fun reloadReminders(tabName: String, location: Location?) {
        var seen: Boolean = true
        var all: Boolean = false
        if (tabName == "All") {
            all = true
        } else if ( tabName == "Occurred" ) {
            seen = true
        } else if ( tabName == "Upcoming" ) {
            seen = false
        }
        viewModelScope.launch {
            _reloadReminders(seen, all, location)
        }
    }

    fun deleteReminder(reminderId: Long, tabName: String, location: Location?) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminderId)
            reloadReminders(tabName, location)
        }
    }

    fun setReminderAsSeen(reminderId: Long) {
        viewModelScope.launch {
            reminderRepository.setReminderSeen(reminderId, true)
        }
    }

    init {
        reloadReminders("Upcoming", null)
    }
}

data class ReminderCardViewState(
    val reminders: List<Reminder> = emptyList(),
    val tabs: List<String> = emptyList()
)