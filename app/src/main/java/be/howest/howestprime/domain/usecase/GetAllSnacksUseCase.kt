package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.repository.SnackRepository
import javax.inject.Inject

class GetAllSnacksUseCase @Inject constructor(
     private val snackRepository: SnackRepository
) {
    suspend operator fun invoke(): List<Snack> {
        return snackRepository.getAllSnacks()
    }
}