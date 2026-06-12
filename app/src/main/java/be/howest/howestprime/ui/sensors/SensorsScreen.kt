package be.howest.howestprime.ui.sensors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.howest.howestprime.R
import be.howest.howestprime.ui.theme.HowestMain

@Composable
fun SensorsScreen(
    uiState: SensorsUIState,
    toggleFlashLight: () -> Unit,
    onScreenVisible: () -> Unit,
    onScreenHidden: () -> Unit,
    modifier: Modifier = Modifier
) {
    DisposableEffect(Unit) {
        onScreenVisible()
        onDispose { onScreenHidden() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("sensors_screen")
    ) {
        SensorsHeader()

        LightSensorSection(uiState = uiState)
        Spacer(modifier = Modifier.height(40.dp))
        SeatingSection(isSeated = uiState.isSeated)
        Spacer(modifier = Modifier.height(40.dp))
        FlashlightSection(enabled = uiState.flashlightEnabled, onToggle = toggleFlashLight)
    }
}

@Composable
private fun SensorsHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.sensors_title),
            modifier = Modifier.padding(vertical = 24.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun LightSensorSection(
    uiState: SensorsUIState,
    modifier: Modifier = Modifier
) {
    val progress = (uiState.luxValue / 1000f).coerceIn(0f, 1f)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.WbSunny,
            contentDescription = stringResource(R.string.sensors_light_sensor_description),
            tint = HowestMain,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = HowestMain
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.sensors_lux_value, uiState.luxValue.toInt()),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(uiState.brightnessLabel, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SeatingSection(
    isSeated: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.Chair,
            contentDescription = stringResource(R.string.sensors_seating_description),
            tint = HowestMain,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isSeated) stringResource(R.string.sensors_seated) else stringResource(R.string.sensors_standing),
            color = if (isSeated) HowestMain else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.testTag("seating_status_text")
        )
    }
}

@Composable
private fun FlashlightSection(
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.sensors_flashlight_label), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = enabled, onCheckedChange = { onToggle() }, modifier = Modifier.testTag("flashlight_switch"))
    }
}

@Preview(showBackground = true)
@Composable
private fun SensorsScreenPreview() {
    SensorsScreen(
        uiState = SensorsUIState(luxValue = 350f, isSeated = true, flashlightEnabled = false),
        toggleFlashLight = {},
        onScreenVisible = {},
        onScreenHidden = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SensorsScreenStandingPreview() {
    SensorsScreen(
        uiState = SensorsUIState(luxValue = 800f, isSeated = false, flashlightEnabled = true),
        toggleFlashLight = {},
        onScreenVisible = {},
        onScreenHidden = {}
    )
}
