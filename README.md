# Starship Registry

A Spring Boot demonstration project for managing starship registrations and information.

## Requirements

- Java 21
- Maven
- Docker and Docker Compose

## Tech Stack

- **Framework**: Spring Boot 3.4.3
- **Data**: JPA, Redis (caching), RabbitMQ (messaging)
- **Database**: H2 (dev), Flyway (easier to maintain DDL scripts)
- **Tools**: Lombok, Karate (integration tests)
- **Security**: Keycloak

## Architecture

This project follows the Hexagonal Architecture pattern:

```
                     ┌─────────────────────────┐
                     │                         │
                     │       Core Layer        │
                     │ (Use cases, Domain      │
                     │  Business Logic)        │
                     └───────────┬─────────────┘
                                 │
                                 │
          ┌────────────────────────────────────────┐
          │                                        │
┌─────────┴──────────┐                ┌────────────┴─────────┐
│                    │                │                      │
│   Inbound Ports    │                │     Outbound Ports   │
│   (Application     │                │     (Repository      │
│    Services)       │                │      Interfaces)     │
│                    │                │                      │
└─────────┬──────────┘                └────────────┬─────────┘
          │                                        │
          │                                        │
┌─────────┴──────────┐                ┌────────────┴─────────┐
│                    │                │                      │
│  Inbound Adapters  │                │   Outbound Adapters  │
│  (REST Controller, │                │   (JPA Repository,   │
│   RabbitMQ         │                │    Redis, etc.)      │
│   Consumer)        │                │                      │
│                    │                │                      │
└────────────────────┘                └──────────────────────┘
```

## Features

- RESTful API for starship management with authentication
- Caching and message queue integration
- Health monitoring and metrics

## Building and Running

### Build the project

```bash
mvn clean install
```

### Run with Docker Compose

```bash
docker-compose up -d
```

This will start:
- The Starship Registry application
- Redis for caching
- RabbitMQ for messaging
- Keycloak for authentication

### Run tests

```bash
mvn test          # Unit tests
mvn verify        # Integration tests
```

## Useful Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Redis Documentation](https://redis.io/documentation)
- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
