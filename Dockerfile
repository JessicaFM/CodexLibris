# Usa una imagen base de Java (JDK 17)
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado por Spring Boot
COPY target/codexlibris-api.jar app.jar

# Copia el archivo de keystore SSL
COPY src/main/resources/keystore.p12 keystore.p12

# Expone el puerto HTTPS
EXPOSE 8443

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
