FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /app

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN echo "JAVA_HOME is set to: $JAVA_HOME" && java -version

COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/codexlibris-api.jar app.jar

EXPOSE 8080

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]