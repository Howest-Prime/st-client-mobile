package be.howest.howestprime.ui.snacks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.usecase.AddSnackToCartUseCase
import be.howest.howestprime.domain.usecase.GetAllSnacksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnacksViewModel @Inject constructor(
    private val getAllSnacks: GetAllSnacksUseCase,
    private val addSnack: AddSnackToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SnacksUIState())
    val uiState: StateFlow<SnacksUIState> = _uiState.asStateFlow()

    init {
        loadAllSnacks()
    }

    fun addToCart(snack: Snack) {
        viewModelScope.launch {
            addSnack(snack)
        }
    }

    private fun loadAllSnacks() {
        viewModelScope.launch {
            try {
                val snacks = getAllSnacks()
                _uiState.update { it.copy(snacks = snacks, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Failed to load snacks")
                }
            }
        }
    }
}
