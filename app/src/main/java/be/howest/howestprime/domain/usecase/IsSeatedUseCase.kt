package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.SensorRepository
import javax.inject.Inject

class IsSeatedUseCase @Inject constructor(
    private val sensorRepository: SensorRepository
) {
    suspend operator fun invoke(): Boolean {
        return sensorRepository.isSeated()
    }
}
