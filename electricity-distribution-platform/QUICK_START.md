# Quick Start Guide (5 Minutes)

## Setup: Docker Only + Local Services

This project runs **PostgreSQL and Kafka in Docker** while **all services run locally** on your machine.

---

## Step 1: Start Docker Infrastructure (1 minute)

```bash
cd electricity-distribution-platform
docker-compose up -d
```

Verify:
```bash
docker-compose ps
# Should show 2 containers: eps-postgres and eps-kafka both "Up (healthy)"
```

---

## Step 2: Build Project (2 minutes)

```bash
mvn clean install -DskipTests
```

---

## Step 3: Start Services (2 minutes)

### Option A: Using IDE (Recommended)
- IntelliJ: Right-click any service → Run Maven Goal → `spring-boot:run`
- VS Code: Install Spring Boot Extension → Run from Boot Dashboard
- Eclipse: Create Maven Run Configurations

### Option B: Terminal (One per service)

```bash
# Terminal 1
cd auth-service
mvn spring-boot:run

# Terminal 2 (new terminal)
cd api-gateway
mvn spring-boot:run

# Terminal 3 (new terminal)
cd customer-service
mvn spring-boot:run

# Continue for other services as needed...
```

---

## Services Quick Reference

| Service | Port | Swagger UI |
|---------|------|-----------|
| API Gateway | 4004 | - |
| Auth Service | 8080 | http://localhost:8080/swagger-ui.html |
| Customer Service | 8081 | http://localhost:8081/swagger-ui.html |
| Meter Service | 8082 | http://localhost:8082/swagger-ui.html |
| Billing Service | 8090 | http://localhost:8090/swagger-ui.html |
| Complaint Service | 8092 | http://localhost:8092/swagger-ui.html |
| Platform Service | 8085 | http://localhost:8085/swagger-ui.html |

**Note:** You don't need to start all 17 services. Start only the ones you're developing/testing.

---

## Test API (1 minute)

### 1. Login
```bash
curl -X POST http://localhost:4004/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"superadmin","password":"password"}'
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

### 2. Test (replace with token from above)
```bash
curl http://localhost:4004/api/customers \
  -H "Authorization: Bearer <access_token>"
```

---

## Configuration

**All defaults already set for localhost:**
- Database: `localhost:5432` (user: `admin_user`, pass: `password`)
- Kafka: `localhost:9094`
- No config changes needed! ✅

---

## Stop Services

```bash
# Stop individual: CTRL+C in terminal
# Stop Docker: docker-compose down
```

---

## Troubleshooting

**Port already in use?**
```bash
# Find what's using port 8080
lsof -i :8080  # Mac/Linux
netstat -ano | findstr :8080  # Windows

# Kill it
kill -9 <PID>  # Mac/Linux
taskkill /PID <PID> /F  # Windows
```

**Database connection error?**
```bash
docker-compose restart postgres
sleep 5  # Wait for restart
```

**Kafka not found?**
```bash
docker-compose restart kafka
sleep 5  # Wait for restart
```

---

## Full Guide

For detailed setup, troubleshooting, and development workflow, see **`LOCAL_SETUP_GUIDE.md`**

---

**Ready?** Go! 🚀
