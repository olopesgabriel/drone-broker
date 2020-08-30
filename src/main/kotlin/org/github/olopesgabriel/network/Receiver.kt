package org.github.olopesgabriel.network

import org.github.olopesgabriel.logging.Logger
import java.net.ServerSocket
import java.net.Socket

open class Receiver {
    private val listeners = ArrayList<DeviceConnectedListener>()

    protected fun onDeviceConnected(socket: Socket) {
        for (listener in listeners) {
            listener.onDeviceConnected(socket)
        }
    }

    fun addDeviceConnectedListener(deviceConnectedListener: DeviceConnectedListener) {
        listeners.add(deviceConnectedListener)
    }
}