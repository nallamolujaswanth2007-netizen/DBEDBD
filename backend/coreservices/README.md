# Inventory Core Services

Spring Boot core service for inventory monitoring, built like the demo `coreservices` project.

## Run

```bash
mvnw.cmd spring-boot:run
```

## PostgreSQL

Default database settings are in `src/main/resources/application.properties`:

- Database: `inventory_monitoring_db`
- Username: `postgres`
- Password: `postgres`
- Port: `5432`

## URLs

- Auth API: `http://localhost:8001/users/signin`
- Inventory API: `http://localhost:8001/inventory/items`
- Spring Swagger UI: `http://localhost:8001/swagger-ui.html`

Seeded login:

- Email: `admin@inventory.com`
- Password: `admin123`
