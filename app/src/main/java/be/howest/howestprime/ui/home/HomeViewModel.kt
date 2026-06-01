package be.howest.howestprime.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(emailAddress = email)
    }

    fun onSubscribeClick() {
        _uiState.value = _uiState.value.copy(isSubscribed = true)
    }
}
