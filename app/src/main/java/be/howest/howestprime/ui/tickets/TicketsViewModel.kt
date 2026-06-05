package be.howest.howestprime.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.usecase.GetAllTicketsUseCase
import be.howest.howestprime.domain.usecase.GetUpcomingTicketsUseCase
import be.howest.howestprime.domain.usecase.ScheduleMovieReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val getAllTickets: GetAllTicketsUseCase,
    private val getUpcomingTickets: GetUpcomingTicketsUseCase,
    private val scheduleMovieReminder: ScheduleMovieReminderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsUIState())
    val uiState: StateFlow<TicketsUIState> = _uiState.asStateFlow()

    init {
        loadAllTickets()
    }

    fun toggleUpcomingOnly() {
        val enabled = !_uiState.value.showUpcomingOnly
        viewModelScope.launch {
            val flow = if (enabled) getUpcomingTickets() else getAllTickets()
            flow.collect { tickets ->
                _uiState.update { it.copy(tickets = tickets, showUpcomingOnly = enabled) }
            }
        }
    }

    fun scheduleReminder(ticket: Ticket, minutesBefore: Int) {
        scheduleMovieReminder(ticket, minutesBefore)
    }

    private fun loadAllTickets() {
        viewModelScope.launch {
            getAllTickets().collect { tickets ->
                _uiState.update { it.copy(tickets = tickets, isLoading = false) }
            }
        }
    }
}
