package be.howest.howestprime.data.messaging

interface MessageConsumer {
    fun startConsuming()
    fun stopConsuming()
    var onMessageReceived: (String) -> Unit
}