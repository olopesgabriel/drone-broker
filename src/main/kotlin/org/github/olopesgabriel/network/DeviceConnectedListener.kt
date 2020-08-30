package org.github.olopesgabriel.network

import java.net.Socket

interface DeviceConnectedListener {

    fun onDeviceConnected(socket: Socket)
}