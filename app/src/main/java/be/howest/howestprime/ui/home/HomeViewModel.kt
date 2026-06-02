package be.howest.howestprime.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.howestprime.domain.usecase.GetSubscriptionStatusUseCase
import be.howest.howestprime.domain.usecase.SubscribeToNewsletterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSubscriptionStatus: GetSubscriptionStatusUseCase,
    private val subscribeToNewsletter: SubscribeToNewsletterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        collectSubscriptionStatus()
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(emailAddress = email) }
    }

    fun onSubscribeClick() {
        viewModelScope.launch {
            subscribeToNewsletter(_uiState.value.emailAddress)
        }
    }

    private fun collectSubscriptionStatus() {
        viewModelScope.launch {
            getSubscriptionStatus().collect { subscribed ->
                _uiState.update { it.copy(isSubscribed = subscribed) }
            }
        }
    }
}
