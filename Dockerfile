# Java (JDK 17)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/codexlibris-api.jar app.jar

COPY src/main/resources/keystore.p12 keystore.p12

EXPOSE 8443

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]