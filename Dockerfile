# Use a minimal JDK 21 runtime base image
FROM eclipse-temurin:21-jdk

# Set the working directory in the container
WORKDIR /app

# Cop the JAR file into the container
COPY target/stock_price_notification_service-0.0.1-SNAPSHOT.jar app.jar

#Expose the application port
EXPOSE 8080

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


