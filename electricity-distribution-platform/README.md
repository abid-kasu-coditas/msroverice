# Electricity Distribution Management Platform

A production-grade multi-tenant electricity distribution management system built with Spring Boot microservices architecture. Implements event-driven architecture with Kafka, gRPC for inter-service communication, and PostgreSQL for persistence.

## Project Overview

This platform manages:

- **Customer Lifecycle**: Registration, profile management, account status
- **Electricity Connections**: Connection requests, meter provisioning, installations
- **Meter Management**: Meter readings, consumption tracking (via gRPC)
- **Billing**: Bill generation, bill management
- **Payment Processing**: Payment collections, transactions
- **Complaint Management**: Customer complaints, resolution tracking
- **Notifications**: Event-driven notifications (Kafka consumer)
- **Analytics**: Business analytics and reporting
- **Audit Logging**: Complete audit trail for compliance

## Architecture

### Microservices

```
api-gateway (Port 8080)
├── auth-service (Port 8081)
├── customer-service (Port 8082)
├── connection-service (Port 8083)
├── meter-service (Port 8084, gRPC 9084)
├── billing-service (Port 8085)
├── payment-service (Port 8086)
├── complaint-service (Port 8087)
├── notification-service (Consumer)
├── analytics-service (Consumer)
└── audit-service (Port 8089)
```

### Technology Stack

- **Framework**: Spring Boot 3.4.1
- **Language**: Java 21
- **Database**: PostgreSQL 16
- **Message Broker**: Apache Kafka 7.6.0
- **RPC**: gRPC 1.69.0
- **Protocol Buffers**: 4.29.1
- **Authentication**: JWT (jjwt 0.12.6)
- **API Documentation**: SpringDoc OpenAPI 2.6.0
- **Testing**: Spring Boot Test, Testcontainers
- **Container**: Docker & Docker Compose

### Design Patterns

- **API Gateway Pattern**: Central routing and JWT validation
- **Service-to-Service Communication**: gRPC for synchronous, Kafka for asynchronous
- **Event-Driven Architecture**: Kafka for event publishing and consumption
- **Multi-Tenant**: Schema-based tenant isolation via PostgreSQL
- **CQRS Elements**: Separate read/write models in analytics
- **Circuit Breaker Ready**: Spring Cloud patterns

## Project Structure

```
electricity-distribution-platform/
├── pom.xml (parent)
├── docker-compose.yml
├── infrastructure/
│   └── init-databases.sh
│
├── api-gateway/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       └── main/
│           ├── java/com/eps/apigateway/
│           │   └── ApiGatewayApplication.java
│           └── resources/
│               └── application.properties
│
├── auth-service/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── src/main/java/com/eps/authservice/
│   │   ├── model/ (User, UserRole)
│   │   ├── repository/ (UserRepository)
│   │   ├── service/ (UserService)
│   │   ├── controller/ (AuthController)
│   │   ├── dto/ (LoginRequest, LoginResponse, UserRequest, UserResponse)
│   │   ├── exception/ (GlobalExceptionHandler, Custom Exceptions)
│   │   ├── util/ (JwtUtil)
│   │   ├── config/ (SecurityConfig)
│   │   └── AuthServiceApplication.java
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql (seed users)
│
├── customer-service/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── src/main/java/com/eps/customerservice/
│   │   ├── model/ (Customer, CustomerStatus)
│   │   ├── repository/ (CustomerRepository)
│   │   ├── service/ (CustomerService)
│   │   ├── controller/ (CustomerController)
│   │   ├── dto/ (CustomerRequest, CustomerResponse)
│   │   ├── mapper/ (CustomerMapper)
│   │   ├── exception/ (GlobalExceptionHandler, Custom Exceptions)
│   │   └── CustomerServiceApplication.java
│   └── src/main/resources/
│       ├── application.properties
│       └── proto/customer_event.proto
│
├── connection-service/
├── meter-service/ (gRPC Server)
├── billing-service/
├── payment-service/
├── complaint-service/
├── notification-service/ (Kafka Consumer)
├── analytics-service/ (Kafka Consumer)
├── audit-service/
└── integration-tests/
```

## User Roles

Platform supports hierarchical user roles:

### Platform Roles

- `SUPER_ADMIN`: Full system access
- `MANAGEMENT`: Management operations
- `STATE_HEAD`: State-level operations
- `DISTRICT_HEAD`: District-level operations
- `CITY_HEAD`: City-level operations
- `SALES_POC`: Sales operations
- `CRM`: Customer relationship management
- `BILLER`: Billing operations
- `TECHNICIAN`: Technical operations

### Client Roles

- `CLIENT_OPERATIONS`: Client operations
- `CLIENT_SALES_POC`: Client sales representative
- `CLIENT_BPO_EMPLOYEE`: BPO employee
- `CLIENT_BPO_MANAGER_L1`: Level 1 manager
- `CLIENT_BPO_MANAGER_L2`: Level 2 manager

## API Endpoints

### Authentication Service

```
POST   /auth/register              # Register new user
POST   /auth/login                 # User login (returns JWT)
POST   /auth/refresh-token         # Refresh access token
GET    /auth/users/{id}            # Get user by ID
GET    /auth/users/username/{username}  # Get user by username
GET    /auth/users                 # List all users
PUT    /auth/users/{id}            # Update user
DELETE /auth/users/{id}            # Delete user
```

