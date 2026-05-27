# Backend

The backend has two services, matching the demo project structure:

- `coreservices` - Spring Boot service connected to PostgreSQL
- `gateway` - FastAPI service that exposes frontend-facing endpoints and proxies to Spring Boot

Run PostgreSQL first, then start `coreservices` on port `8001`, then `gateway` on port `8000`.
