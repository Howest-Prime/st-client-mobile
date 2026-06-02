package be.howest.howestprime.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.usecase.ClearCartUseCase
import be.howest.howestprime.domain.usecase.GetCartItemsUseCase
import be.howest.howestprime.domain.usecase.PlaceOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItems: GetCartItemsUseCase,
    private val placeOrder: PlaceOrderUseCase,
    private val clearCartItems: ClearCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUIState())
    val uiState: StateFlow<CartUIState> = _uiState.asStateFlow()

    init {
        collectCartItems()
    }

    fun placeOrder() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isPlacingOrder = true) }
                placeOrder(_uiState.value.items)
                _uiState.update { it.copy(isPlacingOrder = false, isOrderPlaced = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isPlacingOrder = false, errorMessage = "Failed to place order") }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                clearCartItems()
                _uiState.update { it.copy(isOrderPlaced = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to clear cart") }
            }
        }
    }

    private fun collectCartItems() {
        viewModelScope.launch {
            try {
                getCartItems().collect { items ->
                    _uiState.update { it.copy(items = items, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load cart") }
            }
        }
    }
}
