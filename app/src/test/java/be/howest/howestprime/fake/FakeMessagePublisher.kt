package be.howest.howestprime.fake

import be.howest.howestprime.data.messaging.MessagePublisher

class FakeMessagePublisher : MessagePublisher {

    var lastPublishedMessage: String? = null
    var publishCallCount = 0
    var shouldThrow = false

    override suspend fun publishMessage(message: String) {
        if (shouldThrow) throw RuntimeException("Publish failed")
        lastPublishedMessage = message
        publishCallCount++
    }
}
