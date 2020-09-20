package org.github.olopesgabriel.protocol

import org.github.olopesgabriel.logging.Logger
import org.github.olopesgabriel.messaging.Device
import org.github.olopesgabriel.messaging.InboundMessage
import org.github.olopesgabriel.messaging.MessageQueue
import org.github.olopesgabriel.messaging.OutboundMessage
import java.net.Socket
import java.util.*
import javax.swing.text.html.Option
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessageRouter(
    private val outboundMessageQueue: MessageQueue<OutboundMessage>
) {

    companion object {
        private val logger = Logger.getLogger<MessageRouter>()
    }

    private val routes: MutableMap<String, Device> = HashMap()
    private val devices: MutableMap<String, Device> = HashMap()
    private val listeners: MutableMap<String, MutableList<Device>> = HashMap()

    fun createRoute(routeName: String, device: Device) {
        val namedDevice = Device(routeName, device.socket)
        routes[routeName] = namedDevice
        devices[device.ipAddress] = namedDevice
        logger.info("Rota ($routeName, ${device.ipAddress}) criada com sucesso")
    }

    fun createListener(target: String, device: Device) {
        if (!listeners.containsKey(target)) {
            listeners[target] = ArrayList()
        }

        val knownDevice = getKnownDevice(device)

        listeners[target]?.add(knownDevice)
        logger.info("Agora \"${knownDevice.name}\" está escutando mensagens de \"$target\"")
    }

    fun routeMessage(message: InboundMessage) {
        val knownDevice = getKnownDevice(message.from)
        val listeners = listeners[knownDevice.name] ?: emptyList<Device>()
        for (listener in listeners) {
            val outboundMessage = OutboundMessage(listener, knownDevice, message.text)
            outboundMessageQueue.send(outboundMessage)
            logger.info("Mensagem \"${outboundMessage.text}\" será enviada ao dispositivo \"${listener.name}\"")
        }
    }

    fun removeDevice(device: Device) {
        val knownDevice = getKnownDevice(device)
        devices.remove(knownDevice.ipAddress)
        routes.remove(knownDevice.name)
        for (key in listeners.keys) {
            val routeListeners = listeners[key] ?: continue
            val listenersToRemove = ArrayList<Device>()
            for (listener in routeListeners) {
                if (listener.ipAddress == knownDevice.ipAddress) {
                    listenersToRemove.add(listener)
                }
            }

            for (listenerToRemove in listenersToRemove) {
                routeListeners.remove(listenerToRemove)
            }
        }
    }

    private fun getKnownDevice(device: Device): Device {
        if (devices.containsKey(device.ipAddress)) {
            return devices[device.ipAddress]!!
        }

        return device
    }

}
