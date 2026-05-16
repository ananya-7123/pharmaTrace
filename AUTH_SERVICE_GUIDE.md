# Auth Service - Complete Documentation

## Overview

The Auth Service is a Java Spring Boot microservice that handles:

- User registration with role-based access control (RBAC)
- User authentication using JWT tokens
- Role-based authorization (PHARMACIST, DISTRIBUTOR, MANUFACTURER, REGULATOR, ADMIN)
- User profile management

## Project Structure

```
auth-service/
├── pom.xml                          # Maven dependencies
├── Dockerfile                       # Container configuration
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/pharmatrace/auth/
    │   │   ├── AuthServiceApplication.java    # Spring Boot entry point
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java       # Spring Security configuration
    │   │   ├── controller/
    │   │   │   └── AuthController.java       # REST API endpoints
    │   │   ├── dto/
    │   │   │   ├── LoginRequest.java         # Login request payload
    │   │   │   ├── RegisterRequest.java      # Registration request payload
    │   │   │   └── AuthResponse.java         # Unified response payload
    │   │   ├── model/
    │   │   │   └── User.java                 # User entity with roles
    │   │   ├── repository/
    │   │   │   └── UserRepository.java       # JPA repository for Users
    │   │   ├── security/
    │   │   │   ├── JwtUtil.java              # JWT token generation & validation
    │   │   │   └── JwtAuthFilter.java        # Request filter for JWT validation
    │   │   └── service/
    │   │       └── AuthService.java          # Business logic
    │   └── resources/
    │       └── application.properties         # Spring Boot configuration
    └── test/                                  # Unit tests (to be added)
```

## Setup Instructions

### 1. Prerequisites

- Java 17+ installed
- Maven 3.9+ installed
- MySQL 8+ running locally
- Git

### 2. Create Database

```sql
CREATE DATABASE pharmatrace_auth;
USE pharmatrace_auth;
```

### 3. Build the Service

```bash
cd auth-service
mvn clean install
```

### 4. Run Locally

```bash
mvn spring-boot:run
```

The service will start on: `http://localhost:8081`

### 5. Docker Build & Run

```bash
# Build Docker image
docker build -t pharmatrace-auth:1.0 .

# Run container
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/pharmatrace_auth \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  pharmatrace-auth:1.0
```

## API Endpoints

### 1. Register User

**Endpoint:** `POST /api/auth/register`

**Request:**

```json
{
  "email": "pharmacist1@example.com",
  "password": "SecurePass123",
  "fullName": "Arun Kumar",
  "role": "PHARMACIST"
}
```

**Roles:** `PHARMACIST`, `DISTRIBUTOR`, `MANUFACTURER`, `REGULATOR`, `ADMIN`

**Response (201 Created):**

```json
{
  "userId": 1,
  "email": "pharmacist1@example.com",
  "fullName": "Arun Kumar",
  "role": "PHARMACIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "createdAt": "2024-05-15T10:30:00",
  "message": "User registered successfully"
}
```

**Error Response (400 Bad Request):**

```json
{
  "message": "Email already registered"
}
```

---

### 2. Login User

**Endpoint:** `POST /api/auth/login`

**Request:**

```json
{
  "email": "pharmacist1@example.com",
  "password": "SecurePass123"
}
```

**Response (200 OK):**

```json
{
  "userId": 1,
  "email": "pharmacist1@example.com",
  "fullName": "Arun Kumar",
  "role": "PHARMACIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "createdAt": "2024-05-15T10:30:00",
  "message": "Login successful"
}
```

**Error Response (401 Unauthorized):**

```json
{
  "message": "Invalid email or password"
}
```

---

### 3. Health Check

**Endpoint:** `GET /api/auth/health`

**Response:**

```
Auth Service is running
```

---

## JWT Token Format

The JWT token contains:

