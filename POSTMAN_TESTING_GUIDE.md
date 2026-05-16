# Postman Testing Guide - Auth Service

## Setup Postman Collection

### Option 1: Manual Setup

Follow these steps to create a Postman collection:

### Option 2: Import Pre-built Collection

[Scroll to bottom for JSON collection]

---

## Test Scenarios

### Scenario 1: User Registration

#### Test Case 1.1: Register Pharmacist

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Headers:**

```
Content-Type: application/json
```

**Body:**

```json
{
  "email": "arun.pharmacist@example.com",
  "password": "PharmacistPass123",
  "fullName": "Arun Kumar",
  "role": "PHARMACIST"
}
```

**Expected Response:** `201 Created`

```json
{
  "userId": 1,
  "email": "arun.pharmacist@example.com",
  "fullName": "Arun Kumar",
  "role": "PHARMACIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "createdAt": "2024-05-15T10:30:00",
  "message": "User registered successfully"
}
```

**Save the token:** Use Postman's **Tests** tab:

```javascript
if (pm.response.code === 201) {
  pm.environment.set("pharmacist_token", pm.response.json().token);
  pm.environment.set("pharmacist_email", pm.response.json().email);
}
```

---

#### Test Case 1.2: Register Distributor

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Body:**

```json
{
  "email": "rajesh.distributor@example.com",
  "password": "DistributorPass123",
  "fullName": "Rajesh Kumar",
  "role": "DISTRIBUTOR"
}
```

**Save token:**

```javascript
if (pm.response.code === 201) {
  pm.environment.set("distributor_token", pm.response.json().token);
}
```

---

#### Test Case 1.3: Register Manufacturer

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Body:**

```json
{
  "email": "pharma.manufacturer@example.com",
  "password": "ManufacturerPass123",
  "fullName": "Pharma Manufacturer Ltd",
  "role": "MANUFACTURER"
}
```

---

#### Test Case 1.4: Register Regulator

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Body:**

```json
{
  "email": "inspector.regulator@example.com",
  "password": "RegulatorPass123",
  "fullName": "Government Inspector",
  "role": "REGULATOR"
}
```

---

### Scenario 2: Duplicate Email Prevention

#### Test Case 2.1: Register with Existing Email

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Body:**

```json
{
  "email": "arun.pharmacist@example.com",
  "password": "DifferentPass123",
  "fullName": "Different Name",
  "role": "DISTRIBUTOR"
}
```

**Expected Response:** `400 Bad Request`

```json
{
  "message": "Email already registered"
}
```

---

### Scenario 3: Input Validation

#### Test Case 3.1: Invalid Email Format

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Body:**

```json
{
  "email": "not-an-email",
  "password": "ValidPass123",
  "fullName": "Test User",
  "role": "PHARMACIST"
}
```

**Expected Response:** `400 Bad Request` (Validation error)

---

#### Test Case 3.2: Password Too Short

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Body:**

```json
{
  "email": "user@example.com",
  "password": "short",
  "fullName": "Test User",
  "role": "PHARMACIST"
}
```

**Expected Response:** `400 Bad Request`

---

#### Test Case 3.3: Invalid Role

**Endpoint:** `POST http://localhost:8081/api/auth/register`

**Body:**

```json
{
  "email": "user@example.com",
  "password": "ValidPass123",
  "fullName": "Test User",
  "role": "INVALID_ROLE"
}
```

**Expected Response:** `400 Bad Request`

```json
{
  "message": "Invalid role. Must be one of: PHARMACIST, DISTRIBUTOR, MANUFACTURER, REGULATOR, ADMIN"
}
```

---

### Scenario 4: User Login

#### Test Case 4.1: Successful Login

**Endpoint:** `POST http://localhost:8081/api/auth/login`

**Headers:**

```
Content-Type: application/json
```

**Body:**

```json
{
  "email": "arun.pharmacist@example.com",
  "password": "PharmacistPass123"
}
```

**Expected Response:** `200 OK`

```json
{
  "userId": 1,
  "email": "arun.pharmacist@example.com",
  "fullName": "Arun Kumar",
  "role": "PHARMACIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "createdAt": "2024-05-15T10:30:00",
  "message": "Login successful"
}
```

---

#### Test Case 4.2: Invalid Password

**Endpoint:** `POST http://localhost:8081/api/auth/login`

**Body:**

```json
{
  "email": "arun.pharmacist@example.com",
  "password": "WrongPassword"
}
```

**Expected Response:** `401 Unauthorized`

```json
{
  "message": "Invalid email or password"
}
```

---

#### Test Case 4.3: Non-existent User

**Endpoint:** `POST http://localhost:8081/api/auth/login`

**Body:**

```json
{
  "email": "nonexistent@example.com",
  "password": "SomePassword123"
}
```

