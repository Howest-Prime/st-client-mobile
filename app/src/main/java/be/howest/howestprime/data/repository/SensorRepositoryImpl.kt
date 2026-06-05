package be.howest.howestprime.data.repository

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import androidx.core.content.ContextCompat.getSystemService
import be.howest.howestprime.domain.repository.SensorRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SensorRepository {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

    private val _lightLevel = MutableStateFlow(0f)
    override val luxFlow: StateFlow<Float> = _lightLevel

    private val _isSeated = MutableStateFlow(false)
    override val isSeated: StateFlow<Boolean> = _isSeated

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            _lightLevel.value = event.values[0]
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun start() {
        sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun stop() {
        sensorManager.unregisterListener(listener)
    }

    override suspend fun toggleFlashlight(enabled: Boolean) {
        val cameraId = cameraManager?.cameraIdList?.firstOrNull() ?: return
        cameraManager.setTorchMode(cameraId, enabled)
    }
}
