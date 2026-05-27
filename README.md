# Inventory Monitoring System

Full-stack demo project following the same backend pattern as the reference project:

- `frontend` - React + Vite dashboard
- `backend/coreservices` - Spring Boot core service with PostgreSQL/JPA business logic
- `backend/gateway` - FastAPI gateway that forwards frontend requests to Spring Boot
- `database` - PostgreSQL schema and seed scripts for pgAdmin

## Database Setup With pgAdmin

1. Open pgAdmin and create a database named `inventory_monitoring_db`.
2. Use username `postgres` and password `postgres`, or update `backend/coreservices/src/main/resources/application.properties`.
3. Optional: run `database/schema.sql` and `database/seed.sql` manually in pgAdmin. The Spring Boot core service also creates and seeds tables automatically on startup.

## Run Spring Boot Core Services

```bash
cd backend\coreservices
mvnw.cmd spring-boot:run
```

Core service URLs:

- Health/API root: `http://localhost:8001/inventory/items`
- Spring Swagger UI: `http://localhost:8001/swagger-ui.html`

## Run FastAPI Gateway

```bash
cd backend\gateway
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
copy .env.example .env
python run.py
```

Gateway URLs:

- Gateway health: `http://localhost:8000/`
- FastAPI Swagger UI: `http://localhost:8000/docs`
- Frontend API entry: `http://localhost:8000/inventoryservice/items`

## Run Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend URL:

- `http://localhost:5173`

Demo login:

- Email: `admin@inventory.com`
- Password: `admin123`

Start order: PostgreSQL, Spring Boot `coreservices`, FastAPI `gateway`, then React `frontend`.
