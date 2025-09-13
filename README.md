# Users & Pets Management

Single-module Spring Boot 3 application (Java 17) with H2 database.

## Run
1. Build:mvn clean install
2. Run:mvn spring-boot:run
3. Swagger UI: http://localhost:8080/swagger-ui.html  
H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:userspetsdb`, user `sa`, no password)

## Features
- Entities: User, Address, Pet
- APIs for create/update/delete (soft-delete) and queries described in the assignment.
- Soft-delete implemented via `alive` boolean.
- Tests: unit and integration tests included.

## Design notes
- Single-module chosen for simplicity and to avoid cyclic module dependencies.
- Address is normalized entity.
- If strict "same physical pet with multiple owners" is required, extend model with `Ownership` join table.
