# --- Etape 1 : compilation ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Les dependances bougent rarement : cette couche reste en cache tant que pom.xml est inchange
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package

# --- Etape 2 : image finale, sans Maven ni sources ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# Ne pas tourner en root
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring

COPY --from=build /build/target/StringMVC_TO_StringBoot.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