- **Header:** Algorithm (HS256) and token type
- **Payload:**
  - `sub` (subject): User email
  - `role`: User role
  - `iat` (issued at): Token creation time
  - `exp` (expiration): Token expiration (24 hours by default)
- **Signature:** Signed with secret key

**Example decoded token:**

```json
{
  "role": "PHARMACIST",
  "sub": "pharmacist1@example.com",
  "iat": 1715860200,
  "exp": 1715946600
}
```

---

## Using JWT Token in Requests

All protected endpoints require the JWT token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Key Features

### 1. Password Security

- Passwords are hashed using BCrypt algorithm
- Passwords never stored in plain text
- Minimum 8 characters required

### 2. Role-Based Access Control (RBAC)

Five predefined roles with different responsibilities:

- **PHARMACIST:** Retail pharmacy operators, scan QR codes
- **DISTRIBUTOR:** Wholesale drug distribution
- **MANUFACTURER:** Drug production and QR creation
- **REGULATOR:** Government regulatory oversight
- **ADMIN:** Platform administration

### 3. JWT Authentication

- Stateless authentication (no session storage needed)
- Token expires after 24 hours
- Token includes user email and role for quick authorization checks
- Each request filtered and validated at JwtAuthFilter

### 4. Data Validation

- Email validation using Jakarta Validation
- Password strength requirements
- Role validation against enum values

---

## Security Configuration

**SecurityConfig.java** handles:

- CSRF protection disabled (for REST APIs)
- Route-level authorization:
  - Public routes: `/api/auth/register`, `/api/auth/login`, `/api/auth/health`
  - Protected routes: Everything else requires valid JWT
- Stateless session management (no cookies)
- JWT filter integrated before authentication filter

---

## Configuration (application.properties)

```properties
# Server
server.port=8081

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/pharmatrace_auth
spring.datasource.username=root
spring.datasource.password=password

# JWT (customizable)
jwt.secret=pharmatrace_secret_key_that_should_be_at_least_256_bits_long_for_security
jwt.expiration=86400000  # 24 hours in milliseconds

# Hibernate
spring.jpa.hibernate.ddl-auto=update
```

---

## Database Schema

### Users Table

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## Testing with Postman

See `POSTMAN_GUIDE.md` for detailed Postman collection and testing instructions.

---

## Dependencies

| Dependency         | Version | Purpose                         |
| ------------------ | ------- | ------------------------------- |
| Spring Boot Web    | 3.1.5   | REST API framework              |
| Spring Data JPA    | 3.1.5   | Database ORM                    |
| Spring Security    | 3.1.5   | Authentication & authorization  |
| MySQL Connector    | 8.0.33  | MySQL database driver           |
| JJWT               | 0.12.3  | JWT token creation & validation |
| Lombok             | Latest  | Boilerplate reduction           |
| Jakarta Validation | Latest  | Input validation                |

---

## Troubleshooting

### Connection refused to MySQL

```
Error: com.mysql.cj.jdbc.exceptions.CommunicationsException
```

**Solution:** Ensure MySQL is running on `localhost:3306`

### Column name issue

```
Error: Unknown column 'role' in 'field list'
```

**Solution:** Spring JPA will auto-create the table. Run application once.

### JWT validation fails

```
Error: Token validation failed
```

**Solution:** Check if token expired (24-hour default) or secret key matches

---

## Future Enhancements

1. **Email verification** - OTP-based email confirmation
2. **2FA (Two-Factor Authentication)** - SMS or authenticator app support
3. **Token refresh** - Implement refresh token rotation
4. **Audit logging** - Track all authentication events
5. **Rate limiting** - Prevent brute force attacks
6. **OAuth2 integration** - Google/GitHub login support

---

## Next Steps

1. Start the Auth Service locally or with Docker
2. Test all endpoints using Postman (see POSTMAN_GUIDE.md)
3. Integrate with Drug Registry Service
4. Add unit tests for business logic
5. Deploy to production environment
