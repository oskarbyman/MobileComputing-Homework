package fi.dy.ose.mobilecomputing.ui.home.reminderCard

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

@HiltViewModel
class ReminderCardListViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReminderCardViewState())

    val state: StateFlow<ReminderCardViewState>
        get() = _state

    private suspend fun _reloadReminders(seen: Boolean, all: Boolean) {
        var list: List<Reminder> = emptyList()
        if (all) {
            list = reminderRepository.loadReminders()
        } else if (seen) {
            list = reminderRepository.loadSeenReminders(seen)
        } else {
            list = reminderRepository.loadSeenReminders(seen)
        }
        val listButSorted: List<Reminder> = list.sortedByDescending { it.reminder_time }
        _state.value = ReminderCardViewState(
            reminders = listButSorted,
            tabs = listOf("Occurred", "Upcoming", "All")
        )
    }

    fun reloadReminders(tabName: String) {
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
            _reloadReminders(seen, all)
        }
    }

    fun deleteReminder(reminderId: Long, tabName: String) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminderId)
            reloadReminders(tabName)
        }
    }

    fun setReminderAsSeen(reminderId: Long) {
        viewModelScope.launch {
            reminderRepository.setReminderSeen(reminderId, true)
        }
    }

    init {
        reloadReminders("Upcoming")
    }
}

data class ReminderCardViewState(
    val reminders: List<Reminder> = emptyList(),
    val tabs: List<String> = emptyList()
)