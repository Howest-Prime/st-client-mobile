package be.howest.howestprime.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.usecase.GetCinemaLocationUseCase
import be.howest.howestprime.domain.usecase.GetLocationStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getCinemaLocation: GetCinemaLocationUseCase,
    private val getLocationStream: GetLocationStreamUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUIState())
    val uiState: StateFlow<MapUIState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(cinemaLocation = getCinemaLocation())
        }
    }

    fun startLocationUpdates() {
        viewModelScope.launch {
            getLocationStream().collect { point ->
                _uiState.update { it.copy(userLocation = point) }
            }
        }
    }
}
