package org.github.olopesgabriel.messaging

data class InboundMessage(
    val from: Device,
    val text: String
)