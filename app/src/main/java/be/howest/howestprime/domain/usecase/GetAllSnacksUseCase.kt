package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.repository.SnackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSnacksUseCase @Inject constructor(
    private val snackRepository: SnackRepository
) {
    operator fun invoke(): Flow<List<Snack>> {
        return snackRepository.getAllSnacks()
    }
}