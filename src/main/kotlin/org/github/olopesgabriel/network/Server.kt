package org.github.olopesgabriel.network

import org.github.olopesgabriel.logging.Logger
import org.github.olopesgabriel.messaging.InboundMessage
import org.github.olopesgabriel.messaging.OutboundMessage
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.net.ServerSocket

class Server (
    port: Int,
    private val onMessageReceivedListener: OnMessageReceivedListener<InboundMessage>? = null
) {

    companion object {
        private val logger = Logger.getLogger<Server>()
    }

    private val serverSocket = ServerSocket(port)
    private lateinit var clientListeningThread: Thread

    fun start() {
        clientListeningThread = Thread(this::startListeningSocketMessages, "server")
        clientListeningThread.start()
    }

    private fun startListeningSocketMessages() {
        while (true) {
            val socket = serverSocket.accept()
            val messageReader = SocketMessageReader(socket)
            messageReader.setListener(onMessageReceivedListener)

            logger.info("Dispositivo anonimo ${socket.inetAddress.hostAddress} conectou-se ao servidor")

            Thread(messageReader, "message-reader")
                .start()
        }
    }
}