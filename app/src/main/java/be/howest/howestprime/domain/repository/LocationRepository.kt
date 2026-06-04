package be.howest.howestprime.domain.repository

import com.mapbox.geojson.Point

interface LocationRepository {
    suspend fun getUserLocation(): Point?
}