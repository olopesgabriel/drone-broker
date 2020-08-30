package org.github.olopesgabriel

import org.github.olopesgabriel.network.Sender

fun main() {
    val sender = Sender("Controle remoto", 1234)
    val appBroker = AppBroker(sender)

    appBroker.start()
}

