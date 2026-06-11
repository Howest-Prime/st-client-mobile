package be.howest.howestprime.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.usecase.GetTicketByIdUseCase
import be.howest.howestprime.domain.usecase.ScheduleMovieReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    private val getTicketById: GetTicketByIdUseCase,
    private val scheduleMovieReminder: ScheduleMovieReminderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketDetailUIState())
    val uiState: StateFlow<TicketDetailUIState> = _uiState.asStateFlow()

    private var pendingReminder: Pair<Ticket, Int>? = null

    fun loadTicket(ticketId: String?) {
        try {
            viewModelScope.launch {
                getTicketById(ticketId).collect { ticket ->
                    _uiState.update { currentState ->
                        if (currentState.ticket?.id == ticket?.id) {
                            currentState.copy(ticket = ticket)
                        } else {
                            currentState.copy(ticket = ticket, errorMessage = null)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Ticket could not be loaded") }
        }
    }

    fun scheduleReminder(ticket: Ticket, minutesBefore: Int) {
        scheduleMovieReminder(ticket, minutesBefore)
    }

    fun setPendingReminder(ticket: Ticket, minutesBefore: Int) {
        pendingReminder = ticket to minutesBefore
    }

    fun schedulePendingReminder() {
        pendingReminder?.let { (ticket, minutesBefore) ->
            scheduleMovieReminder(ticket, minutesBefore)
            pendingReminder = null
        }
    }
}
