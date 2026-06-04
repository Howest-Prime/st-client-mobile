package be.howest.howestprime.data.messaging

import android.util.Log
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class RabbitMQMessageConsumer(
    private val host: String,
    private val queueName: String,
    private val connectionFactory: ConnectionFactory
): MessageConsumer {

    private var consumerJob: Job? = null

    override var onMessageReceived: (String) -> Unit = { Log.w("Messagebroker", "onMessageReceived not set") }

    override fun startConsuming() {
        consumerJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = connectionFactory.newConnection(host)
                val channel = connection.createChannel()

                channel.queueDeclare(queueName, true, false, false, null)

                val deliverCallback = DeliverCallback { _, delivery ->
                    val message = String(delivery.body, StandardCharsets.UTF_8)
                    onMessageReceived(message)
                }

                channel.basicConsume(queueName, true, deliverCallback, CancelCallback {
                    Log.w("Messagebroker", "Consumer cancelled")
                })

                while (isActive) delay(1000)

            } catch (e: Exception) {
                Log.e("Messagebroker", "Error consuming from Messagebroker! - ${e.message}")
            }
        }
    }

    override fun stopConsuming() {
        consumerJob?.cancel()
    }
}
