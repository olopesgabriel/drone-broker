package org.github.olopesgabriel.protocol

import org.github.olopesgabriel.messaging.InboundMessage
import org.github.olopesgabriel.network.OnMessageReceivedListener

class MessageInterceptor (
    private val messageRouter: MessageRouter
) : OnMessageReceivedListener<InboundMessage> {

    private fun intercept(message: InboundMessage) {
        val messageCommand = CommandMessage(message.text)

        when(messageCommand.command) {
            "init" -> messageRouter.createRoute(messageCommand.target, message.from)
            "listen" -> messageRouter.createListener(messageCommand.target, message.from)
            else -> messageRouter.routeMessage(message)
        }
    }

    private class CommandMessage(message: String) {

        private var _command: String = ""
        private var _target: String = ""
        private var _text: String = ""

        val command: String
            get() = _command.toLowerCase()

        val target: String
            get() = _target.toLowerCase()

        val text: String
            get() = _text

        init {
            val split = message.split(" ")
            if (split.isNotEmpty()) {
                _command = split[0].trim()
            }

            if (split.size > 1) {
                _target = split[1].trim()
            }

            if (split.size > 2) {
                val args = split.subList(2, split.size-1)
                _text = args.joinToString(" ").trim()
            }
        }
    }

    override fun onMessageReceivedListener(message: InboundMessage) {
        intercept(message)
    }
}