**Expected Response:** `401 Unauthorized`

```json
{
  "message": "Invalid email or password"
}
```

---

### Scenario 5: Health Check

#### Test Case 5.1: Service Health

**Endpoint:** `GET http://localhost:8081/api/auth/health`

**Expected Response:** `200 OK`

```
Auth Service is running
```

---

## Postman Environment Variables

Create a **PharmaTrace** environment with these variables:

| Variable             | Initial Value           | Current Value |
| -------------------- | ----------------------- | ------------- |
| `base_url`           | `http://localhost:8081` |               |
| `pharmacist_token`   | ``                      | (Auto-filled) |
| `distributor_token`  | ``                      | (Auto-filled) |
| `manufacturer_token` | ``                      | (Auto-filled) |
| `regulator_token`    | ``                      | (Auto-filled) |
| `pharmacist_email`   | ``                      | (Auto-filled) |

---

## How to Use Tokens in Requests

After registration/login, use the token in subsequent requests:

**Example Protected Request:**

```
GET {{base_url}}/api/protected-endpoint
Authorization: Bearer {{pharmacist_token}}
```

---

## Postman Collection JSON

Copy this JSON into Postman (Import → Paste Raw):

```json
{
  "info": {
    "name": "PharmaTrace Auth Service",
    "description": "Auth Service API testing collection",
    "version": "1.0.0"
  },
  "item": [
    {
      "name": "Health Check",
      "request": {
        "method": "GET",
        "url": "{{base_url}}/api/auth/health"
      },
      "response": []
    },
    {
      "name": "Register Pharmacist",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "url": "{{base_url}}/api/auth/register",
        "body": {
          "mode": "raw",
          "raw": "{\n  \"email\": \"arun.pharmacist@example.com\",\n  \"password\": \"PharmacistPass123\",\n  \"fullName\": \"Arun Kumar\",\n  \"role\": \"PHARMACIST\"\n}"
        }
      },
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "if (pm.response.code === 201) {",
              "    pm.environment.set(\"pharmacist_token\", pm.response.json().token);",
              "    pm.environment.set(\"pharmacist_email\", pm.response.json().email);",
              "    pm.test(\"Token saved to environment\", function() { pm.expect(pm.environment.get(\"pharmacist_token\")).to.be.truthy(); });",
              "}"
            ]
          }
        }
      ]
    },
    {
      "name": "Register Distributor",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "url": "{{base_url}}/api/auth/register",
        "body": {
          "mode": "raw",
          "raw": "{\n  \"email\": \"rajesh.distributor@example.com\",\n  \"password\": \"DistributorPass123\",\n  \"fullName\": \"Rajesh Kumar\",\n  \"role\": \"DISTRIBUTOR\"\n}"
        }
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "url": "{{base_url}}/api/auth/login",
        "body": {
          "mode": "raw",
          "raw": "{\n  \"email\": \"arun.pharmacist@example.com\",\n  \"password\": \"PharmacistPass123\"\n}"
        }
      }
    },
    {
      "name": "Login - Invalid Password",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "url": "{{base_url}}/api/auth/login",
        "body": {
          "mode": "raw",
          "raw": "{\n  \"email\": \"arun.pharmacist@example.com\",\n  \"password\": \"WrongPassword\"\n}"
        }
      }
    }
  ]
}
```

---

## Test Execution Checklist

- [ ] Health check responds with 200
- [ ] Register pharmacist with 201 response
- [ ] Register distributor with 201 response
- [ ] Register manufacturer with 201 response
- [ ] Register regulator with 201 response
- [ ] Duplicate email returns 400
- [ ] Invalid email format returns 400
- [ ] Short password returns 400
- [ ] Invalid role returns 400
- [ ] Login with correct credentials returns 200 with token
- [ ] Login with wrong password returns 401
- [ ] Login with non-existent email returns 401
- [ ] JWT tokens are properly formatted
- [ ] All tokens auto-save to environment variables

---

## Token Validation

To verify token contents, use https://jwt.io:

1. Go to https://jwt.io
2. Paste your JWT token from Postman response
3. Verify payload contains:
   - `role`: User's role
   - `sub`: User's email
   - `exp`: Expiration timestamp
   - `iat`: Issued at timestamp

---

## Troubleshooting Postman Tests

### 401 Unauthorized

- Check if token is expired (24-hour default)
- Verify token format in Authorization header: `Bearer <token>`
- Ensure environment variable `{{pharmacist_token}}` is set

### Connection Refused

- Verify Auth Service is running on `http://localhost:8081`
- Check firewall isn't blocking port 8081
- Restart the service and try again

### Validation Errors

- Verify email format (must contain @)
- Check password length (minimum 8 characters)
- Confirm role matches enum: PHARMACIST, DISTRIBUTOR, MANUFACTURER, REGULATOR, ADMIN
