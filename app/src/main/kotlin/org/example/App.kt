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
    println("@@@ Запуск приложения")
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
        } else if (input.isNullOrBlank()) {
            println("Вы должны что-то ввести.")
        } else {
            try {
                val foundEntity: University = collectionUniversities.find(Document("name", input)).first()
                println("По вашему запросу найден университет:")
                println(foundEntity)
            } catch (ex: MongoClientException) {
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

private fun initMongoDB(): MongoDatabase {
    val url = "mongodb://localhost:27017"

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
