# Шаг 1: Сборка приложения
FROM gradle:7.6-jdk17-alpine AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY . .

# Собираем проект и создаем фэт-джар
RUN ./gradlew shadowJar

# Шаг 2: Создание финального образа для запуска
FROM amazoncorretto:17.0.13-alpine

# Копируем фэт-джар из предыдущей стадии сборки
COPY --from=builder /app/app/build/libs/dz5.jar /app/app.jar

# Указываем точку входа
ENTRYPOINT ["java", "-jar", "/app/app.jar"]