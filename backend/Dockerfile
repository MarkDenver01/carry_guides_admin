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
FROM openjdk:21-jdk-slim

WORKDIR /app

# Install netcat and maven
RUN apt-get update && apt-get install -y netcat-openbsd maven && rm -rf /var/lib/apt/lists/*

COPY . .

# If mvnw/wait-for-it.sh exist, make them executable
RUN [ -f mvnw ] && chmod +x mvnw || echo "mvnw not found"
RUN [ -f wait-for-it.sh ] && chmod +x wait-for-it.sh || echo "wait-for-it.sh not found"

# Build app with fallback
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "target/carry_guide_admin-0.0.1-SNAPSHOT.jar"]

