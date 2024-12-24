FROM amazoncorretto:17.0.13-alpine

COPY app/build/libs/*.jar dz5.jar

ENTRYPOINT ["java", "-jar", "/dz5.jar"]