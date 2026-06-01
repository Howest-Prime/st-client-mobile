package be.howest.howestprime.ui.tickets

import be.howest.howestprime.domain.model.Ticket

data class TicketsUIState(
    val tickets: List<Ticket> = emptyList(),
    val showUpcomingOnly: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
