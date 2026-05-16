# PharmaTrace - Medicine Authenticity & Cold Chain Monitoring Platform

A cloud-native microservices platform addressing the counterfeit medicine crisis in India, which kills ~200,000 people annually. Built with polyglot technology stack (Java Spring Boot + Python FastAPI).

## 🎯 Problem Statement

Counterfeit medicines are a major public health threat, especially in developing countries. PharmaTrace enables:

- ✅ Verification of drug authenticity via QR codes
- ✅ Real-time cold chain monitoring
- ✅ Anomaly detection for tampered batches
- ✅ Role-based access for stakeholders (Pharmacists, Distributors, Regulators)

## 🏗️ Architecture

### Microservices

| Service                   | Technology       | Port | Purpose                                         |
| ------------------------- | ---------------- | ---- | ----------------------------------------------- |
| **Auth Service**          | Java Spring Boot | 8081 | User authentication & JWT tokens                |
| **Drug Registry Service** | Java Spring Boot | 8082 | Drug batch management & QR codes                |
| **Verification Service**  | Python FastAPI   | 8083 | QR validation & authenticity checks             |
| **Anomaly ML Service**    | Python FastAPI   | 8084 | Isolation Forest ML model for anomaly detection |
| **Alert Service**         | Python FastAPI   | 8085 | Trigger alerts & log suspicious activities      |
| **Frontend**              | React            | 3000 | Pharmacist portal & dashboard                   |

### Tech Stack

- **Languages:** Java 17, Python 3.10+
- **Frameworks:** Spring Boot 3.1, FastAPI 0.104+
- **Databases:** MySQL (Java services), SQLite/PostgreSQL (Python services)
- **Authentication:** JWT with role-based access control (RBAC)
- **ML:** scikit-learn (Isolation Forest)
- **Containerization:** Docker & Docker Compose
- **API Testing:** Postman

---

## 📋 User Roles

| Role             | Responsibilities                           |
| ---------------- | ------------------------------------------ |
| **PHARMACIST**   | Scan QR codes at retail, report suspicions |
| **DISTRIBUTOR**  | Manage drug distribution, track shipments  |
| **MANUFACTURER** | Create QR codes, register batches          |
| **REGULATOR**    | Audit, investigate, enforce compliance     |
| **ADMIN**        | Platform administration                    |

---

## 🚀 Quick Start

### Prerequisites

```bash
- Java 17+
- Maven 3.9+
- Python 3.10+
- MySQL 8+
- Docker & Docker Compose
- Git
```

### Local Setup

#### 1. Clone Repository

```bash
git clone <repo-url>
cd pharmatrace
```

#### 2. Create MySQL Database

```sql
CREATE DATABASE pharmatrace_auth;
CREATE DATABASE pharmatrace_registry;
```

#### 3. Build & Run Services

**Auth Service** (Java)

```bash
cd auth-service
mvn clean install
mvn spring-boot:run
# Runs on http://localhost:8081
```

**Drug Registry Service** (Java)

```bash
cd drug-registry-service
mvn clean install
mvn spring-boot:run
# Runs on http://localhost:8082
```

**Verification Service** (Python)

```bash
cd verification-service
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
python main.py
# Runs on http://localhost:8083
```

**Anomaly ML Service** (Python)

```bash
cd anomaly-ml-service
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python main.py
# Runs on http://localhost:8084
```

**Alert Service** (Python)

```bash
cd alert-service
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python main.py
# Runs on http://localhost:8085
```

#### 4. Run with Docker Compose (Recommended)

```bash
docker-compose up -d
```

---

## 📚 Service Documentation

- [Auth Service Guide](AUTH_SERVICE_GUIDE.md) - JWT authentication, roles, user management
- [Postman Testing Guide](POSTMAN_TESTING_GUIDE.md) - Complete API testing collection
- Drug Registry Service Guide (Coming Soon)
- Verification Service Guide (Coming Soon)
- Anomaly ML Service Guide (Coming Soon)
- Alert Service Guide (Coming Soon)

---

## 🧪 Testing

### Test Auth Service

1. See [POSTMAN_TESTING_GUIDE.md](POSTMAN_TESTING_GUIDE.md) for comprehensive testing
2. Key endpoints:
   - `POST /api/auth/register` - User registration
   - `POST /api/auth/login` - User login (returns JWT token)
   - `GET /api/auth/health` - Service health check

