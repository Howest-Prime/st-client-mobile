package be.howest.howestprime.ui.tickets

import be.howest.howestprime.domain.model.MovieReminderOption
import be.howest.howestprime.domain.model.Ticket

data class TicketDetailUIState(
    val ticket: Ticket? = null,
    val reminderOptions: List<MovieReminderOption> = MovieReminderOption.entries,
    val errorMessage: String? = null
)
