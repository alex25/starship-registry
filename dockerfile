# Base image for the build stage
FROM eclipse-temurin:21 AS build

# Environment variables that can be defined externally during build or container execution
ARG BASE_PATH='/v1/starshipdb'
ARG APP_PORT='8081'
ARG APP_HOST='localhost'
ARG DB_URL='jdbc:h2:mem:starshipdb'
ARG DB_USER='admin'
ARG DB_PASS='admin'

# Set application environment variables (overridable)
ENV BASE_PATH=${BASE_PATH}
ENV APP_PORT=${APP_PORT}
ENV APP_HOST=${APP_HOST}
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASS=${DB_PASS}

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
ARG APP_PORT

# Install timezone data for the runtime image
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*

# Set timezone for the operating system
ENV TZ=America/Argentina/Buenos_Aires

# Copy the JAR file from the build stage
COPY --from=build /usr/src/app/target/*.jar /app.jar

# Expose the application port
EXPOSE ${APP_PORT}

# Command to run the application with JVM timezone configuration
CMD ["java", "-Duser.timezone=America/Argentina/Buenos_Aires", "-jar", "/app.jar"]