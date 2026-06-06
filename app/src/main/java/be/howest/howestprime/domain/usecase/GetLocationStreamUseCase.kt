package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationStreamUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke() = locationRepository.location
}