### Example: Register & Login

**Register Pharmacist:**

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "pharmacist@example.com",
    "password": "SecurePass123",
    "fullName": "Arun Kumar",
    "role": "PHARMACIST"
  }'
```

**Response:**

```json
{
  "userId": 1,
  "email": "pharmacist@example.com",
  "fullName": "Arun Kumar",
  "role": "PHARMACIST",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "User registered successfully"
}
```

---

## 📁 Project Structure

```
pharmatrace/
├── auth-service/                  # Java Spring Boot
│   ├── pom.xml
│   ├── src/main/java/...
│   ├── Dockerfile
│   └── README.md
├── drug-registry-service/         # Java Spring Boot (Coming Soon)
├── verification-service/          # Python FastAPI (Coming Soon)
├── anomaly-ml-service/           # Python FastAPI + ML (Coming Soon)
├── alert-service/                # Python FastAPI (Coming Soon)
├── frontend/                      # React (Coming Soon)
├── docker-compose.yml
├── AUTH_SERVICE_GUIDE.md
├── POSTMAN_TESTING_GUIDE.md
└── README.md (this file)
```

---

## 🔐 Security Features

✅ **JWT Authentication** - Secure stateless token-based auth  
✅ **Password Hashing** - BCrypt encryption for passwords  
✅ **Role-Based Access Control (RBAC)** - 5 distinct user roles  
✅ **CORS Support** - Cross-origin requests handled  
✅ **Input Validation** - Email, password, and role validation  
✅ **Rate Limiting** - (Future enhancement)  
✅ **SQL Injection Prevention** - JPA prevents SQL injection

---

## 🐳 Docker Deployment

### Build Individual Service Images

```bash
cd auth-service
docker build -t pharmatrace-auth:1.0 .

cd ../drug-registry-service
docker build -t pharmatrace-registry:1.0 .

cd ../verification-service
docker build -t pharmatrace-verify:1.0 .
```

### Run All Services with Docker Compose

```bash
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

---

## 🔄 Data Flow (Example Scenario)

1. **Pharmacist Registration** → Auth Service creates user account, returns JWT
2. **Drug Manufacturer** → Posts drug batch to Drug Registry Service
3. **Pharmacist Scan** → Scans QR code → Verification Service validates batch
4. **Anomaly Detection** → ML Service analyzes scan patterns
5. **Alert Triggered** → Alert Service logs suspicious activity

---

## 📊 Database Schema (Auth Service)

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🎓 Learning Resources

- [Spring Security + JWT Tutorial](https://spring.io/guides/tutorials/spring-security-and-angular-js/)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8949)
- [FastAPI Documentation](https://fastapi.tiangolo.com/)
- [Docker Tutorial](https://docs.docker.com/get-started/)

---

## 🚦 Current Status

| Service               | Status         | Completion |
| --------------------- | -------------- | ---------- |
| Auth Service          | ✅ Complete    | 100%       |
| Drug Registry Service | 🔄 In Progress | 0%         |
| Verification Service  | 🔄 In Progress | 0%         |
| Anomaly ML Service    | 🔄 In Progress | 0%         |
| Alert Service         | 🔄 In Progress | 0%         |
| Frontend              | 🔄 In Progress | 0%         |

---

## 📋 Next Steps

1. ✅ Build Auth Service (DONE)
2. 🔄 Build Drug Registry Service
3. 🔄 Build Verification Service
4. 🔄 Build Anomaly ML Service
5. 🔄 Build Alert Service
6. 🔄 Create React Frontend
7. 🔄 Integration testing
8. 🔄 Deployment to cloud (AWS/Azure)

---

## 👨‍💻 Development Team

- **Developer:** Student (KIIT, Final Year, CS)
- **Placement Goal:** Showcase cloud-native microservices architecture
- **Interview Talking Points:**
  - Polyglot microservices (Java + Python)
  - JWT authentication & RBAC
  - ML model integration
  - Docker containerization
  - RESTful API design
  - Problem-solving impact (countering fake medicines)

---

## 📝 License

MIT License - Feel free to use for portfolio projects

---

## 📞 Contact

For questions or improvements, open an issue or reach out to the development team.

---

**Last Updated:** May 15, 2024  
**Version:** 1.0.0  
**Status:** Active Development
