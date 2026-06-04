# Quick Reference - Electricity Distribution Management Platform

## 🚀 Get Started in 5 Minutes

### Option 1: Full Docker Stack (Recommended)

```bash
cd electricity-distribution-platform
mvn clean package -DskipTests
docker-compose up -d
# Wait 10 seconds for services to start
# Access: http://localhost:8080
```

### Option 2: Local Development

```bash
# Terminal 1: PostgreSQL
docker run -d --name postgres-eps -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16

# Terminal 2: Kafka
docker run -d --name kafka-eps -e KAFKA_ZOOKEEPER_CONNECT=localhost:2181 \
  -p 9092:9092 confluentinc/cp-kafka:7.6.0

# Terminal 3: Auth Service
cd auth-service && mvn spring-boot:run

# Terminal 4: Customer Service
cd customer-service && mvn spring-boot:run
```

---

## 📖 Essential Documents (Read in Order)

1. **README.md** - Start here (5 min read)
2. **ARCHITECTURE.md** - Understand design (10 min read)
3. **IMPLEMENTATION_GUIDE.md** - Before you code (15 min read)
4. **PROJECT_STATUS.md** - Track progress
5. **API_EXAMPLES.http** - Test endpoints

---

## 🔑 Default Credentials

**Login Endpoint**: `POST http://localhost:8080/auth/login`

| Username   | Password | Role        |
| ---------- | -------- | ----------- |
| superadmin | password | SUPER_ADMIN |
| management | password | MANAGEMENT  |
| technician | password | TECHNICIAN  |
| biller     | password | BILLER      |
| crm        | password | CRM         |

---

## 📡 API Endpoints Quick Map

### Auth Service (Port 8081)

```
Register:    POST   /auth/register
Login:       POST   /auth/login
Get Users:   GET    /auth/users
Get User:    GET    /auth/users/{id}
Update User: PUT    /auth/users/{id}
Delete User: DELETE /auth/users/{id}
```

### Customer Service (Port 8082)

```
List:   GET    /api/customers
Create: POST   /api/customers
Get:    GET    /api/customers/{id}
Update: PUT    /api/customers/{id}
Delete: DELETE /api/customers/{id}
```

### Other Services (8083-8089)

Same CRUD pattern as Customer Service

---

## 🛠️ Development Workflow

### Step 1: Choose a Service to Implement

Pick from: Connection, Meter, Billing, Payment, Complaint, Notification, Analytics, Audit

### Step 2: Follow the Template Pattern

**Exact pattern** (copy from Customer Service):

1. Create Entity class (JPA with UUID id)
2. Create Status enum (if needed)
3. Create DTOs (Request + Response)
4. Create Mapper (toModel/toDTO)
5. Create Repository (JpaRepository)
6. Create Service (business logic)
7. Create Controller (REST endpoints)
8. Create GlobalExceptionHandler
9. Update application.properties
10. Update docker-compose.yml

### Step 3: Build & Test

```bash
# Build single service
mvn -pl service-name clean package

# Build Docker image
docker build -t service-name:latest service-name/

# Run and test
docker run -p 8083:8083 service-name:latest
curl http://localhost:8083/api/service
```

### Step 4: Commit & Document

- Add service to README.md
- Update API_EXAMPLES.http
- Mark task complete in PROJECT_STATUS.md

---

## 🎯 Code Checklist for Each Service

```
Entity Layer:
  [ ] Entity class with @Entity, @Table
  [ ] UUID id with @GeneratedValue(UUID)
  [ ] Status enum (if applicable)
  [ ] JPA annotations (@Column, @Enumerated, etc.)

DTO Layer:
  [ ] RequestDTO with @Valid, @NotBlank annotations
  [ ] ResponseDTO with all fields
  [ ] Bean validation annotations

Mapper Layer:
  [ ] Static toModel(RequestDTO) method
  [ ] Static toDTO(Entity) method

Repository Layer:
  [ ] Extends JpaRepository<Entity, UUID>
  [ ] Custom query methods
  [ ] @Query annotations if needed

Service Layer:
  [ ] @Service class
  [ ] Constructor injection of Repository
  [ ] CRUD methods (5): get all, get by id, create, update, delete
  [ ] Business logic implementation
  [ ] Exception throwing

Controller Layer:
  [ ] @RestController on class
  [ ] @RequestMapping("/api/endpoint")
  [ ] @PreAuthorize on methods
  [ ] @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
  [ ] @RequestBody, @PathVariable annotations
  [ ] @Valid annotation on DTOs
  [ ] ResponseEntity<> return types

Exception Handler:
  [ ] @RestControllerAdvice class
  [ ] @ExceptionHandler methods for each exception type
  [ ] Custom exception classes extending RuntimeException
  [ ] Proper HTTP status codes (400, 404, 409, etc.)

Configuration:
  [ ] application.properties updated
  [ ] database name unique
  [ ] port number unique (8081-8089)
  [ ] spring.jpa.hibernate.ddl-auto=update
```

