package be.howest.howestprime.di

import be.howest.howestprime.data.repository.CartRepositoryImpl
import be.howest.howestprime.data.repository.SensorRepositoryImpl
import be.howest.howestprime.data.repository.SnackRepositoryImpl
import be.howest.howestprime.data.repository.SubscriptionRepositoryImpl
import be.howest.howestprime.data.repository.TicketRepositoryImpl
import be.howest.howestprime.domain.repository.CartRepository
import be.howest.howestprime.domain.repository.SensorRepository
import be.howest.howestprime.domain.repository.SnackRepository
import be.howest.howestprime.domain.repository.SubscriptionRepository
import be.howest.howestprime.domain.repository.TicketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTicketRepository(
        impl: TicketRepositoryImpl
    ): TicketRepository

    @Binds
    @Singleton
    abstract fun bindSnackRepository(
        impl: SnackRepositoryImpl
    ): SnackRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindSensorRepository(
        impl: SensorRepositoryImpl
    ): SensorRepository

    @Binds
    @Singleton
    abstract fun bindSubscriptionRepository(
        impl: SubscriptionRepositoryImpl
    ): SubscriptionRepository
}
