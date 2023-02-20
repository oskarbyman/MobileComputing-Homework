package fi.dy.ose.mobilecomputing.ui.home.reminderCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.code.domain.entity.Reminder
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

    private suspend fun _reloadReminders() {
        val list: List<Reminder> = reminderRepository.loadReminders()
        val listButSorted: List<Reminder> = list.sortedByDescending { it.reminder_time }
        _state.value = ReminderCardViewState(
            reminders = listButSorted
        )

    }

    fun reloadReminders() {
        viewModelScope.launch {
            _reloadReminders()
        }
    }

    fun deleteReminder(reminderId: Long) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminderId)
            _reloadReminders()
        }
    }

    init {
        reloadReminders()
    }
}

data class ReminderCardViewState(
    val reminders: List<Reminder> = emptyList()
)