---

## 🧪 Quick Testing

### Login and Get Token

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"superadmin","password":"password"}'

# Copy the accessToken from response
```

### Create Customer

```bash
TOKEN="<paste-accessToken-here>"

curl -X POST http://localhost:8080/api/customers \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "phone": "9999999999",
    "address": "123 Test St",
    "city": "TestCity",
    "district": "TestDistrict",
    "state": "TestState"
  }'
```

### View Swagger UI

```
Auth Service:     http://localhost:8081/swagger-ui.html
Customer Service: http://localhost:8082/swagger-ui.html
```

---

## 🐳 Docker Commands

```bash
# Start all services
docker-compose up -d

# View running services
docker-compose ps

# View logs of specific service
docker-compose logs -f auth-service

# Stop services
docker-compose stop

# Stop and remove everything (including volumes)
docker-compose down -v

# Build new images
docker-compose build

# Build and start
docker-compose up -d --build
```

---

## 📦 Service Port Assignments

| Service      | Port | gRPC | Database              |
| ------------ | ---- | ---- | --------------------- |
| API Gateway  | 8080 | -    | -                     |
| Auth         | 8081 | -    | auth_service_db       |
| Customer     | 8082 | -    | customer_service_db   |
| Connection   | 8083 | -    | connection_service_db |
| Meter        | 8084 | 9084 | meter_service_db      |
| Billing      | 8085 | -    | billing_service_db    |
| Payment      | 8086 | -    | payment_service_db    |
| Complaint    | 8087 | -    | complaint_service_db  |
| Notification | 8088 | -    | -                     |
| Audit        | 8089 | -    | audit_service_db      |
| Analytics    | -    | -    | -                     |
| PostgreSQL   | 5432 | -    | 8 schemas             |
| Kafka        | 9092 | -    | -                     |

---

## 🔐 Security Reminders

1. **Always use**: `@PreAuthorize("hasAnyRole('...')")` on endpoints
2. **Password policy**: Min 8 chars, uppercase, lowercase, numbers, special chars
3. **Tokens**: 24-hour expiration, use refresh tokens for renewal
4. **JWT Secret**: Long string, 32+ characters, keep in environment variables
5. **HTTPS**: Enable in production, use TLS certificates
6. **CORS**: Configure for specific domains in production

---

## 🚨 Common Issues & Solutions

### Issue: "Connection refused to PostgreSQL"

**Solution**:

```bash
docker-compose ps  # Check if postgres is running
docker logs eps-postgres  # Check logs
docker-compose restart postgres
```

### Issue: "Port already in use"

**Solution**:

```bash
# Find what's using port 8080
lsof -i :8080
# Kill the process
kill -9 <PID>
```

### Issue: "JWT validation failed"

**Solution**:

- Make sure token is in "Bearer <token>" format
- Check token expiration (24 hours)
- Verify you copied the full token

### Issue: "No routes available"

**Solution**:

- Check API Gateway logs: `docker logs eps-api-gateway`
- Verify service is running: `docker-compose ps`
- Check service URL is correct in gateway config

### Issue: "Database migration error"

**Solution**:

```bash
# Reset database
docker-compose down -v
docker-compose up postgres
# Wait for healthy status, then start other services
```

---

## 📊 Project Stats at a Glance

| Metric                | Value          |
| --------------------- | -------------- |
| Total Services        | 11 + 1 Gateway |
| Completed Services    | 3              |
| Skeleton Services     | 8              |
| Total LOC (Completed) | ~1,300         |
| Total LOC (Estimated) | ~10,200        |
| Database Schemas      | 8              |
| User Roles            | 14             |
| REST Endpoints        | 50+            |
| Kafka Topics          | 7              |
| Docker Containers     | 13             |
| Microservice Ports    | 8081-8089      |

---

## ⚡ Performance Tips

1. **Batch Operations**: Group multiple calls when possible
2. **Caching**: Use Spring Cache for frequently accessed data
3. **Connection Pooling**: HikariCP (default) with 10-20 connections
4. **Database Indexing**: Always index frequently queried columns
5. **Kafka Partitions**: Use customer ID as partition key for ordering
6. **gRPC**: Use for performance-critical inter-service calls
7. **Async Processing**: Use Kafka for non-blocking operations

---

## 🔄 CI/CD Ready

### Build Pipeline

```bash
# Stage 1: Build
mvn clean package -DskipTests

