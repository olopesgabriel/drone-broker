package org.github.olopesgabriel.network

import org.github.olopesgabriel.logging.Logger
import org.github.olopesgabriel.messaging.Device
import org.github.olopesgabriel.messaging.InboundMessage
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.net.Socket
import java.net.SocketException

class SocketMessageReader(
    private val socket: Socket
) : Runnable {

    companion object {
        private val logger = Logger.getLogger<SocketMessageReader>()
    }

    private var listener: OnMessageReceivedListener<InboundMessage>? = null

    override fun run() {
        sendConnectedMessage()
        readSocket(socket)
    }

    private fun sendConnectedMessage() {
        onMessageReceived("connected")
    }

    private fun readSocket(socket: Socket) {
        val inputStream = DataInputStream(BufferedInputStream((socket.getInputStream())))
        do {
            try {
                readMessage(inputStream)
            } catch (ex: SocketException) {
                logger.info("O cliente ${socket.inetAddress.hostAddress} se disconectou")
                onMessageReceived("disconnect")
                break
            }
        } while (true)
    }

    private fun readMessage(inputStream: DataInputStream) {
        val buffer = ByteArray(1024)
        inputStream.read(buffer)
        val message = createStringFromInput(buffer)
        if (message.isNotEmpty()) {
            onMessageReceived(message)
        }
    }

    private fun createStringFromInput(input: ByteArray): String {
        val validBytes = input.filter { it.toInt() != 0 }
        return String(validBytes.toByteArray(), Charsets.UTF_8)
    }

    fun setListener(listener: OnMessageReceivedListener<InboundMessage>?) {
        this.listener = listener
    }

    private fun onMessageReceived(text: String) {
        logger.info("${socket.inetAddress.hostAddress} enviou a mensagem \"$text\"")
        val anonymousDevice = Device.anonymous(socket)
        val message = InboundMessage(anonymousDevice, text)
        listener?.onMessageReceivedListener(message)
    }
}