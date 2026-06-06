package be.howest.howestprime.ui.map

import com.mapbox.geojson.Point

data class MapUIState(
    val cinemaLocation: Point? = null,
    val userLocation: Point? = null
)
