package be.howest.howestprime.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotation

@Composable
fun MapScreen(
    uiState: MapUIState,
    onRequestLocationPermission: () -> Unit,
    onMapReady: (MapView) -> Unit,
    onMapDisposed: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onRequestLocationPermission()
    }

    DisposableEffect(Unit) {
        onDispose { onMapDisposed() }
    }

    val mapViewportState = rememberMapViewportState {
        uiState.cinemaPolygon?.let { pts ->
            val lng = pts.map { it.longitude() }.average()
            val lat = pts.map { it.latitude() }.average()
            setCameraOptions {
                center(Point.fromLngLat(lng, lat))
                zoom(16.0)
                pitch(0.0)
                bearing(0.0)
            }
        }
    }

    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
    ) {
        MapEffect(Unit) { mapView ->
            onMapReady(mapView)
        }

        uiState.cinemaPolygon?.let { ring ->
            val polygonColor = if (uiState.isInsideBuilding) Color(0xFF4CAF50.toInt()) else Color(0xFFF44336.toInt())
            PolygonAnnotation(points = listOf(ring)) {
                fillColor = polygonColor
                fillOpacity = 0.35
            }
        }
    }
}
