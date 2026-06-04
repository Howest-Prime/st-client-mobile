package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.LocationRepository
import com.mapbox.geojson.Point
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Point? = locationRepository.getUserLocation()
}