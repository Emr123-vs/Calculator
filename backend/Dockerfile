# ---------- Stage 1: build the jar with Maven ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# ---------- Stage 2: lightweight runtime ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/backend.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "--add-modules", "jdk.httpserver", "-jar", "app.jar"]