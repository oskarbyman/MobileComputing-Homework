package fi.dy.ose.mobilecomputing.ui.home.reminderCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.dy.ose.mobilecomputing.data.entity.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ReminderCardListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ReminderCardViewState())

    val state: StateFlow<ReminderCardViewState>
        get() = _state

    init {
        val list = mutableListOf<Reminder>()
        for (x in 1..20) {
            list.add(
                Reminder(
                    id = x.toLong(),
                    categoryId = x.toLong(),
                    title = "$x reminder",
                    note = "Remember to do $x",
                    date = LocalDateTime.now()
                )
            )
        }
        viewModelScope.launch {
            _state.value = ReminderCardViewState(
                reminders = list
            )
        }
    }
}

data class ReminderCardViewState(
    val reminders: List<Reminder> = emptyList()
)