package be.howest.howestprime.domain.usecase

import be.howest.howestprime.fake.FakeSensorRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SensorUseCasesTest {

    private lateinit var fakeRepository: FakeSensorRepository

    @Before
    fun setUp() {
        fakeRepository = FakeSensorRepository()
    }

    // GetLuxStreamUseCase

    @Test
    fun getLuxStream_returns_current_lux_value() = runTest {
        fakeRepository.setLux(350f)
        val useCase = GetLuxStreamUseCase(fakeRepository)
        val result = useCase().first()
        assertEquals(350f, result)
    }

    @Test
    fun getLuxStream_returns_zero_initially() = runTest {
        val useCase = GetLuxStreamUseCase(fakeRepository)
        val result = useCase().first()
        assertEquals(0f, result)
    }

    // IsSeatedUseCase

    @Test
    fun isSeated_returns_false_initially() = runTest {
        val useCase = IsSeatedUseCase(fakeRepository)
        assertFalse(useCase().first())
    }

    @Test
    fun isSeated_returns_true_when_seated() = runTest {
        fakeRepository.setSeated(true)
        val useCase = IsSeatedUseCase(fakeRepository)
        assertTrue(useCase().first())
    }

    // ToggleFlashlightUseCase

    @Test
    fun toggleFlashlight_passes_enabled_true_to_repository() = runTest {
        val useCase = ToggleFlashlightUseCase(fakeRepository)
        useCase(true)
        assertEquals(true, fakeRepository.lastFlashlightEnabled)
    }

    @Test
    fun toggleFlashlight_passes_enabled_false_to_repository() = runTest {
        val useCase = ToggleFlashlightUseCase(fakeRepository)
        useCase(false)
        assertEquals(false, fakeRepository.lastFlashlightEnabled)
    }

    // StartListeningToLightSensorUseCase

    @Test
    fun startListening_calls_repository_start() {
        val useCase = StartListeningToLightSensorUseCase(fakeRepository)
        useCase()
        assertEquals(1, fakeRepository.startCallCount)
    }

    @Test
    fun startListening_can_be_called_multiple_times() {
        val useCase = StartListeningToLightSensorUseCase(fakeRepository)
        useCase()
        useCase()
        assertEquals(2, fakeRepository.startCallCount)
    }

    // StopListeningToLightSensorUseCase

    @Test
    fun stopListening_calls_repository_stop() {
        val useCase = StopListeningToLightSensorUseCase(fakeRepository)
        useCase()
        assertEquals(1, fakeRepository.stopCallCount)
    }

    @Test
    fun stopListening_can_be_called_multiple_times() {
        val useCase = StopListeningToLightSensorUseCase(fakeRepository)
        useCase()
        useCase()
        assertEquals(2, fakeRepository.stopCallCount)
    }
}
