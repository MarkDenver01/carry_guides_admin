#FROM openjdk:21-jdk-slim
#
#WORKDIR /app
#
## Install netcat for wait-for-it.sh
#RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*
#
#COPY . .
#RUN chmod +x mvnw wait-for-it.sh
#RUN ./mvnw clean package -DskipTests
#
#EXPOSE 8080
#ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "target/carry_guide_admin-0.0.1-SNAPSHOT.jar"]
# backend/Dockerfile
# Use OpenJDK base image
FROM eclipse-temurin:21-jdk-alpine

# Set workdir
WORKDIR /app

# Install Maven and Netcat (for health checks or DB wait)
RUN apk update && apk add maven netcat-openbsd

# Copy source code
COPY . .

# Build Spring Boot app using Maven (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Expose default Spring Boot port
EXPOSE 8080

# Run the built JAR (replace name with your actual JAR name)
ENTRYPOINT ["java", "-jar", "target/carry_guide_admin-0.0.1-SNAPSHOT.jar"]
