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
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.bson.Document
import org.example.data.University
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("UniversityApp")

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

private val database by lazy { initMongoDB() }
private val collectionUniversities by lazy { database.getCollection<University>("Universities") }

fun main() {
    logger.info("ПРИВЕТСВУЕМ ВАС В ПОИСКЕ ПО КАТАЛОГУ УНИВЕСИТЕТОВ!")
    client.use {
        logger.info("Загрузка университетов...")
        when (val result = runBlocking { loadUniversitiesToMongoDB() }) {
            is Result.Success -> {
                logger.info("Список университетов успешно загружен.")
                getUniversities()
            }
            is Result.Error -> {
                logger.error("Ошибка при загрузке университетов: ${result.message}")
                println("Произошла ошибка. Попробуйте снова.")
            }
        }
    }
}

private suspend fun loadUniversitiesToMongoDB(): Result<List<University>> {
    val universities = getUniversitiesByCountry()
    return if (universities is Result.Success) {
        val existingNames = collectionUniversities.find().map { it.name }.toList().toSet()
        val newUniversities = universities.data.filter { it.name !in existingNames }
        if (newUniversities.isNotEmpty()) {
            collectionUniversities.insertMany(newUniversities)
            logger.info("Добавлено ${newUniversities.size} новых университетов.")
            Result.Success(newUniversities)
        } else {
            logger.info("Новых университетов для добавления не найдено.")
            Result.Success(emptyList())
        }
    } else {
        universities
    }
}

private fun getUniversities() {
    while (true) {
        print("\nВведите название университета на английском языке (или 'exit' для выхода): ")
        val input = readlnOrNull()
        when {
            input.equals("exit", ignoreCase = true) -> {
                println("Завершение программы.")
                break
            }
            input.isNullOrBlank() -> println("Введите название университета.")
            else -> findUniversityByName(input)
        }
    }
}

private fun findUniversityByName(name: String) {
    try {
        val foundEntity: University = collectionUniversities.find(Document("name", name)).first()
        println("По вашему запросу найден университет:")
        println(foundEntity)
    } catch (ex: MongoClientException) {
        println("Университет $name не найден. Попробуйте снова.")
    }
}

private suspend fun getUniversitiesByCountry(): Result<List<University>> {
    val listOfUniversities = mutableListOf<University>()
    while (true) {
        print("\nВведите название страны для поиска (или 'exit' для выхода): ")
        val input = readlnOrNull()
        when {
            input.equals("exit", ignoreCase = true) -> {
                println("Завершение программы.")
                return Result.Error("Программа завершена.")
            }
            input.isNullOrBlank() -> {
                println("Введите название страны.")
            }
            else -> {
                val result = runCatching {
                    val encodedCountry = input.encodeURLParameter()
                    val response: HttpResponse = client.get("http://universities.hipolabs.com/search?country=$encodedCountry")
                    logger.info("Ответ от сервера: ${response.status}")
                    response.body<List<University>>()
                }

                result.onSuccess { universities ->
                    if (universities.isNotEmpty()) {
                        listOfUniversities.addAll(universities)
                        println("Найдено ${universities.size} университетов для страны $input.")
                        return Result.Success(listOfUniversities)
                    } else {
                        println("Для этой страны $input ничего не найдено. Попробуйте снова.")
                    }
                }.onFailure { ex ->
                    logger.error("Ошибка при загрузке университетов: ${ex.message}")
                    println("Произошла ошибка. Попробуйте снова.")
                }
            }
        }
    }
}

private fun initMongoDB(): MongoDatabase {
    val url = System.getenv("MONGO_URL") ?: "mongodb://localhost:27017"
    logger.info("@@@ initMongoDB() url = $url")
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

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}