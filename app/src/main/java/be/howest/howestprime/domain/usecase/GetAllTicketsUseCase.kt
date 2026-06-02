package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.repository.TicketRepository
import javax.inject.Inject

class GetAllTicketsUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(): List<Ticket> {
        return ticketRepository.getAllTickets()
    }
}