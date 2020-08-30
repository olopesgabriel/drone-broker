package org.github.olopesgabriel.messaging

data class OutboundMessage(
    val to: Device,
    val text: String
)