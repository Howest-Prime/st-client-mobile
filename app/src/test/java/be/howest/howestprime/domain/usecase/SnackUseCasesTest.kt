package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.fake.FakeCartRepository
import be.howest.howestprime.fake.FakeSnackRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SnackUseCasesTest {

    private lateinit var fakeSnackRepository: FakeSnackRepository
    private lateinit var fakeCartRepository: FakeCartRepository

    private val snackWithPromo = Snack(
        id = "s1", name = "Popcorn", description = "Salty popcorn",
        imageUrl = "https://example.com/popcorn.jpg", category = "Snacks",
        originalPrice = 4.99, promoPrice = 3.49, stock = 50, promo = true,
        allergens = listOf("Gluten")
    )

    private val snackWithoutPromo = Snack(
        id = "s2", name = "Nachos", description = "Crispy nachos",
        imageUrl = "https://example.com/nachos.jpg", category = "Snacks",
        originalPrice = 5.99, promoPrice = null, stock = 30, promo = false,
        allergens = emptyList()
    )

    @Before
    fun setUp() {
        fakeSnackRepository = FakeSnackRepository()
        fakeCartRepository = FakeCartRepository()
    }

    @Test
    fun getAllSnacks_returns_all_snacks_from_repository() = runTest {
        fakeSnackRepository.setSnacks(listOf(snackWithPromo, snackWithoutPromo))
        val useCase = GetAllSnacksUseCase(fakeSnackRepository)
        val result = useCase().first()
        assertEquals(listOf(snackWithPromo, snackWithoutPromo), result)
    }

    @Test
    fun getAllSnacks_returns_empty_list_when_no_snacks() = runTest {
        val useCase = GetAllSnacksUseCase(fakeSnackRepository)
        val result = useCase().first()
        assertEquals(emptyList<Snack>(), result)
    }

    @Test
    fun addSnackToCart_uses_promoPrice_when_available() = runTest {
        val useCase = AddSnackToCartUseCase(fakeCartRepository)
        useCase(snackWithPromo)
        val items = fakeCartRepository.getCartItems().first()
        assertEquals(3.49, items.first().price, 0.001)
    }

    @Test
    fun addSnackToCart_uses_originalPrice_when_no_promoPrice() = runTest {
        val useCase = AddSnackToCartUseCase(fakeCartRepository)
        useCase(snackWithoutPromo)
        val items = fakeCartRepository.getCartItems().first()
        assertEquals(5.99, items.first().price, 0.001)
    }

    @Test
    fun addSnackToCart_adds_item_with_quantity_one() = runTest {
        val useCase = AddSnackToCartUseCase(fakeCartRepository)
        useCase(snackWithPromo)
        val items = fakeCartRepository.getCartItems().first()
        assertEquals(1, items.first().quantity)
    }

    @Test
    fun addSnackToCart_maps_snack_id_and_name_correctly() = runTest {
        val useCase = AddSnackToCartUseCase(fakeCartRepository)
        useCase(snackWithPromo)
        val item = fakeCartRepository.getCartItems().first().first()
        assertEquals("s1", item.snackId)
        assertEquals("Popcorn", item.name)
    }
}
