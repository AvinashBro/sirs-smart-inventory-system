# Stage 1: Build the app with Maven (use robust image with Java 17)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Set JAVA_HOME explicitly for Render Linux
ENV JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH
# Copy pom.xml first for better caching
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copy source and build (skip tests for speed)
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Stage 2: Run the app with lightweight JRE (Temurin 17)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar
# Render binds to $PORT (10000), expose it
EXPOSE $PORT
# Run with $PORT env var for Spring Boot
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]