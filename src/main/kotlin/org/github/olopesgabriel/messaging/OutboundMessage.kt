package org.github.olopesgabriel.messaging

data class OutboundMessage(
    val to: Device,
    val from: Device,
    val text: String
)