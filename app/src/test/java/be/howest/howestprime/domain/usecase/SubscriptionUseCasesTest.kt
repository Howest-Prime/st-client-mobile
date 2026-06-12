package be.howest.howestprime.domain.usecase

import be.howest.howestprime.fake.FakeSubscriptionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubscriptionUseCasesTest {

    private lateinit var fakeRepository: FakeSubscriptionRepository

    @Before
    fun setUp() {
        fakeRepository = FakeSubscriptionRepository()
    }

    // GetSubscriptionStatusUseCase

    @Test
    fun getSubscriptionStatus_returns_false_initially() = runTest {
        val useCase = GetSubscriptionStatusUseCase(fakeRepository)
        assertFalse(useCase().first())
    }

    @Test
    fun getSubscriptionStatus_returns_true_after_subscribing() = runTest {
        fakeRepository.setSubscribed(true)
        val useCase = GetSubscriptionStatusUseCase(fakeRepository)
        assertTrue(useCase().first())
    }

    // SubscribeToNewsletterUseCase

    @Test
    fun subscribeToNewsletter_delegates_email_to_repository() = runTest {
        val useCase = SubscribeToNewsletterUseCase(fakeRepository)
        useCase("test@example.com")
        assertEquals("test@example.com", fakeRepository.lastSubscribedEmail)
    }

    @Test
    fun subscribeToNewsletter_sets_subscription_status_to_true() = runTest {
        val useCase = SubscribeToNewsletterUseCase(fakeRepository)
        useCase("test@example.com")
        assertTrue(fakeRepository.getSubscriptionStatus().first())
    }

    @Test
    fun subscribeToNewsletter_increments_call_count() = runTest {
        val useCase = SubscribeToNewsletterUseCase(fakeRepository)
        useCase("a@b.com")
        useCase("c@d.com")
        assertEquals(2, fakeRepository.subscribeCallCount)
    }

    @Test
    fun subscribeToNewsletter_accepts_empty_email() = runTest {
        val useCase = SubscribeToNewsletterUseCase(fakeRepository)
        useCase("")
        assertEquals("", fakeRepository.lastSubscribedEmail)
    }
}
