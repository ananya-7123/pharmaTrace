# Multi-stage Dockerfile for Auth Service
FROM maven:3.9.0-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/auth-service-*.jar app.jar

# Expose port
EXPOSE 8081

# Environment variables
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/pharmatrace_auth
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
