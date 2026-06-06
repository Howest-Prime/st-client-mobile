package be.howest.howestprime.data.repository

import android.content.Context
import be.howest.howestprime.domain.repository.LocationRepository
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationRepository {

    private val _location = MutableStateFlow<Point?>(null)
    override val location: Flow<Point> = _location.filterNotNull()

    private val featureCollection: FeatureCollection by lazy {
        context.assets.open("howestprime.geojson")
            .bufferedReader()
            .use { it.readText() }
            .let { FeatureCollection.fromJson(it) }
    }

    private var listener: OnIndicatorPositionChangedListener? = null
    private var mapView: MapView? = null

    override fun getCinemaLocation(): Point? {
        val feature = featureCollection.features()?.firstOrNull() ?: return null
        return when (val geometry = feature.geometry()) {
            is Point -> geometry
            is Polygon -> {
                val ring = geometry.coordinates().firstOrNull() ?: return null
                val lng = ring.map { it.longitude() }.average()
                val lat = ring.map { it.latitude() }.average()
                Point.fromLngLat(lng, lat)
            }
            else -> null
        }
    }

    override fun attachMapView(mapView: MapView) {
        this.mapView = mapView

        mapView.location.updateSettings {
            enabled = true
            locationPuck = createDefault2DPuck()
        }

        listener = OnIndicatorPositionChangedListener { point ->
            _location.value = point
        }

        mapView.location.addOnIndicatorPositionChangedListener(listener!!)
    }

    override fun detachMapView() {
        mapView?.let { mv ->
            listener?.let {
                mv.location.removeOnIndicatorPositionChangedListener(it)
            }
            mv.location.updateSettings { enabled = false }
        }
        listener = null
        mapView = null
    }
}
