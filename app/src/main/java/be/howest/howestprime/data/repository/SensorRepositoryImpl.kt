package be.howest.howestprime.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import be.howest.howestprime.domain.repository.SensorRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class SensorRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SensorRepository {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private val linearAccSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

    private val _lightLevel = MutableStateFlow(0f)
    override val luxFlow: StateFlow<Float> = _lightLevel

    private val _isSeated = MutableStateFlow(false)
    override val isSeated: StateFlow<Boolean> = _isSeated

    private var smoothedMagnitude = 0f

    private val lightListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            _lightLevel.value = event.values[0]
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private val motionListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val magnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            smoothedMagnitude = ALPHA * smoothedMagnitude + (1f - ALPHA) * magnitude
            _isSeated.value = smoothedMagnitude < MOTION_THRESHOLD
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun start() {
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(motionListener, linearAccSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun stop() {
        sensorManager.unregisterListener(lightListener)
        sensorManager.unregisterListener(motionListener)
        smoothedMagnitude = 0f
    }

    override suspend fun toggleFlashlight(enabled: Boolean) {
        val cameraId = cameraManager?.cameraIdList?.firstOrNull() ?: return
        cameraManager.setTorchMode(cameraId, enabled)
    }

    companion object {
        private const val ALPHA = 0.8f            // low-pass smoothing factor
        private const val MOTION_THRESHOLD = 0.5f // m/s²
    }
}
