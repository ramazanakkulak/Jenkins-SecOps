ARG BASE_IMAGE_VERSION=17-jdk-alpine
# Use an official OpenJDK 17 image as a parent image
FROM openjdk:${BASE_IMAGE_VERSION}

LABEL maintainer="test@gmail.com"

WORKDIR /app
# Copy the JAR file from the target directory to the working directory in the container
COPY target/devops-spring-app-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8081 for the application
EXPOSE 8081

# Define the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]