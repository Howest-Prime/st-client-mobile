package be.howest.howestprime.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_showsSubscribeForm_whenNotSubscribed() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUIState(emailAddress = "", isSubscribed = false),
                onOrderSnackClick = {},
                onEmailChanged = {},
                onSubscribeClick = {}
            )
        }

        composeTestRule.onNodeWithTag("subscribe").assertIsDisplayed()
    }

    @Test
    fun homeScreen_hideSubscribeForm_whenSubscribeClicked() {
        var isSubscribed by mutableStateOf(false)

        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUIState(emailAddress = "test@test.com", isSubscribed = isSubscribed),
                onOrderSnackClick = {},
                onEmailChanged = {},
                onSubscribeClick = { isSubscribed = true }
            )
        }

        composeTestRule.onNodeWithText("SUBSCRIBE").performClick()

        composeTestRule.onNodeWithTag("subscribe").assertDoesNotExist()
    }

    @Test
    fun homeScreen_showsOrderSnackButton() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUIState(),
                onOrderSnackClick = {},
                onEmailChanged = {},
                onSubscribeClick = {}
            )
        }

        composeTestRule.onNodeWithTag("order_snack_btn").assertIsDisplayed()
    }

    @Test
    fun homeScreen_orderSnackButton_triggersCallback() {
        var clicked = false

        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUIState(),
                onOrderSnackClick = { clicked = true },
                onEmailChanged = {},
                onSubscribeClick = {}
            )
        }

        composeTestRule.onNodeWithTag("order_snack_btn").performClick()

        assertTrue(clicked)
    }

    @Test
    fun homeScreen_subscribeButton_isDisabled_whenEmailEmpty() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUIState(emailAddress = "", isSubscribed = false),
                onOrderSnackClick = {},
                onEmailChanged = {},
                onSubscribeClick = {}
            )
        }

        composeTestRule.onNodeWithText("SUBSCRIBE").assertIsNotEnabled()
    }

    @Test
    fun homeScreen_subscribeButton_isEnabled_whenEmailNotEmpty() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUIState(emailAddress = "test@test.com", isSubscribed = false),
                onOrderSnackClick = {},
                onEmailChanged = {},
                onSubscribeClick = {}
            )
        }

        composeTestRule.onNodeWithText("SUBSCRIBE").assertIsEnabled()
    }

    @Test
    fun homeScreen_hidesSubscribeForm_whenAlreadySubscribed() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUIState(emailAddress = "", isSubscribed = true),
                onOrderSnackClick = {},
                onEmailChanged = {},
                onSubscribeClick = {}
            )
        }

        composeTestRule.onNodeWithTag("subscribe").assertDoesNotExist()
    }
}
