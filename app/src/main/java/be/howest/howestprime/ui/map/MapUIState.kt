package be.howest.howestprime.ui.map

import com.mapbox.geojson.Point

data class MapUIState(
    val cinemaPolygon: List<Point>? = null,
    val isInsideBuilding: Boolean = false
)
