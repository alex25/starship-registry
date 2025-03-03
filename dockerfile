# Imagen base para la fase de construcción
FROM eclipse-temurin:21 AS build

# Variables de entorno que pueden ser definidas externamente al construir o ejecutar el contenedor
ARG BASE_PATH='/v1/starshipdb'
ARG APP_PORT='8080'
ARG APP_HOST='localhost'
ARG DB_URL='jdbc:h2:mem:starshipdb'
ARG DB_USER='admin'
ARG DB_PASS='admin'

# Establece valores de entorno para la aplicación, con opción de sobrescribirlos
ENV BASE_PATH=${BASE_PATH}
ENV APP_PORT=${APP_PORT}
ENV APP_HOST=${APP_HOST}
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASS=${DB_PASS}

# Configuración del directorio de trabajo y copia de archivos del proyecto
WORKDIR /usr/src/app
COPY . .

# Da permisos de ejecución al script mvnw (si lo usas)
RUN chmod +x mvnw

# Compila la aplicación usando Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Imagen base para la fase de ejecución
FROM eclipse-temurin:21 AS runtime

# Copia el archivo JAR generado desde la etapa de construcción
COPY --from=build /usr/src/app/target/*.jar /app.jar

# Expone el puerto configurado
EXPOSE ${APP_PORT}

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app.jar"]