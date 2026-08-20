# Product API

A RESTful backend API for managing products, built with Java and Spring Boot.

This project demonstrates practical backend engineering fundamentals including layered architecture, PostgreSQL persistence, REST API design, input validation, database migrations, automated testing, pagination, and Dockerized development.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Maven
- JUnit 5
- Mockito
- Docker
- Docker Compose

## Architecture

The application follows a layered architecture:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Spring Data JPA / Hibernate
  ↓
PostgreSQL
```

Responsibilities are separated between layers:

- **Controller** — Handles HTTP requests, input validation, and API responses.
- **Service** — Handles application use cases and business logic.
- **Repository** — Provides persistence operations while keeping database access separate from the service layer.
- **DTOs** — Define the API request and response contracts without exposing JPA entities directly.
- **Mapper** — Converts persistence/domain objects into API response DTOs.

## Features

- Create products
- Retrieve a product by ID
- Update products
- Delete products
- Retrieve paginated product lists
- Optional product descriptions
- Request validation
- Consistent API error responses
- PostgreSQL persistence
- Flyway database migrations
- Transactional updates
- Unit and integration testing
- Dockerized application and database
- PostgreSQL container healthcheck

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/products` | Create a product |
| `GET` | `/products/{id}` | Get a product by ID |
| `GET` | `/products?page=0&size=20` | Get a paginated product list |
| `PUT` | `/products/{id}` | Update a product |
| `DELETE` | `/products/{id}` | Delete a product |

### Pagination

The product list supports page-based pagination.

Default values:

```text
page = 0
size = 20
```

Validation rules:

```text
page >= 0
1 <= size <= 100
```

Example:

```http
GET /products?page=0&size=20
```

## Example Product

```json
{
  "id": 1,
  "name": "Mechanical Keyboard",
  "price": 250.00,
  "stock": 10,
  "description": "Wireless mechanical keyboard"
}
```

`description` is optional and has a maximum length of 500 characters.

## How to Run

### Using Docker Compose

Docker is the recommended way to run the application and PostgreSQL together.

From the project root:

```bash
docker compose up --build
```

The API will be available at:

```text
http://localhost:8080
```

Docker Compose starts both the Spring Boot application and PostgreSQL database.

A PostgreSQL healthcheck is used so that the application waits for the database to become ready before starting.

To stop the containers:

```bash
docker compose down
```

> `docker compose down -v` also deletes the PostgreSQL volume and its persisted data. Use it only when the database should intentionally be reset.

## How to Test

Run the automated test suite with the Maven Wrapper.

### Windows

```bash
mvnw.cmd test
```

### macOS / Linux

```bash
./mvnw test
```

The test suite covers important behavior including:

- Product creation
- Product retrieval
- Product updates
- Product deletion
- Validation failures
- Product-not-found errors
- Pagination
- Service behavior
- API integration with the persistence layer

Tests use an isolated test environment so they do not depend on existing development data.

## Database Migrations

Database schema changes are managed using Flyway.

Migration files are stored in:

```text
src/main/resources/db/migration/
```

Current migrations include:

```text
V1__create_products_table.sql
V2__add_product_description.sql
```

Flyway keeps database schema changes versioned and reproducible across environments.

Hibernate validates the database schema rather than being responsible for automatically evolving it.

## Key Engineering Decisions

### Separate API DTOs from JPA Entities

Request and response DTOs are separated from persistence entities.

This prevents the database model from automatically becoming the public API contract and gives the application explicit control over which fields clients can send and receive.

### Layered Architecture

HTTP handling, application logic, and persistence responsibilities are separated into Controller, Service, and Repository layers.

This keeps responsibilities clear and makes individual components easier to test and change.

### Database-Level Pagination

Pagination is performed at the database level instead of loading all products into application memory and then selecting a subset.

The API also limits the maximum page size to prevent clients from requesting excessively large result sets.

### Version-Controlled Database Schema

Flyway migrations are used as the source of truth for database schema evolution.

Existing migrations are treated as history. New schema changes are introduced through new migration versions instead of modifying migrations that may already have been applied in other environments.

### Consistent Error Responses

Expected API failures such as validation errors and missing products are converted into consistent error responses instead of exposing framework or internal implementation details directly to clients.

### Test Isolation

Integration tests create their own test data and clean up between tests.

Tests therefore do not rely on fixed database IDs or existing development data.

### Dockerized Development Environment

Spring Boot and PostgreSQL can run together using Docker Compose.

Container networking uses the PostgreSQL service name for internal communication, while a healthcheck ensures that the database is ready before the application starts.
