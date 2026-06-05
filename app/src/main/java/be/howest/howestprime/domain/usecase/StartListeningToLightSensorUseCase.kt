package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.SensorRepository
import javax.inject.Inject

class StartListeningToLightSensorUseCase @Inject constructor(
    private val sensorRepository: SensorRepository
) {
    operator fun invoke() = sensorRepository.start()
}