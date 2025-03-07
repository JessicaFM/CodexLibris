FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /app

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN echo "JAVA_HOME is set to: $JAVA_HOME" && java -version

COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests -Dspring.profiles.active=docker


FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app

#COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

#CMD ["java", "-jar", "app.jar"]
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
