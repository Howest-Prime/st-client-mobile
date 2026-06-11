package be.howest.howestprime.ui.tickets

import be.howest.howestprime.domain.model.MovieReminderOption
import be.howest.howestprime.domain.model.Ticket

data class TicketsUIState(
    val tickets: List<Ticket> = emptyList(),
    val reminderOptions: List<MovieReminderOption> = MovieReminderOption.entries,
    val showUpcomingOnly: Boolean = false,
    val errorMessage: String? = null
)
