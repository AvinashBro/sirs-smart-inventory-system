# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime stage – tiny & perfect for Render
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# This is the ONLY correct way for Render 2025
EXPOSE 8080
ENV JAVA_OPTS="-Dserver.port=8080"
CMD exec java $JAVA_OPTS -jar app.jar
# This single line fixes the $PORT error forever
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]