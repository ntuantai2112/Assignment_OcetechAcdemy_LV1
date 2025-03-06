# Stage 1: Build
# Start with a Maven image that includes JDK 17
FROM maven:3.9.9-amazoncorretto-17 AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with Maven
RUN mvn package -DskipTests

# Stage 2: Create Image
# Start with Amazon Corretto JDK 17
FROM amazoncorretto:17.0.13

# Set working directory to /app and copy compiled file from the build stage
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
