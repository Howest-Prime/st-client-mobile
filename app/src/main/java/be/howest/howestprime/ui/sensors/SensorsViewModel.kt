package be.howest.howestprime.ui.sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.usecase.GetLuxStreamUseCase
import be.howest.howestprime.domain.usecase.IsSeatedUseCase
import be.howest.howestprime.domain.usecase.ToggleFlashlightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorsViewModel @Inject constructor(
    private val getLuxStream: GetLuxStreamUseCase,
    private val isSeated: IsSeatedUseCase,
    private val toggleFlash: ToggleFlashlightUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SensorsUIState())
    val uiState: StateFlow<SensorsUIState> = _uiState.asStateFlow()

    init {
        collectLux()
    }

    fun toggleFlashlight(enabled: Boolean) {
        viewModelScope.launch {
            try {
                toggleFlash(enabled)
                _uiState.update { it.copy(flashlightEnabled = enabled) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to toggle flashlight") }
            }
        }
    }

    private fun collectLux() {
        viewModelScope.launch {
            try {
                getLuxStream().collect { lux ->
                    _uiState.update {
                        it.copy(
                            luxValue = lux,
                            isSeated = isSeated(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to read sensor data") }
            }
        }
    }
}