### Customer Service

```
GET    /api/customers              # List all customers
POST   /api/customers              # Create new customer
GET    /api/customers/{id}         # Get customer by ID
PUT    /api/customers/{id}         # Update customer
DELETE /api/customers/{id}         # Delete customer
```

### Meter Service (REST)

```
GET    /api/meters                 # List meters
POST   /api/meters                 # Create meter account
GET    /api/meters/{id}            # Get meter details
```

### gRPC Services

**MeterService** (Port 9084):

```proto
service MeterService {
  rpc CreateMeterAccount(CreateMeterAccountRequest) returns (CreateMeterAccountResponse);
}
```

## Getting Started

### Prerequisites

- Java 21 JDK
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 16 (optional if using Docker)
- Kafka 7.6+ (optional if using Docker)

### Quick Start with Docker

1. **Build all services**:

```bash
mvn clean package -DskipTests -f pom.xml
```

2. **Build Docker images** (from root directory):

```bash
docker-compose build
```

3. **Start the platform**:

```bash
docker-compose up -d
```

4. **Verify services are running**:

```bash
docker-compose ps
```

5. **Access the services**:

- API Gateway: http://localhost:8080
- Swagger UI Auth Service: http://localhost:8081/swagger-ui.html
- Swagger UI Customer Service: http://localhost:8082/swagger-ui.html

### Local Development Setup

1. **Install dependencies**:

```bash
mvn clean install
```

2. **Start PostgreSQL**:

```bash
docker run -d \
  --name postgres-eps \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16-alpine
```

3. **Start Kafka** (or use docker-compose for Kafka only):

```bash
docker run -d \
  --name kafka-eps \
  -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -p 9092:9092 \
  confluentinc/cp-kafka:7.6.0
```

4. **Start individual services**:

```bash
# Terminal 1
cd auth-service
mvn spring-boot:run

# Terminal 2
cd api-gateway
mvn spring-boot:run

# Terminal 3
cd customer-service
mvn spring-boot:run

# etc...
```

## Testing

### Run unit tests:

```bash
mvn test
```

### Run integration tests:

```bash
mvn verify
```

### Test with Testcontainers:

Integration tests use Testcontainers to spin up PostgreSQL and Kafka automatically.

## API Examples

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "superadmin",
    "password": "password"
  }'
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

### Create Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Authorization: Bearer <accessToken>" \
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

### Get Customers

```bash
curl http://localhost:8080/api/customers \
  -H "Authorization: Bearer <accessToken>"
```

## Key Features

### 1. **Multi-Tenant Architecture**

- Schema-based isolation per tenant
- Automatic tenant context handling
- Secure data segregation

### 2. **Event-Driven Architecture**

- Kafka for asynchronous communication
- Event sourcing for audit trail
- Event-driven notifications

### 3. **gRPC Communication**

- Low-latency inter-service RPC
- Meter Service exposes gRPC endpoints
- Protocol buffer definitions

### 4. **Security**

- JWT-based authentication
- Role-based access control (RBAC)
- Global exception handling
- Input validation and sanitization

### 5. **API Documentation**

- Swagger UI on each service
- OpenAPI 3.0 specification
- Interactive API testing

### 6. **Observability**

- Structured logging
- Audit service for compliance
- Analytics service for insights

## Database Schema

Each service has its own database following schema:

```
auth_service_db
├── users

customer_service_db
├── customers

meter_service_db
├── meter_accounts
├── meter_readings

billing_service_db
├── bills
├── bill_items

payment_service_db
├── payments
├── transactions

complaint_service_db
├── complaints

audit_service_db
├── audit_logs
```

## Kafka Topics

```
customer-events         # Customer lifecycle events
connection-events       # Connection events
meter-readings         # Meter reading events
bill-generated         # Bill generation events
payment-processed      # Payment events
complaint-created      # Complaint events
audit-events           # Audit trail events
```

## Deployment

### Docker Compose Deployment

```bash
docker-compose up -d
```

### Kubernetes Deployment (Future)

Helm charts ready for Kubernetes deployment.

## Performance Considerations

- **Connection Pooling**: HikariCP (default)
- **JPA Caching**: Second-level cache via Hibernate
- **Kafka Partitioning**: By customer ID for ordering
- **gRPC Optimization**: Binary protocol, HTTP/2
- **Database Indexing**: On frequently queried fields

## Security Best Practices

1. **Secrets Management**: Use environment variables for sensitive data
2. **HTTPS/TLS**: Enabled in production
3. **JWT Expiration**: Tokens expire after 24 hours
4. **Refresh Tokens**: Separate refresh token lifecycle
5. **Input Validation**: All endpoints validate input
6. **CORS**: Configured for cross-origin requests

## Monitoring & Logging

- **Centralized Logging**: ELK stack ready (not included)
- **Metrics**: Micrometer integration (ready for Prometheus)
- **Distributed Tracing**: OpenTelemetry ready
- **Health Checks**: Spring Boot Actuator endpoints

## Contributing

1. Follow the established code structure
2. Match the reference architecture patterns exactly
3. Write unit tests for new features
4. Update API documentation
5. Follow Spring Boot best practices

## License

Proprietary - Electricity Provider Service Platform

## Contact

For questions or support, contact the development team.

---

**Version**: 1.0.0
**Last Updated**: January 2025
**Status**: Production-Ready Architecture
