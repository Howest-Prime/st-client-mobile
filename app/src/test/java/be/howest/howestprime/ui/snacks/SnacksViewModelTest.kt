package be.howest.howestprime.ui.snacks

import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.usecase.AddSnackToCartUseCase
import be.howest.howestprime.domain.usecase.GetAllSnacksUseCase
import be.howest.howestprime.fake.FakeCartRepository
import be.howest.howestprime.fake.FakeSnackRepository
import be.howest.howestprime.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SnacksViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeSnackRepository: FakeSnackRepository
    private lateinit var fakeCartRepository: FakeCartRepository
    private lateinit var viewModel: SnacksViewModel

    private val snack1 = Snack(
        id = "s1", name = "Popcorn", description = "Salty", imageUrl = "",
        category = "Snacks", originalPrice = 4.99, promoPrice = 3.49,
        stock = 100, promo = true, allergens = emptyList()
    )

    private val snack2 = Snack(
        id = "s2", name = "Nachos", description = "Crispy", imageUrl = "",
        category = "Snacks", originalPrice = 5.99, promoPrice = null,
        stock = 50, promo = false, allergens = listOf("Gluten")
    )

    @Before
    fun setUp() {
        fakeSnackRepository = FakeSnackRepository()
        fakeCartRepository = FakeCartRepository()
        viewModel = SnacksViewModel(
            getAllSnacks = GetAllSnacksUseCase(fakeSnackRepository),
            addSnack = AddSnackToCartUseCase(fakeCartRepository)
        )
    }

    @Test
    fun initial_state_has_empty_snack_list() {
        assertEquals(emptyList<Snack>(), viewModel.uiState.value.snacks)
    }

    @Test
    fun snacks_are_loaded_when_repository_has_data() = runTest {
        fakeSnackRepository.setSnacks(listOf(snack1, snack2))
        assertEquals(listOf(snack1, snack2), viewModel.uiState.value.snacks)
    }

    @Test
    fun initial_state_has_no_error() {
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun addToCart_calls_repository_with_correct_snack() = runTest {
        viewModel.addToCart(snack1)
        val cartItems = fakeCartRepository.getCartItems().first()
        assertEquals(1, cartItems.size)
        assertEquals("s1", cartItems.first().snackId)
    }

    @Test
    fun addToCart_uses_promo_price_when_available() = runTest {
        viewModel.addToCart(snack1)
        val cartItems = fakeCartRepository.getCartItems().first()
        assertEquals(3.49, cartItems.first().price, 0.001)
    }

    @Test
    fun addToCart_uses_original_price_when_no_promo() = runTest {
        viewModel.addToCart(snack2)
        val cartItems = fakeCartRepository.getCartItems().first()
        assertEquals(5.99, cartItems.first().price, 0.001)
    }

    @Test
    fun snacks_update_when_repository_emits_new_list() = runTest {
        fakeSnackRepository.setSnacks(listOf(snack1))
        assertEquals(1, viewModel.uiState.value.snacks.size)
        fakeSnackRepository.setSnacks(listOf(snack1, snack2))
        assertEquals(2, viewModel.uiState.value.snacks.size)
    }
}
