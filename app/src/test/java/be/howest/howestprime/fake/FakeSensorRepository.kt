package be.howest.howestprime.fake

import be.howest.howestprime.domain.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSensorRepository : SensorRepository {

    private val _lux = MutableStateFlow(0f)
    private val _isSeated = MutableStateFlow(false)

    var startCallCount = 0
    var stopCallCount = 0
    var lastFlashlightEnabled: Boolean? = null
    var shouldThrowOnFlashlight = false

    fun setLux(lux: Float) {
        _lux.value = lux
    }

    fun setSeated(seated: Boolean) {
        _isSeated.value = seated
    }

    override val luxFlow: Flow<Float> = _lux.asStateFlow()

    override val isSeated: Flow<Boolean> = _isSeated.asStateFlow()

    override fun start() {
        startCallCount++
    }

    override fun stop() {
        stopCallCount++
    }

    override suspend fun toggleFlashlight(enabled: Boolean) {
        if (shouldThrowOnFlashlight) throw RuntimeException("Flashlight failed")
        lastFlashlightEnabled = enabled
    }
}
