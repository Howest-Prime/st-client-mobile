package be.howest.howestprime.ui.home

import be.howest.howestprime.domain.usecase.GetSubscriptionStatusUseCase
import be.howest.howestprime.domain.usecase.SubscribeToNewsletterUseCase
import be.howest.howestprime.fake.FakeSubscriptionRepository
import be.howest.howestprime.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeSubscriptionRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeSubscriptionRepository()
        viewModel = HomeViewModel(
            getSubscriptionStatus = GetSubscriptionStatusUseCase(fakeRepository),
            subscribeToNewsletter = SubscribeToNewsletterUseCase(fakeRepository)
        )
    }

    @Test
    fun initial_state_has_empty_email_and_not_subscribed() {
        assertEquals("", viewModel.uiState.value.emailAddress)
        assertFalse(viewModel.uiState.value.isSubscribed)
    }

    @Test
    fun onEmailChanged_updates_email_in_state() {
        viewModel.onEmailChanged("user@example.com")
        assertEquals("user@example.com", viewModel.uiState.value.emailAddress)
    }

    @Test
    fun onEmailChanged_with_empty_string_clears_email() {
        viewModel.onEmailChanged("user@example.com")
        viewModel.onEmailChanged("")
        assertEquals("", viewModel.uiState.value.emailAddress)
    }

    @Test
    fun subscription_status_reflects_repository_state() = runTest {
        fakeRepository.setSubscribed(true)
        assertTrue(viewModel.uiState.value.isSubscribed)
    }

    @Test
    fun onSubscribeClick_subscribes_with_current_email() = runTest {
        viewModel.onEmailChanged("test@test.com")
        viewModel.onSubscribeClick()
        assertEquals("test@test.com", fakeRepository.lastSubscribedEmail)
    }

    @Test
    fun onSubscribeClick_sets_subscription_status_to_true() = runTest {
        viewModel.onEmailChanged("test@test.com")
        viewModel.onSubscribeClick()
        assertTrue(viewModel.uiState.value.isSubscribed)
    }

    @Test
    fun initial_subscription_status_is_loaded_from_repository() = runTest {
        fakeRepository.setSubscribed(true)
        val newViewModel = HomeViewModel(
            getSubscriptionStatus = GetSubscriptionStatusUseCase(fakeRepository),
            subscribeToNewsletter = SubscribeToNewsletterUseCase(fakeRepository)
        )
        assertTrue(newViewModel.uiState.value.isSubscribed)
    }
}
