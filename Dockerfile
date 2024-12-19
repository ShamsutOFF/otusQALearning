FROM amazoncorretto:17.0.13-alpine

COPY app/build/libs/*.jar lesson11.jar

ENTRYPOINT ["java", "-jar", "/lesson11.jar"]