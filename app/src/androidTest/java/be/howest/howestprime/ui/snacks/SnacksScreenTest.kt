package be.howest.howestprime.ui.snacks

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.howest.howestprime.domain.model.Snack
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SnacksScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleSnack = Snack(
        id = "1", name = "Popcorn", description = "Salty popcorn", imageUrl = "",
        category = "snacks", originalPrice = 3.50, promoPrice = null,
        stock = 10, promo = false, allergens = emptyList()
    )

    private val promoSnack = Snack(
        id = "2", name = "Nachos", description = "Cheesy nachos", imageUrl = "",
        category = "snacks", originalPrice = 4.00, promoPrice = 3.00,
        stock = 5, promo = true, allergens = emptyList()
    )

    @Test
    fun snacksScreen_isDisplayed() {
        composeTestRule.setContent {
            SnacksScreen(uiState = SnacksUIState(snacks = emptyList()), onAddToCart = {})
        }

        composeTestRule.onNodeWithTag("snacks_screen").assertIsDisplayed()
    }

    @Test
    fun snacksScreen_showsSnackName_whenSnacksExist() {
        composeTestRule.setContent {
            SnacksScreen(uiState = SnacksUIState(snacks = listOf(sampleSnack)), onAddToCart = {})
        }

        composeTestRule.onNodeWithText("Popcorn").assertIsDisplayed()
    }

    @Test
    fun snacksScreen_showsAddButton_forEachSnack() {
        composeTestRule.setContent {
            SnacksScreen(uiState = SnacksUIState(snacks = listOf(sampleSnack, promoSnack)), onAddToCart = {})
        }

        composeTestRule.onNodeWithTag("snack_add_btn_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("snack_add_btn_2").assertIsDisplayed()
    }

    @Test
    fun snacksScreen_clickingAddButton_triggersCallback() {
        var addedSnack: Snack? = null

        composeTestRule.setContent {
            SnacksScreen(
                uiState = SnacksUIState(snacks = listOf(sampleSnack)),
                onAddToCart = { addedSnack = it }
            )
        }

        composeTestRule.onNodeWithTag("snack_add_btn_1").performClick()

        assertTrue(addedSnack == sampleSnack)
    }

    @Test
    fun snacksScreen_showsPromoPrice_whenSnackHasPromo() {
        composeTestRule.setContent {
            SnacksScreen(uiState = SnacksUIState(snacks = listOf(promoSnack)), onAddToCart = {})
        }

        composeTestRule.onNodeWithText("PROMO €3.0", substring = true).assertIsDisplayed()
    }

    @Test
    fun snacksScreen_showsMultipleSnacks() {
        composeTestRule.setContent {
            SnacksScreen(uiState = SnacksUIState(snacks = listOf(sampleSnack, promoSnack)), onAddToCart = {})
        }

        composeTestRule.onNodeWithText("Popcorn").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nachos").assertIsDisplayed()
    }
}
