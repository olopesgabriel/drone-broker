package org.github.olopesgabriel.messaging

import java.net.Socket

data class Device(
    val name: String,
    val socket: Socket
) {

    val ipAddress: String
        get() = socket.inetAddress.hostAddress

    val macAddress: String
        get() {
            // TODO: Implementar método que pega o Mac Address to Socket
            return ""
        }

    companion object {
        fun anonymous(socket: Socket): Device {
            return Device("anonymous", socket)
        }
    }
}