# Local Development Setup Guide

This guide configures the project to run **Kafka and PostgreSQL in Docker** while **all microservices run locally** on your machine.

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│ Your Local Machine                                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────────┐  ┌──────────────┐  ┌────────────┐ │
│  │ auth-service    │  │ api-gateway  │  │ customer   │ │
│  │ (Port 8080)     │  │ (Port 4004)  │  │ (Port 8081)│ │
│  └─────────────────┘  └──────────────┘  └────────────┘ │
│                                                         │
│  ┌─────────────────┐  ┌──────────────┐  ┌────────────┐ │
│  │ meter-service   │  │ billing-svc  │  │ complaint  │ │
│  │ (Port 8082)     │  │ (Port 8090)  │  │ (Port 8092)│ │
│  └─────────────────┘  └──────────────┘  └────────────┘ │
│                                                         │
│  ... (all other services locally)                      │
│                                                         │
└──────────────┬──────────────┬────────────────────────────┘
               │              │
               ▼              ▼
        ┌────────────┬────────────────┐
        │  Docker    │     Docker     │
        │  PostgreSQL│     Kafka      │
        │  :5432     │     :9092      │
        └────────────┴────────────────┘
```

## Prerequisites

- **Java**: JDK 21 or later
- **Maven**: 3.8+
- **Docker & Docker Compose**: Latest version
- **Git**: For cloning (optional)

## Step 1: Start Docker Containers (PostgreSQL & Kafka)

### 1.1 Navigate to project root
```bash
cd electricity-distribution-platform
```

### 1.2 Start the containers
```bash
docker-compose up -d
```

### 1.3 Verify containers are running
```bash
docker-compose ps
```

**Expected output:**
```
NAME            STATUS
eps-postgres    Up (healthy)
eps-kafka       Up (healthy)
```

### 1.4 Verify database connectivity
```bash
psql -h localhost -U admin_user -d epsdb -c "SELECT 1;"
```

### 1.5 Verify Kafka connectivity
```bash
docker exec eps-kafka kafka-topics.sh --bootstrap-server localhost:9092 --list
```

---

## Step 2: Build All Services

### 2.1 Build from project root
```bash
mvn clean install -DskipTests
```

This compiles all services to `/target` directories.

---

## Step 3: Run Services Locally

Each service runs on a different port. You can start them in parallel or sequentially.

### Option A: Sequential (One Terminal Per Service)

**Terminal 1 - Auth Service (Port 8080)**
```bash
cd auth-service
mvn spring-boot:run
```

**Terminal 2 - API Gateway (Port 4004)**
```bash
cd api-gateway
mvn spring-boot:run
```

**Terminal 3 - Customer Service (Port 8081)**
```bash
cd customer-service
mvn spring-boot:run
```

**Terminal 4 - Meter Service (Port 8082)**
```bash
cd meter-service
mvn spring-boot:run
```

*Continue for other services...*

### Option B: Parallel using IDE

Use your IDE (IntelliJ, Eclipse) to run multiple services simultaneously:

1. **IntelliJ IDEA**:
   - Right-click service's `pom.xml` → "Run Maven Goal" → `spring-boot:run`
   - Repeat for each service in different Run Configurations

2. **VS Code with Spring Boot Extension**:
   - Install "Spring Boot Extension Pack"
   - Click "Boot Dashboard" → Run each service

3. **Eclipse**:
   - File → Run Configurations → Create new Maven Build
   - Goal: `spring-boot:run`
   - Create one per service

### Option C: Custom Script (Optional)

Create `start-services.sh`:

```bash
#!/bin/bash

SERVICES=(
    "auth-service:8080"
    "api-gateway:4004"
    "customer-service:8081"
    "meter-service:8082"
    "connection-service:8095"
    "billing-service:8090"
    "meter-reading-service:8089"
    "payment-service:8093"
    "complaint-service:8092"
    "platform-service:8085"
    "client-onboarding-service:8086"
    "tenant-provisioning-service:8086"
    "tenant-user-service:8088"
    "geography-service:8087"
    "notification-service:8094"
    "employee-service:9000"
    "connection-service:8095"
)

