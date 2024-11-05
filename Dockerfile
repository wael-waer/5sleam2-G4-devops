# Use the official OpenJDK 17 base image
FROM openjdk:17-jdk-slim

#author
LABEL authors="omar"

#Set the working directory
WORKDIR /app

# Copy the jar file from the build context into the container
COPY target/Foyer-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8090

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]