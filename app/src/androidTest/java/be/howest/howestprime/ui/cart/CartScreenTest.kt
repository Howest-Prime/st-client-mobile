package be.howest.howestprime.ui.cart

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.howest.howestprime.domain.model.CartItem
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleItems = listOf(
        CartItem("1", "Popcorn", 3.50, 2),
        CartItem("2", "Nachos", 4.00, 1)
    )

    @Test
    fun cartScreen_showsEmptyMessage_whenCartIsEmpty() {
        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(items = emptyList()),
                onPlaceOrder = {},
                onClearCart = {},
                onShare = {}
            )
        }

        composeTestRule.onNodeWithTag("cart_empty_text").assertIsDisplayed()
    }

    @Test
    fun cartScreen_showsOrderButtons_whenCartHasItems() {
        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(items = sampleItems),
                onPlaceOrder = {},
                onClearCart = {},
                onShare = {}
            )
        }

        composeTestRule.onNodeWithTag("place_order_btn").assertIsDisplayed()
        composeTestRule.onNodeWithTag("cancel_btn").assertIsDisplayed()
    }

    @Test
    fun cartScreen_showsOrderPlacedMessage_whenOrderPlaced() {
        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(isOrderPlaced = true),
                onPlaceOrder = {},
                onClearCart = {},
                onShare = {}
            )
        }

        composeTestRule.onNodeWithTag("cart_order_placed_text").assertIsDisplayed()
    }

    @Test
    fun cartScreen_emptyText_notVisible_whenCartHasItems() {
        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(items = sampleItems),
                onPlaceOrder = {},
                onClearCart = {},
                onShare = {}
            )
        }

        composeTestRule.onNodeWithTag("cart_empty_text").assertDoesNotExist()
    }

    @Test
    fun cartScreen_placeOrderButton_triggersCallback() {
        var orderPlaced = false

        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(items = sampleItems),
                onPlaceOrder = { orderPlaced = true },
                onClearCart = {},
                onShare = {}
            )
        }

        composeTestRule.onNodeWithTag("place_order_btn").performClick()

        assertTrue(orderPlaced)
    }

    @Test
    fun cartScreen_cancelButton_triggersCallback() {
        var cancelled = false

        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(items = sampleItems),
                onPlaceOrder = {},
                onClearCart = { cancelled = true },
                onShare = {}
            )
        }

        composeTestRule.onNodeWithTag("cancel_btn").performClick()

        assertTrue(cancelled)
    }

    @Test
    fun cartScreen_showsErrorMessage_whenErrorOccurs() {
        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(errorMessage = "Order failed"),
                onPlaceOrder = {},
                onClearCart = {},
                onShare = {}
            )
        }

        composeTestRule.onNodeWithTag("cart_error_text").assertIsDisplayed()
    }

    @Test
    fun cartScreen_showsItemNames_inCartList() {
        composeTestRule.setContent {
            CartScreen(
                uiState = CartUIState(items = sampleItems),
                onPlaceOrder = {},
                onClearCart = {},
                onShare = {}
            )
        }

        composeTestRule.onNodeWithText("2 x Popcorn").assertIsDisplayed()
        composeTestRule.onNodeWithText("1 x Nachos").assertIsDisplayed()
    }
}
