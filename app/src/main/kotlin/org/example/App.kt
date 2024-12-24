package org.example

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.mongodb.*
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
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
import org.bson.Document
import org.example.data.University
import org.slf4j.LoggerFactory
import java.util.*

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
    var attemptCounter = 0
    val maxAttempts = 5

    println("@@@ Запуск приложения 2")
    val database = initMongoDB()
    val collectionUniversities = database.getCollection<University>("Universities")
    collectionUniversities.insertMany(runBlocking { getUniversities() })

    println("ПРИВЕТСВУЕМ ВАС В ПОИСКЕ ПО КАТАЛОГУ УНИВЕСИТЕТОВ РОССИЙСКОЙ ФЕДЕРАЦИИ!")

    while (true) {
        print("\nВведите название университета на английском языке (или 'exit' для выхода): ")
        val input = readlnOrNull()

        if (input.equals("exit", ignoreCase = true)) {
            println("Завершение программы.")
            break
        } else if (input != null) {
            if (input.isBlank()) {
                println("Вы должны что-то ввести.")
                attemptCounter++
                if (attemptCounter >= maxAttempts) {
                    println("Превышено число попыток. Прекращение программы.")
                    break
                }
            } else {
                try {
                    attemptCounter = 0 // сбрасываем счетчик при успешной операции
                    val foundEntity: University = collectionUniversities.find(Document("name", input)).first()
                    println("По вашему запросу найден университет:")
                    println(foundEntity)
                } catch (ex: MongoClientException) {
                    println("Ничего не найдено. Попробуйте снова.")
                    attemptCounter++
                    if (attemptCounter >= maxAttempts) {
                        println("Превышено число попыток. Прекращение программы.")
                        break
                    }
                }
            }
        } else{
            println("Введен null !!!!")
        }
    }
}

private suspend fun getUniversities(): List<University> {
    val response: HttpResponse = client.get("http://universities.hipolabs.com/search?country=Russian%20Federation")
    println(response.status)
    client.close()
    return response.body<List<University>>()
}

private fun initMongoDB(): MongoDatabase {
    val url = System.getenv("MONGO_URL") ?: "mongodb://localhost:27017"
    println("@@@ initMongoDB() url = $url")
    val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()

    val settings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(url))
        .serverApi(serverApi)
        .build()

    LoggerFactory.getILoggerFactory().apply {
        if (this is LoggerContext) {
            this.getLogger("org.mongodb.driver").level = Level.OFF
        }
    }

    val mongoClient = MongoClient.create(settings)
    val database = mongoClient.getDatabase("OTUS")
    return database
}
