package be.howest.howestprime.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.usecase.GetAllTicketsUseCase
import be.howest.howestprime.domain.usecase.GetUpcomingTicketsUseCase
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
    private val getUpcomingTickets: GetUpcomingTicketsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsUIState())
    val uiState: StateFlow<TicketsUIState> = _uiState.asStateFlow()

    init {
        loadAllTickets()
    }

    suspend fun toggleUpcomingOnly(enabled: Boolean) {
        val tickets = if (enabled) getUpcomingTickets() else getAllTickets()
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tickets = tickets,
                    showUpcomingOnly = enabled
                )
            }
        }
    }

    private fun loadAllTickets(){
        viewModelScope.launch {
            try {
                val tickets = getAllTickets()
                _uiState.update {
                    it.copy(tickets = tickets, isLoading = false, showUpcomingOnly = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Failed to load tickets")
                }
            }
        }
    }
}
