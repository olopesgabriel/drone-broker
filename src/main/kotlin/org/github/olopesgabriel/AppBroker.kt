package org.github.olopesgabriel

import org.github.olopesgabriel.logging.Logger
import org.github.olopesgabriel.messaging.InboundMessage
import org.github.olopesgabriel.messaging.MessageQueue
import org.github.olopesgabriel.messaging.OutboundMessage
import org.github.olopesgabriel.network.*
import org.github.olopesgabriel.protocol.MessageInterceptor
import org.github.olopesgabriel.protocol.MessageRouter

class AppBroker (
    serverPort: Int
) : OnMessageReceivedListener<InboundMessage> {

    companion object {
        private val logger = Logger.getLogger<AppBroker>()
    }

    private val server = Server(serverPort, this)
    private val inboundMessageQueue = MessageQueue<InboundMessage>()
    private val outboundMessageQueue = MessageQueue<OutboundMessage>()
    private val messageRouter = MessageRouter(outboundMessageQueue)
    private val messageInterceptor = MessageInterceptor(messageRouter)
    private val responseSender = ResponseSender()

    fun start() {
        startSender()
        inboundMessageQueue.addListener(messageInterceptor)
        outboundMessageQueue.addListener(responseSender)
    }

    private fun startSender() {
        logger.info("Inicializando AppBroker")
        server.start()
    }

    override fun onMessageReceivedListener(message: InboundMessage) {
        inboundMessageQueue.send(message)
    }
}