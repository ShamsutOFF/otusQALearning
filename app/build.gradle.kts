plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    jacoco
    id("io.ktor.plugin") version "2.3.11"
    kotlin("plugin.serialization").version("2.0.0")
}

repositories {
    mavenCentral()
}

ktor {
    fatJar {
        archiveFileName.set("dz5.jar")
    }
}

application {
    mainClass = "AppKt"
}

val ktorVersion: String by project
val logbackVersion: String by project
val mockitoVersion: String by project

dependencies {
    // Ktor
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.2.1")

    // Tests
//    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    
    // Mockito
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.junit.jupiter.engine)
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("regress-tests") {
    useJUnitPlatform {
        includeTags("regress")
    }
}

tasks.register<Test>("smoke-tests") {
    useJUnitPlatform {
        includeTags("smoke")
    }
}

tasks.register<Test>("sanity-tests") {
    useJUnitPlatform {
        includeTags("sanity")
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    doLast {
        println("Shadow JAR created at: ${archiveFile.get().asFile.absolutePath}")
    }
}

class OtusPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        //Этим мы можем подтягивать любые зависимости которые нужны
        project.dependencies.apply {
            add("test", "org.jetbrains.kotlin:kotlin-test-junit5")
        }
    }
}

// Создаем обязательно Опен класс для нашей собственной таски
open class MyTask : DefaultTask() {
    @TaskAction
    fun action() {
        println("@@@ action() fun call from MyTask class")
        println("@@@ Run jacocoTestReport task!!! ")
//        doFirst {
//            "jacocoTestReport"
//        }
    }
}

// Регистрируем новую таску с указанием группы
tasks.register<MyTask>("myTask") {
    group = "myTestGroup"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<Test>("test") {
    doFirst {
        logger.lifecycle("@@@ Test task Run")
    }
    doLast {
        logger.lifecycle("@@@ Test task is Over")
    }
    // Тут мы указываем какую Таску хотим запустить по завершению данной Таски
//    finalizedBy("myTask")
    finalizedBy("jacocoTestReport")
}