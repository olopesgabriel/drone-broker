package org.github.olopesgabriel.network

import org.github.olopesgabriel.logging.Logger
import java.net.ServerSocket

class Sender (
    private val name: String,
    private val port: Int
) : Receiver(), Runnable {

    companion object {
        private val logger = Logger.getLogger<Sender>()
    }

    private val listeners = ArrayList<OnMessageReceivedListener>()

    private val serverSocket = ServerSocket(port)

    private fun start() {
        while (true) {
            val client = serverSocket.accept()
            logger.info("[Sender $name] Cliente ${client.inetAddress.hostAddress} conectado")

            onDeviceConnected(client)

            val socketReader = SocketMessageReader(client)
            socketReader.setListeners(listeners)
            val thread = Thread(socketReader, "message-reader")
            thread.start()
        }
    }

    fun addOnMessageReceivedListener(onMessageReceivedListener: OnMessageReceivedListener) {
        listeners.add(onMessageReceivedListener)
    }

    override fun run() {
        start()
    }
}