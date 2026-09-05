# 🧭 Model Compass — AI Model Discovery & Recommendation API

[![Java](https://img.shields.io/badge/Java-21%2F25-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)](https://www.postgresql.org/)
[![OpenAPI](https://img.shields.io/badge/OpenAPI%203-Swagger%20UI-green?logo=swagger)](https://swagger.io/)
[![Build & Test](https://img.shields.io/badge/Tests-JUnit%205%20%7C%20Mockito%20%7C%20MockMvc-success)]()

An enterprise-ready Spring Boot RESTful API engineered to catalog, evaluate, filter, and recommend Artificial Intelligence models (LLMs, Vision, Code generation, Multimodal) using a multi-attribute rule-based scoring engine and side-by-side comparison capabilities.

---

## Architecture & Design Patterns

The application follows a clean, decoupled Layered Architecture:

* **Client Layer:** HTTP / JSON communication via Postman, frontend clients, or Swagger UI.
* **Controller Layer (`com.modelcompass.controller`):** Exposes RESTful endpoints, handles HTTP status codes, triggers payload validations, and provides OpenAPI documentation.
* **Service Layer (`com.modelcompass.service`):** Contains core business logic, the model comparison engine, and the weighted recommendation algorithm.
* **Data Access Layer (`com.modelcompass.repository`):** Spring Data JPA repositories interfacing with PostgreSQL using custom query methods and pagination.
* **DTO Decoupling Pattern:** Entities (`AIModel`) are never directly exposed to HTTP clients; request/response DTOs ensure encapsulation.
* **Centralized Exception Handling:** Managed by `@RestControllerAdvice` to guarantee uniform error responses (`400 Bad Request`, `404 Not Found`).

---

## Tech Stack

* **Language:** Java 21 / Java 25
* **Framework:** Spring Boot 3.5.3 (Web, Data JPA, Validation)
* **Database:** PostgreSQL with HikariCP connection pooling
* **Documentation:** Springdoc OpenAPI 3 (v2.8.5) & Swagger UI
* **Testing:** JUnit 5, Mockito, Spring MockMvc

---

## Key Features

1. **Model Catalog (Full CRUD):** Complete lifecycle operations with validation on model attributes.
2. **Search & Multi-Attribute Filtering:** Query models by provider, category, or natural language keywords across descriptions.
3. **Pagination & Dynamic Sorting:** Scalable catalog retrieval via Spring Data JPA `Pageable` (`?page=0&size=10&sort=name,asc`).
4. **Side-by-Side Model Comparison:** Compare an arbitrary list of models by IDs to contrast context limits, benchmarks, and target tasks (`POST /api/models/compare`).
5. **Rule-Based Recommendation Engine:** Evaluates category compatibility (+40), provider alignment (+30), and task keyword relevancy (+30) to score and rank models (`POST /api/models/recommend`).
6. **Centralized Error Handling:** Predictable JSON schemas for validation errors and resource exceptions.
7. **Automated Testing:** Comprehensive unit tests for business isolation and MockMvc slice tests for endpoint contracts.

---
## Getting Started

### 1. Database Setup
Create a PostgreSQL database for the application:
```sql
 CREATE DATABASE modelcompass;
```
 
### 2. Configure Credentials
Verify database settings in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/modelcompass
spring.datasource.username=postgres
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Springdoc OpenAPI & Swagger UI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui/index.html
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha