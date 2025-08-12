# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar into the container
COPY target/*.jar leave-management-0.0.1-SNAPSHOT.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