# Stage 2: Test
mvn test

# Stage 3: Package
docker build -t service:latest .

# Stage 4: Push to registry
docker push registry/service:latest
```

---

## 🎓 Reference Patterns

### Add a new entity field

```java
@Column(columnName = "field_name", nullable = false)
private FieldType fieldName;
```

### Add validation

```java
@NotBlank(message = "Field cannot be blank")
@Email(message = "Must be valid email")
private String email;
```

### Add custom exception

```java
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
```

### Add repository query

```java
@Query("SELECT e FROM Entity e WHERE e.status = :status")
List<Entity> findByStatus(@Param("status") Status status);
```

### Add Kafka publishing

```java
kafkaTemplate.send("topic-name", objectMapper.writeValueAsString(event));
```

### Add Kafka consuming

```java
@KafkaListener(topics = "topic-name", groupId = "consumer-group")
public void listen(String message) {
    // Process event
}
```

---

## 📞 Quick Help

### Need to add a new field to an entity?

1. Add field to Entity class with @Column annotations
2. Update RequestDTO
3. Update ResponseDTO
4. Update Mapper (toModel/toDTO)
5. Service automatically picks up changes via JPA
6. Restart service (migration runs automatically)

### Need to add a new endpoint?

1. Add method to Service class
2. Add method to Controller with @GetMapping/@PostMapping etc.
3. Add @PreAuthorize with required roles
4. Test with curl or Swagger UI
5. Document in README.md

### Need to add Kafka event?

1. Create event class (POJO with eventId, eventDate, eventType, data)
2. Publish via kafkaTemplate.send(topic, event)
3. Subscribe via @KafkaListener in consumer service
4. Add topic to docker-compose if needed

---

## 🏁 Next Steps

1. ✅ **Understand** - Read README.md, ARCHITECTURE.md
2. 🔨 **Start** - Begin with Connection Service
3. 📝 **Follow** - Use IMPLEMENTATION_GUIDE.md patterns
4. 🧪 **Test** - Use API_EXAMPLES.http
5. 📊 **Track** - Update PROJECT_STATUS.md
6. 🔄 **Repeat** - Implement remaining 7 services

---

## 📚 File Navigation

| File                    | Purpose                                | Read Time |
| ----------------------- | -------------------------------------- | --------- |
| README.md               | Getting started, architecture overview | 5 min     |
| ARCHITECTURE.md         | Detailed architecture decisions        | 10 min    |
| IMPLEMENTATION_GUIDE.md | Code patterns and examples             | 20 min    |
| PROJECT_STATUS.md       | Progress tracking                      | 5 min     |
| API_EXAMPLES.http       | API testing examples                   | 10 min    |
| This file               | Quick reference                        | 3 min     |

---

## 🎯 Success Criteria

Your implementation is successful when:

- ✅ All 11 services implemented
- ✅ All CRUD endpoints working
- ✅ Kafka events flowing correctly
- ✅ gRPC communication working
- ✅ Docker Compose stack runs cleanly
- ✅ Integration tests passing
- ✅ API request files complete
- ✅ All endpoints documented

---

**Version**: 1.0.0
**Last Updated**: January 2025
**Status**: Ready for Development
