package be.howest.howestprime.ui.sensors

import be.howest.howestprime.domain.model.BrightnessState

data class SensorsUIState(
    val luxValue: Float = 0f,
    val isSeated: Boolean = false,
    val flashlightEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val brightnessLabel: String = BrightnessState(luxValue).status.toString()
}
