package org.github.olopesgabriel.network

import org.github.olopesgabriel.logging.Logger
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.net.Socket

class SocketMessageReader(
    private val socket: Socket
) : Runnable {

    companion object {
        private val logger = Logger.getLogger<SocketMessageReader>()
    }

    private var listeners: List<OnMessageReceivedListener> = ArrayList()

    override fun run() {
        readSocket(socket)
    }

    private fun readSocket(socket: Socket) {
        val inputStream = DataInputStream(BufferedInputStream((socket.getInputStream())))
        do {
            val buffer = ByteArray(1024)
            inputStream.read(buffer)
            val message = String(buffer)
            if (message.isNotEmpty()) {
                onMessageReceived(message)
            }
        } while (true)
    }

    fun setListeners(listeners: List<OnMessageReceivedListener>) {
        this.listeners = listeners
    }

    private fun onMessageReceived(message: String) {
        logger.info("${socket.inetAddress.hostAddress} enviou a mensagem \"$message\"")
        for (listener in listeners) {
            listener.onMessageReceivedListener(message)
        }
    }
}