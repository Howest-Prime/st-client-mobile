package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsSeatedUseCase @Inject constructor(
    private val sensorRepository: SensorRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return sensorRepository.isSeated
    }
}
