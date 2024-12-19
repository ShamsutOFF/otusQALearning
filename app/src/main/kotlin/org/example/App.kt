/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class App {
    val greeting: String
        get() {
            return "@@@ Hello World from Docker Lesson!"
        }
}

fun main() {
    println(App().greeting)
    println("Текущее время и дата: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")))
}
