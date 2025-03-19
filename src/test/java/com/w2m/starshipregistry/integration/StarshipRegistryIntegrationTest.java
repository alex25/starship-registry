package com.w2m.starshipregistry.integration;

import java.nio.file.Paths;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.intuit.karate.junit5.Karate;

import lombok.extern.slf4j.Slf4j;

@Testcontainers
@Slf4j
public class StarshipRegistryIntegrationTest {

        // Shared network for all containers
        private static final Network starshipNetwork = Network.newNetwork();

        // Redis container
        @SuppressWarnings("resource")
        @Container
        private static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:latest"))
                        .withExposedPorts(6379)
                        .withCreateContainerCmdModifier(cmd -> cmd.withName("redis"))
                        .withNetwork(starshipNetwork)
                        .withNetworkAliases("redis")
                        .withEnv("TZ", "America/Argentina/Buenos_Aires")
                        .withCommand("redis-server", "/usr/local/etc/redis/redis.conf")
                        .withClasspathResourceMapping("redis.conf", "/usr/local/etc/redis/redis.conf",
                                        BindMode.READ_ONLY);

        // RabbitMQ container
        @SuppressWarnings("resource")
        @Container
        private static final GenericContainer<?> rabbitmq = new GenericContainer<>(
                        DockerImageName.parse("rabbitmq:3-management"))
                        .withCreateContainerCmdModifier(cmd -> cmd.withName("rabbitmq"))
                        .withNetwork(starshipNetwork)
                        .withNetworkAliases("rabbitmq")
                        .withExposedPorts(5672, 15672)
                        .withEnv("TZ", "America/Argentina/Buenos_Aires")
                        .withEnv("RABBITMQ_DEFAULT_USER", "admin")
                        .withEnv("RABBITMQ_DEFAULT_PASS", "admin123")
                        .withClasspathResourceMapping("rabbitmq.conf", "/etc/rabbitmq/rabbitmq.conf",
                                        BindMode.READ_ONLY);

        // Keycloak container
        @SuppressWarnings("resource")
        @Container
        private static final GenericContainer<?> keycloak = new GenericContainer<>(
                        DockerImageName.parse("quay.io/keycloak/keycloak:22.0.1"))
                        .withCreateContainerCmdModifier(cmd -> cmd.withName("keycloak"))
                        .withNetwork(starshipNetwork)
                        .withNetworkAliases("keycloak")
                        .withExposedPorts(8080)
                        .withEnv("TZ", "America/Argentina/Buenos_Aires")
                        .withEnv("KEYCLOAK_ADMIN", "admin")
                        .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
                        .withEnv("KC_HTTP_ENABLED", "true")
                        .withEnv("KC_HOSTNAME", "keycloak")
                        .withEnv("KC_HOSTNAME_PORT", "8080")
                        .withEnv("KC_HOSTNAME_STRICT", "false")
                        .withEnv("KC_HOSTNAME_STRICT_BACKCHANNEL", "false")
                        .withClasspathResourceMapping("realm-dev.json", "/opt/keycloak/data/import/realm-dev.json",
                                        BindMode.READ_ONLY)
                        .withCommand("start-dev", "--import-realm");

        // ApiGateway container
        @SuppressWarnings("resource")
        @Container
        private static final GenericContainer<?> apigateway = new GenericContainer<>(
                        new ImageFromDockerfile().withDockerfile(Paths.get("apigateway/Dockerfile")))
                        .withCreateContainerCmdModifier(cmd -> cmd.withName("apigateway"))
                        .withNetwork(starshipNetwork)
                        .withNetworkAliases("apigateway")
                        .withExposedPorts(8888, 8887)
                        .withEnv("TZ", "America/Argentina/Buenos_Aires")
                        .withEnv("KEYCLOAK_SERVICE_URI", "http://keycloak:8080")
                        .withEnv("STARSHIP_SERVICE_URI", "http://app:8081")
                        .withEnv("APP_PORT", "8888");

        // App container (Java application)
        @SuppressWarnings("resource")
        @Container
        private static final GenericContainer<?> app = new GenericContainer<>(
                        new ImageFromDockerfile().withDockerfile(Paths.get("./dockerfile")))
                        .withCreateContainerCmdModifier(cmd -> cmd.withName("app"))
                        .withNetwork(starshipNetwork)
                        .withNetworkAliases("app")
                        .withExposedPorts(8081, 8082)
                        .withEnv("TZ", "America/Argentina/Buenos_Aires")
                        .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
                        .withEnv("SPRING_REDIS_HOST", "redis")
                        .withEnv("SPRING_PROFILES_ACTIVE", "dev");

        @Karate.Test
        Karate karateTestRunner() {
                log.info("Redis started on port: " + redis.getMappedPort(6379));
                log.info("RabbitMQ started on port: " + rabbitmq.getMappedPort(5672));
                log.info("Keycloak started on port: " + keycloak.getMappedPort(8080));
                log.info("ApiGateway started on port: " + apigateway.getMappedPort(8888));
                log.info("App started on port: " + app.getMappedPort(8081));

                String host = app.getHost();
                int port = app.getMappedPort(8081);

                System.setProperty("APP_HOST", host);
                System.setProperty("APP_PORT", String.valueOf(port));

                String hostApiGateway = apigateway.getHost();
                int portApiGateway = apigateway.getMappedPort(8888);

                System.setProperty("APIGATEWAY_HOST", hostApiGateway);
                System.setProperty("APIGATEWAY_PORT", String.valueOf(portApiGateway));

                String hostKeycloak = keycloak.getHost();
                int portKeycloak = keycloak.getMappedPort(8080);

                System.setProperty("KEYCLOAK_HOST", hostKeycloak);
                System.setProperty("KEYCLOAK_PORT", String.valueOf(portKeycloak));

                return Karate.run("classpath:karate").tags("~@ignore")
                                .outputCucumberJson(true)
                                .relativeTo(getClass());
        }
}