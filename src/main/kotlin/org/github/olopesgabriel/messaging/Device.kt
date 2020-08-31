package org.github.olopesgabriel.messaging

import java.net.Socket

data class Device(
    val name: String,
    val socket: Socket
) {

    val ipAddress: String
        get() = socket.inetAddress.hostAddress

    companion object {
        fun anonymous(socket: Socket): Device {
            return Device("anonymous", socket)
        }
    }
}