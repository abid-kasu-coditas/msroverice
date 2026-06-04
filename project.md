# Claude Code Implementation Prompt
## Electricity Provider Service — Spring Boot Microservices

---

## CONTEXT & REFERENCE

You are implementing a production-grade Java Spring Boot microservices project called **Electricity Provider Service**. This project must strictly follow the architecture, coding standards, file structure, technology choices, and implementation patterns from the reference repository:

**Reference Repo:** `https://github.com/chrisblakely01/java-spring-microservices`

Clone and study the reference repo first. Every architectural and coding decision must mirror it. The only thing changing is the domain — from a patient management system to an electricity provider platform. Package names, class naming conventions, REST controller patterns, gRPC proto definitions, Kafka producer/consumer setup, Docker/infrastructure setup — all must match the reference repo's style exactly.

---

## PROJECT OVERVIEW

**Aniruddha's company** is building a PAN India electricity distribution platform. Electricity providers (Reliance Power, Tata Power, MSEB, etc.) onboard onto this platform. Customers can buy connections from multiple providers through a single system. The system is multi-tenant — each client company gets its own isolated database schema.

---

## MICROSERVICES TO BUILD

Map the reference repo's services to this domain:

| Reference Service    | This Project's Service       | Role                                                                 |
|----------------------|------------------------------|----------------------------------------------------------------------|
| `patient-service`    | `customer-service`           | Manages customer onboarding, meter assignments, connection requests  |
| `billing-service`    | `meter-service`              | Handles meter reads (photo uploads), bill generation via gRPC       |
| `analytics-service`  | `analytics-service`          | Consumes Kafka events for usage analytics and reporting              |
| `auth-service`       | `auth-service`               | JWT-based auth for all roles (Super-Admin, Management, Sales, etc.)  |
| `api-gateway`        | `api-gateway`                | Single entry point, JWT validation, route forwarding                 |
| `infrastructure`     | `infrastructure`             | Docker Compose, LocalStack AWS CDK, Kafka, PostgreSQL setup          |
| `integration-tests`  | `integration-tests`          | End-to-end integration tests mirroring reference repo structure      |

---

## TECHNOLOGY STACK

Use **exactly** the same versions and libraries as the reference repo:

- **Java 17**
- **Spring Boot 3.x** (match pom.xml versions from reference)
- **Maven** (multi-module monorepo structure, one root `pom.xml`)
- **PostgreSQL** — one DB per service (multi-tenant: one schema/DB per client company in `customer-service`)
- **Apache Kafka** — event streaming (customer onboarded event, bill generated event)
- **gRPC + Protocol Buffers** — `customer-service` calls `meter-service` via gRPC (mirrors patient → billing)
- **Spring Security + JWT (jjwt 0.12.6)** — in `auth-service` and validated in `api-gateway`
- **Spring Data JPA + Hibernate**
- **Docker + Docker Compose** — same structure as `infrastructure/` in reference
- **LocalStack** — AWS CDK in Java for infra (same as reference `infrastructure/` module)
- **SpringDoc OpenAPI (springdoc-openapi-starter-webmvc-ui 2.6.0)** — Swagger on each service
- **Spring Cloud Gateway** — for `api-gateway`
- **Testcontainers + JUnit 5** — integration tests (mirror reference `integration-tests/` module)

---

## MONOREPO STRUCTURE

Mirror this structure exactly (same folder names pattern, adapted for this domain):

```
electricity-provider-service/
├── api-gateway/
├── auth-service/
├── customer-service/           ← was patient-service
├── meter-service/              ← was billing-service
├── analytics-service/
├── infrastructure/
├── integration-tests/
├── api-requests/               ← .http files for all endpoints
├── grpc-requests/
│   └── meter-service/          ← .proto and test gRPC requests
├── .gitignore
└── README.md
```

Each service has this internal Maven structure (identical to reference):

```
<service-name>/
├── src/
│   ├── main/
│   │   ├── java/com/eps/<servicename>/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   ├── dto/
│   │   │   ├── grpc/            ← gRPC client/server classes
│   │   │   ├── kafka/           ← producers and consumers
│   │   │   ├── config/
│   │   │   ├── exception/
│   │   │   └── <ServiceName>Application.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       └── proto/           ← .proto files (meter-service only)
│   └── test/
│       └── java/com/eps/<servicename>/
├── Dockerfile
└── pom.xml
```

