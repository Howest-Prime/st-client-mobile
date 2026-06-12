package be.howest.howestprime.ui.sensors

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SensorsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sensorsScreen_showsFlashlightToggle() {
        composeTestRule.setContent {
            SensorsScreen(
                uiState = SensorsUIState(),
                toggleFlashLight = {},
                onScreenVisible = {},
                onScreenHidden = {}
            )
        }

        composeTestRule
            .onNodeWithTag("flashlight_switch")
            .assertIsDisplayed()
    }

    @Test
    fun sensorsScreen_showsSeatedStatus_whenSeated() {
        composeTestRule.setContent {
            SensorsScreen(
                uiState = SensorsUIState(isSeated = true),
                toggleFlashLight = {},
                onScreenVisible = {},
                onScreenHidden = {}
            )
        }

        composeTestRule
            .onNodeWithTag("seating_status_text")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("You appear to be seated")
            .assertIsDisplayed()
    }

    @Test
    fun sensorsScreen_showsStandingStatus_whenNotSeated() {
        composeTestRule.setContent {
            SensorsScreen(
                uiState = SensorsUIState(isSeated = false),
                toggleFlashLight = {},
                onScreenVisible = {},
                onScreenHidden = {}
            )
        }

        composeTestRule
            .onNodeWithText("You are standing")
            .assertIsDisplayed()
    }

    @Test
    fun sensorsScreen_flashlightSwitch_isOff_byDefault() {
        composeTestRule.setContent {
            SensorsScreen(
                uiState = SensorsUIState(flashlightEnabled = false),
                toggleFlashLight = {},
                onScreenVisible = {},
                onScreenHidden = {}
            )
        }

        composeTestRule
            .onNodeWithTag("flashlight_switch")
            .assertIsOff()
    }

    @Test
    fun sensorsScreen_flashlightSwitch_isOn_whenEnabled() {
        composeTestRule.setContent {
            SensorsScreen(
                uiState = SensorsUIState(flashlightEnabled = true),
                toggleFlashLight = {},
                onScreenVisible = {},
                onScreenHidden = {}
            )
        }

        composeTestRule
            .onNodeWithTag("flashlight_switch")
            .assertIsOn()
    }

    @Test
    fun sensorsScreen_flashlightToggle_triggersCallback() {
        var toggled = false

        composeTestRule.setContent {
            SensorsScreen(
                uiState = SensorsUIState(flashlightEnabled = false),
                toggleFlashLight = { toggled = true },
                onScreenVisible = {},
                onScreenHidden = {}
            )
        }

        composeTestRule
            .onNodeWithTag("flashlight_switch")
            .performClick()

        assertTrue(toggled)
    }

    @Test
    fun sensorsScreen_isDisplayed() {
        composeTestRule.setContent {
            SensorsScreen(
                uiState = SensorsUIState(),
                toggleFlashLight = {},
                onScreenVisible = {},
                onScreenHidden = {}
            )
        }

        composeTestRule
            .onNodeWithTag("sensors_screen")
            .assertIsDisplayed()
    }
}
