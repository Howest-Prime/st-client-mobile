package be.howest.howestprime.di

import be.howest.howestprime.BuildConfig
import be.howest.howestprime.data.messaging.MessageConsumer
import be.howest.howestprime.data.messaging.MessagePublisher
import be.howest.howestprime.data.messaging.RabbitMQMessageConsumer
import be.howest.howestprime.data.messaging.RabbitMQMessagePublisher
import com.rabbitmq.client.ConnectionFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MessagingModule {
    private val rabbitMQHost = "amqp://${BuildConfig.AMQPusername}:${BuildConfig.AMQPpassword}@${BuildConfig.AMQPurl}"

    @Provides
    @Singleton
    fun provideConnectionFactory(): ConnectionFactory = ConnectionFactory()

    @Provides
    @Singleton
    fun provideMessageConsumer(connectionFactory: ConnectionFactory): MessageConsumer = lazy {
        RabbitMQMessageConsumer(rabbitMQHost, BuildConfig.AMQPsubscribetopic, connectionFactory)
    }.value

    @Provides
    @Singleton
    fun provideMessagePublisher(connectionFactory: ConnectionFactory): MessagePublisher = lazy {
        RabbitMQMessagePublisher(rabbitMQHost, BuildConfig.AMQPpublishtopic, connectionFactory)
    }.value
}
