# Microservices System Documentation

This document provides an overview of the microservices system consisting of six services developed using Spring Boot 3.2.5 and Java 17. The system includes a Config Server, Service Registry, API Gateway, Student Service, Fee Collection Service, and Receipt Service.

## Services Overview

1. **Config Server**: Manages all microservices configurations.
2. **Service Registry**: Registers all microservices for service discovery.
3. **API Gateway**: Routes requests to respective microservices.
4. **Student Service**: Registers and manages student data.
5. **Fee Collection Service**: Collects fees from students.
6. **Receipt Service**: Generates and stores receipts for fee transactions.

## Getting Started

Follow these steps to test the microservices:

1. Start the Config Server.
2. Start the Service Registry.
3. Start the API Gateway service.
4. Start the Student Service, Fee Collection Service, and Receipt Service.

Access Swagger UI for each service:

- Student Service: [http://localhost:9001/swagger-ui/index.html](http://localhost:9001/swagger-ui/index.html)
- Receipt Service: [http://localhost:9002/swagger-ui/index.html](http://localhost:9002/swagger-ui/index.html)
- Fee Collection Service: [http://localhost:9000/swagger-ui/index.html](http://localhost:9000/swagger-ui/index.html)

You can interact with each service using Swagger UI and refer to the respective microservice README files for more details.

## Dependencies

- `spring-cloud-starter-netflix-eureka-server`: Service registry for microservices.
- `spring-cloud-starter-netflix-eureka-client`: Eureka client for service registration.
- `spring-cloud-starter-config`: Configuration management for microservices.
- `spring-boot-starter-webflux`: WebFlux support for reactive web development.
- `spring-retry`: Retry support for handling service unavailability.
- `spring-boot-starter-data-jpa`: JPA support for database operations.
- `h2 database`: In-memory database for development.
- `mapstruct`: Mapping library for DTO to entity mapping.
- `spring-boot-starter-actuator`: Provides production-ready features.
- `HTTPExchange`: Communicating with other microservices.

## Testing

Junit test cases have been written for each microservice to ensure proper functionality and exception handling. Error messages are appropriately displayed in case of service unavailability.

