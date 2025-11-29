# Use official Maven image with Java 17 to build
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# THIS IS THE ONLY LINE THAT MATTERS FOR RENDER
ENV PORT=8080
EXPOSE $PORT

# This single line fixes the $PORT error forever
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]