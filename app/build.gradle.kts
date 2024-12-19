plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    jacoco
    id("io.ktor.plugin") version "2.3.11"
}

repositories {
    mavenCentral()
}

ktor {
    fatJar {
        archiveFileName.set("lesson11.jar")
    }
}

application {
    mainClass.set("App.kt")
}

dependencies {
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.example.AppKt"
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