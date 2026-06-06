package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.LocationRepository
import com.mapbox.maps.MapView
import javax.inject.Inject

class AttachMapViewUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(mapView: MapView) {
        locationRepository.attachMapView(mapView)
    }
}
