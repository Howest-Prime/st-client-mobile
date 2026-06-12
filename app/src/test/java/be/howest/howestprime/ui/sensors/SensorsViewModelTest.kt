package be.howest.howestprime.ui.sensors

import be.howest.howestprime.domain.usecase.GetLuxStreamUseCase
import be.howest.howestprime.domain.usecase.IsSeatedUseCase
import be.howest.howestprime.domain.usecase.StartListeningToLightSensorUseCase
import be.howest.howestprime.domain.usecase.StopListeningToLightSensorUseCase
import be.howest.howestprime.domain.usecase.ToggleFlashlightUseCase
import be.howest.howestprime.fake.FakeSensorRepository
import be.howest.howestprime.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SensorsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeSensorRepository
    private lateinit var viewModel: SensorsViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeSensorRepository()
        viewModel = SensorsViewModel(
            getLuxStream = GetLuxStreamUseCase(fakeRepository),
            startListening = StartListeningToLightSensorUseCase(fakeRepository),
            stopListening = StopListeningToLightSensorUseCase(fakeRepository),
            isSeated = IsSeatedUseCase(fakeRepository),
            toggleFlash = ToggleFlashlightUseCase(fakeRepository)
        )
    }

    @Test
    fun initial_lux_value_is_zero() {
        assertEquals(0f, viewModel.uiState.value.luxValue)
    }

    @Test
    fun initial_seated_status_is_false() {
        assertFalse(viewModel.uiState.value.isSeated)
    }

    @Test
    fun initial_flashlight_is_disabled() {
        assertFalse(viewModel.uiState.value.flashlightEnabled)
    }

    @Test
    fun initial_state_has_no_error() {
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun lux_value_updates_when_sensor_emits() = runTest {
        fakeRepository.setLux(450f)
        assertEquals(450f, viewModel.uiState.value.luxValue)
    }

    @Test
    fun seated_status_updates_when_repository_changes() = runTest {
        fakeRepository.setSeated(true)
        assertTrue(viewModel.uiState.value.isSeated)
    }

    @Test
    fun brightness_label_reflects_current_lux() = runTest {
        fakeRepository.setLux(25f)
        assertEquals("DARK", viewModel.uiState.value.brightnessLabel)
    }

    @Test
    fun brightness_label_is_NORMAL_for_mid_range_lux() = runTest {
        fakeRepository.setLux(300f)
        assertEquals("NORMAL", viewModel.uiState.value.brightnessLabel)
    }

    @Test
    fun onScreenVisible_calls_sensor_start() {
        viewModel.onScreenVisible()
        assertEquals(1, fakeRepository.startCallCount)
    }

    @Test
    fun onScreenHidden_calls_sensor_stop() {
        viewModel.onScreenHidden()
        assertEquals(1, fakeRepository.stopCallCount)
    }

    @Test
    fun toggleFlashlight_enables_flashlight_when_currently_off() = runTest {
        viewModel.toggleFlashlight()
        assertTrue(viewModel.uiState.value.flashlightEnabled)
        assertEquals(true, fakeRepository.lastFlashlightEnabled)
    }

    @Test
    fun toggleFlashlight_disables_flashlight_when_currently_on() = runTest {
        viewModel.toggleFlashlight()
        viewModel.toggleFlashlight()
        assertFalse(viewModel.uiState.value.flashlightEnabled)
        assertEquals(false, fakeRepository.lastFlashlightEnabled)
    }

    @Test
    fun toggleFlashlight_sets_error_message_on_failure() = runTest {
        fakeRepository.shouldThrowOnFlashlight = true
        viewModel.toggleFlashlight()
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun toggleFlashlight_does_not_change_state_on_failure() = runTest {
        fakeRepository.shouldThrowOnFlashlight = true
        viewModel.toggleFlashlight()
        assertFalse(viewModel.uiState.value.flashlightEnabled)
    }
}
