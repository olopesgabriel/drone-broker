package org.github.olopesgabriel

import org.github.olopesgabriel.logging.Logger
import org.github.olopesgabriel.network.DeviceConnectedListener
import org.github.olopesgabriel.network.OnMessageReceivedListener
import org.github.olopesgabriel.network.Receiver
import org.github.olopesgabriel.network.Sender
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.net.Socket

class AppBroker (
    private val sender: Sender
): OnMessageReceivedListener, DeviceConnectedListener {

    companion object {
        private val logger = Logger.getLogger<AppBroker>()
    }

    private val senderThread = Thread(sender, "sender-thread")
    private val messageReceivers = ArrayList<Socket>()

    fun start() {
        startSender()
    }

    private fun startSender() {
        logger.info("Iniciando sender")
        sender.addOnMessageReceivedListener(this)
        sender.addDeviceConnectedListener(this)
        senderThread.start()
    }

    override fun onMessageReceivedListener(message: String) {
        notifyMessageReceivers(message)
    }

    private fun notifyMessageReceivers(message: String) {
        for (socket in messageReceivers) {
            val outputStream = DataOutputStream(BufferedOutputStream(socket.getOutputStream()))
            outputStream.writeBytes(message)
            outputStream.flush()
        }
    }

    override fun onDeviceConnected(socket: Socket) {
        logger.info("Novo receptor: ${socket.inetAddress.hostAddress}")
        messageReceivers.add(socket)
    }
}