for service in "${SERVICES[@]}"; do
    IFS=':' read -r name port <<< "$service"
    (cd "$name" && mvn spring-boot:run) &
    echo "Started $name on port $port"
    sleep 2
done

wait
```

Make it executable:
```bash
chmod +x start-services.sh
./start-services.sh
```

---

## Step 4: Verify Services Are Running

### Check service health
```bash
# Auth Service
curl http://localhost:8080/actuator/health

# API Gateway
curl http://localhost:4004/actuator/health

# Customer Service
curl http://localhost:8081/actuator/health
```

### Expected response
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "kafka": {
      "status": "UP"
    }
  }
}
```

---

## Service Ports Reference

| Service | Port | URL | Swagger |
|---------|------|-----|---------|
| API Gateway | 4004 | http://localhost:4004 | N/A |
| Auth Service | 8080 | http://localhost:8080 | http://localhost:8080/swagger-ui.html |
| Customer Service | 8081 | http://localhost:8081 | http://localhost:8081/swagger-ui.html |
| Meter Service | 8082 | http://localhost:8082 | http://localhost:8082/swagger-ui.html |
| Analytics Service | 8083 | http://localhost:8083 | http://localhost:8083/swagger-ui.html |
| Notification Service | 8094 | http://localhost:8094 | http://localhost:8094/swagger-ui.html |
| Platform Service | 8085 | http://localhost:8085 | http://localhost:8085/swagger-ui.html |
| Client Onboarding | 8086 | http://localhost:8086 | http://localhost:8086/swagger-ui.html |
| Geography Service | 8087 | http://localhost:8087 | http://localhost:8087/swagger-ui.html |
| Tenant User Service | 8088 | http://localhost:8088 (gRPC) | N/A |
| Meter Reading Service | 8089 | http://localhost:8089 | http://localhost:8089/swagger-ui.html |
| Billing Service | 8090 | http://localhost:8090 | http://localhost:8090/swagger-ui.html |
| Platform Billing Service | 8091 | http://localhost:8091 | http://localhost:8091/swagger-ui.html |
| Complaint Service | 8092 | http://localhost:8092 | http://localhost:8092/swagger-ui.html |
| Payment Service | 8093 | http://localhost:8093 | http://localhost:8093/swagger-ui.html |
| Connection Service | 8095 | http://localhost:8095 | http://localhost:8095/swagger-ui.html |

---

## Configuration for Local Development

### Database Connections

All services use environment variables that default to localhost:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/epsdb
spring.datasource.username=admin_user
spring.datasource.password=password
```

**To override**, set environment variables:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/epsdb
export SPRING_DATASOURCE_USERNAME=admin_user
export SPRING_DATASOURCE_PASSWORD=password
```

### Kafka Connections

All services connect to Kafka at `localhost:9094`:

```properties
spring.kafka.bootstrap-servers=localhost:9094
```

**To override**:
```bash
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9094
```

### gRPC Services

**Meter Service (gRPC Server)**:
```
Host: localhost
Port: 9082
```

**Tenant User Service (gRPC Server)**:
```
Host: localhost
Port: 9088
```

---

## Testing

### 1. Login to get JWT Token

```bash
curl -X POST http://localhost:4004/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "superadmin",
    "password": "password"
  }'
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

### 2. Test with JWT Token

```bash
curl -X GET http://localhost:4004/api/customers \
  -H "Authorization: Bearer <access_token>"
```

### 3. Create Customer

```bash
curl -X POST http://localhost:4004/api/customers \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "address": "123 Main St",
    "city": "Mumbai",
    "district": "Mumbai",
    "state": "Maharashtra"
  }'
```

---

## Debugging

### Check PostgreSQL Connection

```bash
# Connect to database
psql -h localhost -U admin_user -d epsdb

# List tables
\dt

# List schemas
\dn

# Exit
\q
```

### Check Kafka Topics

```bash
# List topics
docker exec eps-kafka kafka-topics.sh --bootstrap-server localhost:9092 --list

