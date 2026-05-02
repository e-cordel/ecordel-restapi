# ecordel-restapi - AGENTS Guide

## Project Purpose
Spring Boot REST API that serves cordel and frontend data for web and mobile clients.

## Tech Stack
- Java 25
- Spring Boot 4
- Spring Web / Validation / Security / OAuth2 Resource Server
- Spring Data JPA
- Flyway
- PostgreSQL
- Maven
- Testcontainers (integration tests)

## Deployment
- Production deployment target: Heroku.
- Ensure environment variables and profile-specific settings match Heroku runtime expectations.

## Key Commands
- Build: `mvn clean package`
- Run tests: `mvn test`
- Run local API (local profile):
  - `mvn spring-boot:run -Dspring-boot.run.profiles=local`

## Local Dependencies
- Java 25
- Maven 3.9+
- Docker 20+

## Local Database
- Start PostgreSQL via compose: `docker compose up -d`
- Development data can be seeded using `src/test/resources/db/data/data.sql`.
- Local DB state is persisted under `tmp` (delete folder to reset).

## API Contract
- OpenAPI spec: `openapi.yaml`
- Keep API contract updated when changing endpoints, payloads, status codes, or auth behavior.

## Consumer Repositories
- Web consumer: `../ecordel-frontend`
- Mobile consumer: `../ecordel-mobile`
- Coordinate breaking API changes with both consumers.

## Quality and Security
- Checkstyle uses Google Java Style.
- Dependency checks run in Maven verify phase.
- New Relic agent artifacts are handled in build packaging.
