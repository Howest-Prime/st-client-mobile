package be.howest.howestprime.data.messaging

import android.util.Log
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class RabbitMQMessagePublisher @Inject constructor(
    private val hostName: String,
    private val queueName: String,
    private val connectionFactory: ConnectionFactory
) : MessagePublisher {

    override suspend fun publishMessage(message: String) {
        withContext(Dispatchers.IO) {
            try {
                val connection = connectionFactory.newConnection(hostName)
                val channel = connection.createChannel()

                channel.queueDeclare(queueName, true, false, false, null)
                channel.basicPublish("", queueName, null, message.toByteArray(StandardCharsets.UTF_8))
                Log.i("Messagebroker", "Message sent to broker: $message")

                channel.close()
                connection.close()

            } catch (e: Exception) {
                Log.e("Messagebroker", "Error publishing to Messagebroker! - ${e.message}")
            }
        }
    }
}