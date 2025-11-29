# Build stage (Maven with Java 17)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline -B  # Cache dependencies for faster builds
COPY src ./src
RUN mvn clean package -DskipTests -B  # Skip tests for speed

# Package stage (JRE for runtime)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /home/app
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE $PORT
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]