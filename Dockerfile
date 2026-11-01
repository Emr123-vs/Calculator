# Build stage: temurin + maven
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage: lightweight JRE, replaces deprecated openjdk:17-jre-slim
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/maven-calculator-1.0-SNAPSHOT.jar calculator.jar
ENTRYPOINT ["java", "-jar", "calculator.jar"]
