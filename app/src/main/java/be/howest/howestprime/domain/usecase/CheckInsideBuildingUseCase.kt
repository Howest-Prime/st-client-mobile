package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.LocationRepository
import com.mapbox.geojson.Point
import javax.inject.Inject

class CheckInsideBuildingUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(point: Point): Boolean {
        val ring = locationRepository.getCinemaPolygon() ?: return false
        val x = point.longitude()
        val y = point.latitude()
        var inside = false
        var j = ring.size - 1
        for (i in ring.indices) {
            val xi = ring[i].longitude(); val yi = ring[i].latitude()
            val xj = ring[j].longitude(); val yj = ring[j].latitude()
            if ((yi > y) != (yj > y) && x < (xj - xi) * (y - yi) / (yj - yi) + xi) inside = !inside
            j = i
        }
        return inside
    }
}
