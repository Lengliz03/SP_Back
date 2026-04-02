# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file built by Maven/Gradle
COPY target/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]
