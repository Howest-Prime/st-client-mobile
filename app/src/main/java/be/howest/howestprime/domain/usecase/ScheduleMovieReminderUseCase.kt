package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.repository.TicketRepository
import javax.inject.Inject

class ScheduleMovieReminderUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    operator fun invoke(ticket: Ticket, minutesBefore: Int) =
        ticketRepository.scheduleReminder(ticket, minutesBefore)
}