Base package: `com.eps` (Electricity Provider Service). So for customer-service it's `com.eps.customerservice`, for meter-service it's `com.eps.meterservice`, etc. Match this pattern to how reference uses `com.pm.patientservice`.

---

## DOMAIN MODEL

### customer-service

**Entities:**

```java
// Customer.java
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String district;
    private String state;
    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;   // PENDING, ACTIVE, DISCONNECTED
    private LocalDate registeredAt;
    private boolean active;
    // getters/setters
}

// ConnectionRequest.java — a customer's request for a new connection from a provider
@Entity
@Table(name = "connection_requests")
public class ConnectionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID customerId;
    private UUID clientCompanyId;    // which electricity provider
    @Enumerated(EnumType.STRING)
    private MeterType meterType;     // RESIDENTIAL, INDUSTRIAL, SOLAR
    @Enumerated(EnumType.STRING)
    private RequestStatus status;    // SUBMITTED, APPROVED, REJECTED, INSTALLED
    private LocalDateTime createdAt;
}
```

**REST endpoints** (mirror PatientController pattern exactly):
- `GET    /api/customers`              — list all customers (paginated)
- `POST   /api/customers`              — onboard new customer (CRM role)
- `GET    /api/customers/{id}`         — get customer by ID
- `PUT    /api/customers/{id}`         — update customer
- `DELETE /api/customers/{id}`         — remove customer
- `POST   /api/customers/{id}/connection-requests`   — submit connection request
- `GET    /api/customers/{id}/connection-requests`   — list connection requests

**Kafka Producer** — when a customer is successfully onboarded, publish a `CustomerEvent` protobuf message to topic `customer-events` (mirrors how patient-service publishes to `patient-events`).

**gRPC Client** — when a connection request is APPROVED, call `meter-service` via gRPC to provision a meter account (mirrors how patient-service calls billing-service via gRPC to create a billing account).

---

### meter-service

**gRPC Server** — expose a `MeterService` gRPC service (mirrors `BillingService` in reference).

**Proto file** (`src/main/resources/proto/meter_service.proto`):

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "com.eps.meterservice.grpc";
option java_outer_classname = "MeterServiceProto";

service MeterService {
  rpc CreateMeterAccount (CreateMeterAccountRequest) returns (CreateMeterAccountResponse);
}

message CreateMeterAccountRequest {
  string customer_id    = 1;
  string company_id     = 2;
  string meter_type     = 3;
}

message CreateMeterAccountResponse {
  string account_id     = 1;
  string status         = 2;
}
```

**Entities:**

```java
// MeterAccount.java
@Entity
@Table(name = "meter_accounts")
public class MeterAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID customerId;
    private UUID companyId;
    @Enumerated(EnumType.STRING)
    private MeterType meterType;
    private Double ratePerUnit;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;   // ACTIVE, SUSPENDED, CLOSED
    private LocalDate createdAt;
}

