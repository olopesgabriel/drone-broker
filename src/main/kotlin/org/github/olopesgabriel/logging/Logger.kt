package org.github.olopesgabriel.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger<T> private constructor(
    private val clazz: Class<T>
) {

    private val className = clazz.simpleName
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm")

    fun info(message: String) {
        printMessage(message)
    }

    private fun printMessage(message: String) {
        val dateTime = LocalDateTime.now()
        val formattedDateTime = dateFormatter.format(dateTime)
        val threadName = Thread.currentThread().name
        println("[$formattedDateTime - $threadName] $className: $message")
    }

    companion object {

        inline fun <reified T> getLogger(): Logger<T> {
            return getLogger(T::class.java)
        }

        fun <T> getLogger(clazz: Class<T>): Logger<T> {
            return Logger(clazz)
        }
    }
}