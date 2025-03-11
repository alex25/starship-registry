# Base image for the build stage
FROM eclipse-temurin:21 AS build

# Set working directory and copy project files
WORKDIR /usr/src/app
COPY . .

# Grant execution permissions to Maven Wrapper (if used)
RUN chmod +x mvnw

# Compile the application using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Base image for the runtime stage
FROM eclipse-temurin:21 AS runtime

# Re-declare ARGs to use them in the runtime stage
ARG SPRING_REDIS_HOST
ARG SPRING_REDIS_PORT
ARG SPRING_RABBITMQ_HOST
ARG SPRING_RABBITMQ_PORT
ARG SPRING_PROFILES_ACTIVE
ARG SERVER_PORT

# Install timezone data for the runtime image
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*

# Set timezone for the operating system
ENV TZ=America/Argentina/Buenos_Aires

# Copy the JAR file from the build stage
COPY --from=build /usr/src/app/target/*.jar /app.jar

# Expose the application port
EXPOSE ${SERVER_PORT}

# Command to run the application with JVM timezone configuration
CMD ["java", "-Duser.timezone=America/Argentina/Buenos_Aires", "-jar", "/app.jar"]