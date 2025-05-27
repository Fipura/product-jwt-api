# Product JWT API

A secure REST API for managing products with user registration, login and JWT-based authentication.

## Features

- User registration and login
- JWT token authentication
- Role-based authorization (`USER`, `ADMIN`)
- Protected CRUD operations for products (only for `ADMIN`)
- PostgreSQL + Spring Boot + Spring Security
- Clean architecture with DTOs, services, and filters

## Technologies

- Java 21
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JJWT library)
- Lombok

## How to Run

1. Update your database settings in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/productdb
spring.datasource.username=youruser
spring.datasource.password=yourpass
spring.security.jwt.secret=your-jwt-secret-key
```

2. Run with:

```bash
./mvnw spring-boot:run
```

## API Endpoints

### Authentication

| Method | Endpoint | Body | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | `{ "username": "", "password": "" }` | Register new user |
| POST | `/api/auth/login` | `{ "username": "", "password": "" }` | Login and get token |

**Include JWT in requests:**
```
Authorization: Bearer <your-jwt-token>
```

### Product (requires ADMIN role)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update existing product |
| DELETE | `/api/products/{id}` | Delete product |

## Sample JSON for products

```json
{
  "name": "Laptop",
  "description": "High-end device",
  "price": 1499.99
}
```
