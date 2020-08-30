package org.github.olopesgabriel.network

import org.github.olopesgabriel.messaging.OutboundMessage
import java.io.BufferedOutputStream
import java.io.DataOutputStream

class ResponseSender : OnMessageReceivedListener<OutboundMessage> {

    private fun sendResponse(outboundMessage: OutboundMessage) {
        val socket = outboundMessage.to.socket
        val outputStream = DataOutputStream(BufferedOutputStream(socket.getOutputStream()))

        val message = "[${outboundMessage.from.name}] ${outboundMessage.text}"

        outputStream.write(message.toByteArray(Charsets.UTF_8))
        outputStream.flush()
    }

    override fun onMessageReceivedListener(message: OutboundMessage) {
        sendResponse(message)
    }
}