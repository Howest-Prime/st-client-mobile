package be.howest.howestprime.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.usecase.AttachMapViewUseCase
import be.howest.howestprime.domain.usecase.CheckInsideBuildingUseCase
import be.howest.howestprime.domain.usecase.DetachMapViewUseCase
import be.howest.howestprime.domain.usecase.GetCinemaPolygonUseCase
import be.howest.howestprime.domain.usecase.GetLocationStreamUseCase
import com.mapbox.maps.MapView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getCinemaPolygon: GetCinemaPolygonUseCase,
    private val getLocationStream: GetLocationStreamUseCase,
    private val checkInsideBuilding: CheckInsideBuildingUseCase,
    private val attachMapView: AttachMapViewUseCase,
    private val detachMapView: DetachMapViewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUIState())
    val uiState: StateFlow<MapUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val polygon = getCinemaPolygon()
            _uiState.update { it.copy(cinemaPolygon = polygon) }
        }
    }

    fun startLocationUpdates() {
        viewModelScope.launch {
            getLocationStream().collect { point ->
                val inside = withContext(Dispatchers.Default) { checkInsideBuilding(point) }
                _uiState.update { it.copy(isInsideBuilding = inside) }
            }
        }
    }

    fun onMapReady(mapView: MapView) {
        attachMapView(mapView)
    }

    fun onMapDisposed() {
        detachMapView()
    }
}