# Describe topic
docker exec eps-kafka kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic customer-onboarded

# Read messages from topic
docker exec eps-kafka kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic customer-onboarded --from-beginning
```

### Check Service Logs

**Auth Service logs:**
```bash
cd auth-service
mvn spring-boot:run 2>&1 | grep -i error
```

**Filter by application name:**
```bash
# Terminal running service
# Look for ERROR, WARN messages
# Or add logging configuration
```

### Enable Debug Logging

Add to service's `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.eps=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=DEBUG
```

---

## Stopping Services

### Stop individual service
```
CTRL+C in the terminal running the service
```

### Stop all Docker containers
```bash
docker-compose down
```

### Stop Docker containers but keep volumes
```bash
docker-compose down -v
```

### Remove everything (containers, images, volumes)
```bash
docker-compose down -v --rmi all
```

---

## Troubleshooting

### Issue: "Connection refused" when connecting to database

**Solution:**
```bash
# Verify Docker container is running
docker-compose ps

# If not running, start it
docker-compose up -d postgres

# Wait 10 seconds for database to start
sleep 10
```

### Issue: Kafka connection timeout

**Solution:**
```bash
# Verify Kafka container
docker-compose ps

# Restart Kafka
docker-compose restart kafka

# Wait for Kafka to be healthy
docker-compose ps  # Check STATUS column
```

### Issue: "Address already in use" on port 8080

**Solution:**
1. Find process using port:
```bash
# Linux/Mac
lsof -i :8080

# Windows
netstat -ano | findstr :8080
```

2. Kill process:
```bash
# Linux/Mac
kill -9 <PID>

# Windows
taskkill /PID <PID> /F
```

Or use a different port:
```bash
cd auth-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8085"
```

### Issue: Flyway migration fails

**Solution:**
```bash
# Connect to database and reset schema
psql -h localhost -U admin_user -d epsdb

# Drop and recreate schema
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

# Re-run migrations
# Restart the service
```

### Issue: Services can't find Kafka topic

**Solution:**
```bash
# Kafka auto-creates topics, but sometimes it needs a restart
docker-compose restart kafka

# Or manually create topics
docker exec eps-kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic customer-onboarded --partitions 3 --replication-factor 1
```

---

## Performance Tips for Local Development

1. **Use SSD**: Database and Kafka perform better on solid-state drives

2. **Allocate Memory**: Ensure Docker has sufficient memory
   - Docker Desktop: Settings → Resources → Memory: 4GB+

3. **Close Unnecessary Services**: Only run services you're developing
   - Not all 17 services need to be running simultaneously

4. **Enable Parallel Builds**:
```bash
mvn clean install -T 1C  # 1 thread per core
```

5. **Skip Tests during Development**:
```bash
mvn spring-boot:run -DskipTests
```

6. **Use IDE Debugging**:
   - Set breakpoints and debug directly in IDE
   - Faster than terminal-based debugging

---

## Development Workflow

### 1. Make code changes
```bash
cd <service-name>
# Edit code
```

### 2. Rebuild service
```bash
mvn clean compile
```

### 3. Restart service
```
CTRL+C in the terminal
mvn spring-boot:run
```

### 4. Test changes
```bash
curl http://localhost:<port>/api/endpoint
```

### 5. View logs
```
Check terminal output
```

---

## Docker Commands Reference

```bash
# Start containers
docker-compose up -d

# Stop containers
docker-compose down

# View logs
docker-compose logs -f postgres
docker-compose logs -f kafka

# Access PostgreSQL shell
docker exec -it eps-postgres psql -U admin_user -d epsdb

# Access Kafka shell
docker exec -it eps-kafka bash

# Restart specific service
docker-compose restart postgres

# Remove all data (dangerous!)
docker-compose down -v
```

---

## Summary

✅ **Setup Complete When:**
- Docker containers running: `docker-compose ps` shows 2 UP containers
- Database connected: Can query PostgreSQL
- Kafka running: Can list Kafka topics
- All services started: Can access Swagger UIs
- JWT login works: Can get access token

**Services are ready for local development!** 🎉