// MeterReading.java — biller uploads photo, reading is stored
@Entity
@Table(name = "meter_readings")
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID accountId;
    private Double unitsConsumed;
    private String photoUrl;
    private LocalDateTime uploadedAt;
    private Boolean billGenerated;
}
```

---

### analytics-service

**Kafka Consumer** — consume `CustomerEvent` from `customer-events` topic (mirrors how analytics-service consumes patient events). Log and store analytics data about customer registrations, connection requests by state/district/city.

No REST endpoints, no DB — pure event-driven consumer. Same structure as reference `analytics-service`.

---

### auth-service

Identical in implementation to reference `auth-service`. Adapt only:

- Replace `ADMIN` role seed data with all system roles:
    - `SUPER_ADMIN`, `MANAGEMENT`, `SALES_POC`, `STATE_HEAD`, `DISTRICT_HEAD`, `CITY_HEAD`, `BILLER`, `TECHNICIAN`, `CRM`
    - Client roles: `CLIENT_OPERATIONS`, `CLIENT_BPO_EMPLOYEE`, `CLIENT_BPO_MANAGER_L1`, `CLIENT_BPO_MANAGER_L2`, `CLIENT_SALES_POC`
- Keep the same `User` entity, `JwtUtil`, `UserService`, `AuthController`, `SecurityConfig` pattern
- Same `data.sql` seeding pattern — seed one user per role for testing
- Same H2 in-memory DB for tests, PostgreSQL for runtime

---

### api-gateway

Identical configuration to reference. Add routes for:
- `/api/customers/**` → `customer-service`
- `/api/meters/**` → `meter-service`
- `/api/auth/**` → `auth-service` (no JWT filter on auth routes)

Same JWT validation filter as reference `api-gateway`.

---

## gRPC SETUP

Follow the reference repo's gRPC setup **exactly**:

In `meter-service/pom.xml` and `customer-service/pom.xml`, add the same gRPC dependencies and protobuf Maven plugin as documented in the reference README:

```xml
<!-- Same grpc-netty-shaded 1.69.0, grpc-protobuf 1.69.0, grpc-stub 1.69.0 -->
<!-- Same net.devh:grpc-spring-boot-starter:3.1.0.RELEASE -->
<!-- Same com.google.protobuf:protobuf-java:4.29.1 -->
<!-- Same os-maven-plugin + protobuf-maven-plugin in <build> -->
```

`meter-service` implements `MeterServiceGrpc.MeterServiceImplBase` — annotate with `@GrpcService`.

`customer-service` injects `MeterServiceGrpc.MeterServiceBlockingStub` as a `@Bean` — same pattern as reference patient-service's billing gRPC client.

---

## KAFKA SETUP

Follow the reference repo's Kafka setup exactly.

**`customer-service`** — Producer:
- Proto file: `customer_event.proto` with `CustomerEvent` message (fields: `customerId`, `name`, `email`, `eventType`)
- Add same Kafka + protobuf dependencies as reference
- Publish to topic `customer-events` on successful customer creation

**`analytics-service`** — Consumer:
- Same `spring.kafka.consumer.key-deserializer` and `value-deserializer` as reference
- Deserialize `CustomerEvent` protobuf bytes
- Log event details (same as reference notification-service pattern)

Kafka environment variables (use exact same var names as reference):
```
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

---

## INFRASTRUCTURE

Mirror the reference `infrastructure/` module exactly. Adapt service names:

**Docker Compose services:**
- `customer-service` + `customer-service-db` (PostgreSQL)
- `meter-service` (no DB — gRPC only, stateless for now, or add `meter-service-db`)
- `auth-service` + `auth-service-db`
- `analytics-service`
- `api-gateway`
- `kafka` (Bitnami KRaft, same env vars as reference README)
- `localstack`

Each service container uses the same environment variable format as reference:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://customer-service-db:5432/db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_SQL_INIT_MODE=always
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

Each service has a `Dockerfile` matching the reference pattern (multi-stage Maven build).

**AWS CDK (Java)** — mirror reference `infrastructure/` CDK stack. Create equivalent stacks for ECS services, RDS instances, MSK (Kafka), and a load balancer. Rename stacks from `PatientServiceStack` to `CustomerServiceStack`, etc.

---

## INTEGRATION TESTS

Mirror `integration-tests/` module from reference exactly.

Use **Testcontainers** + **RestAssured** (or the same test libraries as reference).

Write tests for:
1. `CustomerIntegrationTest` — mirrors `PatientIntegrationTest`
    - Create a customer → assert 201, response body fields match
    - Get all customers → assert list size and fields
    - Verify Kafka event published on customer creation
    - Verify gRPC call to meter-service on connection request approval
2. `AuthIntegrationTest` — mirrors reference auth tests
    - Login with seeded user → get JWT
    - Access protected endpoint with JWT → 200
    - Access without JWT → 401

---

## CODING STANDARDS — MUST FOLLOW EXACTLY

These rules ensure the code reads like the reference project:

1. **DTOs separate from entities** — `CustomerRequestDTO`, `CustomerResponseDTO` in `dto/` package. No `@JsonIgnore` on entities directly.

2. **Service layer interface + implementation** — `CustomerService` interface + `CustomerServiceImpl` class. Same as reference.

3. **Global exception handling** — `@RestControllerAdvice` class in `exception/` package. Same exceptions as reference (`ResourceNotFoundException`, `ValidationException`, etc.) renamed to domain context.

4. **Validation** — `@Valid` on controller method parameters, `@NotNull`, `@NotBlank`, `@Email`, `@Size` annotations on DTOs. Same as reference.

5. **Logging** — use `@Slf4j` (Lombok) with `log.info(...)` and `log.error(...)`. Same log message style as reference (`"Creating new customer: {}"`, `"Customer not found: {}"`, etc.).

6. **Lombok** — `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor` on entities and DTOs. Same as reference.

7. **Mapper classes** — manual mappers in a `mapper/` or inline in service, no MapStruct unless reference uses it.

8. **`application.properties`** — no `application.yml`. Use `.properties` files exactly as reference does.

9. **Port assignments:**
    - `auth-service`: 8080
    - `customer-service`: 8081
    - `meter-service`: 8082 (gRPC on 9001, matching reference billing on 9005)
    - `analytics-service`: 8083
    - `api-gateway`: 4004

10. **OpenAPI config** — `@OpenAPIDefinition` and `@SecurityScheme` in a `config/OpenApiConfig.java` per service. Same as reference.

11. **`data.sql`** — seed initial data with `IF NOT EXISTS` guards. Same pattern as reference auth-service `data.sql`.

12. **Test class naming** — `CustomerControllerTest`, `CustomerServiceTest`, `CustomerIntegrationTest`. Match reference test naming convention.

13. **No Lombok on gRPC generated classes** — same caution as reference.

14. **`@Transactional`** on service methods that write. Same usage as reference.

---

## API REQUESTS FILES

Create `api-requests/` folder with `.http` files (IntelliJ HTTP client format, same as reference):

```
api-requests/
├── auth.http
├── customers.http
├── connection-requests.http
└── meter-readings.http
```

Each `.http` file has request examples with `###` separators, variables `@baseUrl`, `@token` — identical format to reference `api-requests/`.

---

## ENVIRONMENT VARIABLES SUMMARY

Match reference repo's variable naming exactly for each service.

**customer-service:**
```
BILLING_SERVICE_ADDRESS=meter-service
BILLING_SERVICE_GRPC_PORT=9001
JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_URL=jdbc:postgresql://customer-service-db:5432/db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
SPRING_SQL_INIT_MODE=always
```

**meter-service:**
```
JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_URL=jdbc:postgresql://meter-service-db:5432/db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

**auth-service:**
```
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_URL=jdbc:postgresql://auth-service-db:5432/db
SPRING_DATASOURCE_USERNAME=admin_user
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_SQL_INIT_MODE=always
```

**analytics-service:**
```
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

---

## IMPLEMENTATION ORDER

Follow this order to build the project (same progression as the reference course):

1. **`customer-service`** — entity, repo, service, controller, DTO, validation, OpenAPI
2. **`meter-service`** — entity, gRPC server setup, proto compilation, gRPC service impl
3. **gRPC wiring** — add gRPC client in `customer-service`, test the call
4. **Kafka** — add producer in `customer-service`, consumer in `analytics-service`
5. **`auth-service`** — copy pattern exactly from reference, adapt roles/seed data
6. **`api-gateway`** — routes + JWT filter
7. **`infrastructure`** — Docker Compose + CDK stacks
8. **`integration-tests`** — Testcontainers-based tests

---

## WHAT NOT TO CHANGE FROM REFERENCE

- pom.xml dependency versions
- gRPC Maven plugin configuration block
- Kafka container environment variable block (copy-paste from reference README)
- Docker multi-stage build pattern in Dockerfiles
- JWT filter implementation in api-gateway
- Spring Security configuration style in auth-service
- Testcontainers setup in integration-tests

The goal is: if someone opens this project side by side with the reference, the code structure, style, and patterns should be immediately familiar — just a different domain.

---

## DELIVERABLE CHECKLIST

- [ ] Root `pom.xml` with all modules declared
- [ ] `customer-service` — full CRUD + Kafka producer + gRPC client
- [ ] `meter-service` — gRPC server + meter reading endpoints
- [ ] `analytics-service` — Kafka consumer
- [ ] `auth-service` — JWT login, role-based users seeded
- [ ] `api-gateway` — routing + JWT validation filter
- [ ] `infrastructure/docker-compose.yml` — all services + dependencies
- [ ] `infrastructure/` CDK stack (Java)
- [ ] `integration-tests/` — at least customer and auth integration tests
- [ ] `api-requests/*.http` — runnable HTTP request examples
- [ ] `grpc-requests/meter-service/` — gRPC test request files
- [ ] All services have Swagger UI enabled at `/swagger-ui.html`
- [ ] README.md mirroring reference structure with env var tables

---

*Reference repository to clone before starting:*
`git clone https://github.com/chrisblakely01/java-spring-microservices`