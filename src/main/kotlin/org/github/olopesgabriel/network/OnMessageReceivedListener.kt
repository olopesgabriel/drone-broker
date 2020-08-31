package org.github.olopesgabriel.network

interface OnMessageReceivedListener<T> {
    fun onMessageReceivedListener(message: T)
}