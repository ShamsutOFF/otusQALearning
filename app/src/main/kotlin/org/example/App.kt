package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.example.data.University
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class App {
    val greeting: String
        get() {
            return "@@@ Hello World from Docker Lesson!\n" + "Текущее время и дата: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
            )
        }
}

private val client by lazy {
    HttpClient(CIO) {
        install(Logging)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
}

fun main() {
    println(App().greeting)
    val listOfUniversity = runBlocking { getUniversities() }

    println("ПРИВЕТСВУЕМ ВАС В ПОИСКЕ ПО КАТАЛОГУ УНИВЕСИТЕТОВ РОССИЙСКОЙ ФЕДЕРАЦИИ!")

    while (true) {
        print("\nВведите название университета на английском языке (или 'exit' для выхода): ")
        val input = readlnOrNull()
        if (input.equals("exit", ignoreCase = true)) {
            println("Завершение программы.")
            break
        } else if (input.isNullOrBlank()) {
            println("Вы должны что-то ввести.")
        } else {
            val foundEntities = listOfUniversity.filter { it.name.equals(input, ignoreCase = true) }
            if (foundEntities.isNotEmpty()) {
                println("Найден университет:")
                foundEntities.forEach { println(it) }
            } else {
                println("Ничего не найдено. Попробуйте снова.")
            }
        }
    }
}

private suspend fun getUniversities(): List<University> {
    val response: HttpResponse = client.get("http://universities.hipolabs.com/search?country=Russian%20Federation")
    println(response.status)
    client.close()
    return response.body<List<University>>()
}
