package be.howest.howestprime.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import be.howest.howestprime.R
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage

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
        uiState.cinemaLocation?.let { cinemaCenter ->
            setCameraOptions {
                center(cinemaCenter)
                zoom(16.0)
                pitch(0.0)
                bearing(0.0)
            }
        }
    }

    val cinemaMarker = rememberIconImage(R.drawable.ic_map_marker)

    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
    ) {
        MapEffect(Unit) { mapView ->
            onMapReady(mapView)
        }

        uiState.cinemaLocation?.let { cinemaPoint ->
            PointAnnotation(point = cinemaPoint) {
                iconImage = cinemaMarker
            }
        }
    }
}
