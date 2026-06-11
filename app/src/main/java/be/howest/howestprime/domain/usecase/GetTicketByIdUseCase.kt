package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.repository.TicketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTicketByIdUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    operator fun invoke(id: String): Flow<Ticket?> {
        return ticketRepository.getTicketById(id)
    }
}
