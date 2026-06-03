package be.howest.howestprime.data.messaging

interface MessagePublisher {
    suspend fun publishMessage(message: String)
}