package org.github.olopesgabriel.messaging

import org.github.olopesgabriel.network.OnMessageReceivedListener
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList

class MessageQueue<T> {

    private val messages: Queue<T> = LinkedList()
    private val mutexLock = ReentrantLock()
    private val listeners: MutableList<OnMessageReceivedListener<T>> = ArrayList()

    fun send(message: T) {
        mutexLock.lock()
        messages.add(message)
        mutexLock.unlock()

        notifyListeners()
    }

    private fun getNextMessage(): Optional<T> {
        mutexLock.lock()
        var output: Optional<T> = Optional.empty()
        if (messages.isNotEmpty()) {
            output = Optional.of(messages.remove())
        }

        mutexLock.unlock()
        return output
    }

    fun addListener(listener: OnMessageReceivedListener<T>) {
        listeners.add(listener)
    }

    private fun notifyListeners() {
        getNextMessage().ifPresent {
            for (listener in listeners) {
                listener.onMessageReceivedListener(it)
            }
        }
    }
}