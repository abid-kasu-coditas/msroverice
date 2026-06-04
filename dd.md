# Revised Implementation Prompt — Electricity Distribution Platform
## Spring Boot Microservices | Multi-Tenant | Flyway | gRPC | Kafka

---

## ANALYSIS OF CURRENT IMPLEMENTATION — WHAT IS WRONG

Before building anything new, understand exactly what the current project got wrong and why. Every fix below is grounded in a specific observed problem.

### Problem 1 — The entire platform layer is absent
The existing code mapped the reference project 1-to-1: `patient-service → customer-service`, `billing-service → meter-service`. This means Aniruddha's company (the platform) has **zero representation** in the codebase. There is no service that manages which electricity providers are onboarded, no service that bills the providers monthly, and no mechanism to suspend a provider when they don't pay. The platform owner is the whole business model — it cannot be missing.

### Problem 2 — No multi-tenancy at all
The current implementation uses `spring.jpa.hibernate.ddl-auto=update` on a single flat schema. Every electricity provider's data — Reliance, Tata Power, MSEB — would live in the same tables with no isolation. This is architecturally wrong. The problem statement explicitly says: *"Every client will have the same database schema, so whenever the new client is on-boarded the application should be able to generate a new database schema."* The current implementation completely ignores this.

### Problem 3 — Flyway is entirely absent
`ddl-auto=update` is used everywhere. This is only acceptable for throw-away prototypes. In production, schema changes done by Hibernate are irreversible and untracked. More critically, Flyway is the **only correct mechanism** to dynamically create a new tenant schema at runtime when a new electricity provider is onboarded. Without Flyway, dynamic tenant provisioning is impossible.

### Problem 4 — JWT carries no tenant context
The `auth-service` JWT payload has a `role` claim but no `tenantId` claim. Because every downstream tenant-aware service needs to know *which schema* to route the request to, the JWT must carry `tenantId`. The `api-gateway` must extract it and forward it as `X-Tenant-ID`. None of this exists in the current implementation.

### Problem 5 — The `meter-service` conflates two distinct domains
In the current code, `meter-service` handles both meter account creation (via gRPC) AND bill generation. These are different bounded contexts. Meter management (types, physical meters, customer connections, photo uploads) belongs in one service. Bill calculation (reading delta × rate) belongs in a separate `billing-service`. The reference project makes this distinction clearly — `billing-service` is its own bounded context that the patient-service calls into.

### Problem 6 — 70% of the domain is missing
The following services required by the business domain do not exist:
- `platform-service` — tenant (electricity provider) registry, platform user management
- `tenant-provisioning-service` — Flyway-based dynamic schema creation per tenant
- `geography-service` — state/district/city/area master data used by all tenant services
- `tenant-user-service` — BPO employees, technicians, billers, CRM per tenant
- `complaint-service` — complaint lifecycle, technician assignment, L1/L2 escalation
- `payment-service` — customer payments, bill status updates
- `platform-billing-service` — monthly SaaS invoicing of electricity providers

### Problem 7 — Entity IDs use UUID instead of BIGSERIAL
All entities use `@GeneratedValue(strategy = GenerationType.UUID)`. The established architecture uses `BIGSERIAL` (`Long`) primary keys. This is significant because: (a) cross-schema references must be raw `Long` fields — UUID is unwieldy for this pattern; (b) PostgreSQL `BIGSERIAL` is faster for indexed joins; (c) the established entity design with `BaseEntity` uses `Long` IDs.

### Problem 8 — AWS CDK / LocalStack is in scope unnecessarily
The current `infrastructure/` module includes a Java CDK stack with LocalStack. For a development-phase project, this adds significant complexity with zero benefit. Docker Compose is sufficient and is what the reference project uses. The CDK layer can be added later when deploying to AWS.

### Problem 9 — `analytics-service` only logs
It just prints Kafka events. It should at minimum persist aggregated data to enable meaningful queries (tenant summary, overdue bills, complaints by area, etc.).

### Problem 10 — No complaint or escalation flow
The BPO→technician→escalation→L1/L2 flow is the core operational workflow of the platform. It is completely absent. A customer raising a complaint, it being assigned to a BPO employee, then to a local technician, and being escalated through manager levels — none of this exists.

---

## WHAT TO KEEP FROM THE CURRENT IMPLEMENTATION

Do **not** throw everything away. The following is correctly done and must be preserved:

- The Maven multi-module monorepo structure with a root `pom.xml`
- The gRPC dependency setup in `meter-service/pom.xml` (grpc-netty-shaded, grpc-stub, grpc-protobuf, grpc-spring-boot-starter, protobuf-maven-plugin) — **keep these exact versions**
- The Protobuf-over-Kafka pattern in `analytics-service` (ByteArray deserializer, `parseFrom()`)
- The `auth-service` `SecurityConfig`, `JwtUtil`, and `AuthController` — just extend the JWT payload
- The `api-gateway` JWT validation filter — just extend it to extract and forward `X-Tenant-ID`
- The `Dockerfile` multi-stage Maven build pattern for each service
- The `api-requests/*.http` file format
- Base package `com.eps` — keep this across all services
- Port assignments: auth=8080, customer=8081, api-gateway=4004

---

## PROJECT OVERVIEW (CORRECTED)

**VidyutConnect** is a two-layer platform:

**Layer 1 — Platform (Aniruddha's company)**: Onboards electricity providers, manages platform staff, bills providers monthly, suspends non-paying providers.

**Layer 2 — Tenant (each electricity provider)**: Each provider gets an isolated PostgreSQL schema. They manage their own staff (BPO, CRM, billers, technicians), their own customers, meters, readings, bills, payments, and complaints.

Both layers share a single PostgreSQL database but use schema isolation. Platform data lives in the `public` schema. Each tenant's data lives in `tenant_<code>` (e.g. `tenant_reliance`).

---

## CORRECTED MONOREPO STRUCTURE

Replace the existing structure with this. Keep the `electricity-distribution-platform/` root folder.

```
electricity-distribution-platform/
├── pom.xml                          ← root POM, all modules declared here
│
├── grpc-proto/                      ← NEW: shared Maven module, all .proto files
│
├── api-gateway/                     ← MODIFY: add X-Tenant-ID injection
├── auth-service/                    ← MODIFY: add tenantId + userType to JWT
│
├── platform-service/                ← NEW: platform user + tenant registry
├── tenant-provisioning-service/     ← NEW: Flyway dynamic schema creation
├── geography-service/               ← NEW: state/district/city/area master data
├── platform-billing-service/        ← NEW: monthly invoicing of tenants
│
├── tenant-user-service/             ← NEW: BPO, CRM, technician, biller mgmt
├── customer-service/                ← MODIFY: fix entities, add tenant routing
├── meter-service/                   ← MODIFY: meter types + physical meters only
├── meter-reading-service/           ← NEW: split from meter-service
├── billing-service/                 ← MODIFY: renamed logic from meter-service
├── payment-service/                 ← NEW
├── complaint-service/               ← NEW
│
├── notification-service/            ← RENAME from analytics-service (analytics logic stays)
├── analytics-service/               ← MODIFY: add persistent aggregation
│
├── infrastructure/
│   ├── docker-compose.yml           ← REPLACE: remove LocalStack/CDK, use plain Docker
│   └── init-db/init.sql
│
├── integration-tests/               ← MODIFY: add tenant provisioning test
└── api-requests/                    ← EXTEND: add new service HTTP files
```

---

## TECHNOLOGY STACK (CORRECTED)

| Component | Library / Version | Notes |
|---|---|---|
| Java | 21 | upgrade from 17 |
| Spring Boot | 3.3.x | keep same |
| Build | Maven multi-module | keep same |
| Database | PostgreSQL 16 | single DB, multi-schema |
| Schema management | Flyway 10.x | replace `ddl-auto=update` everywhere |
| ORM | Spring Data JPA + Hibernate | `ddl-auto=validate` |
| Multi-tenancy | Hibernate `MultiTenantConnectionProvider` | new |
| Messaging | Apache Kafka + Protobuf | keep same versions |
| Sync RPC | gRPC 1.69.0 + Protobuf 4.29.1 | keep same |
| Auth | Spring Security + JJWT 0.12.6 | extend JWT payload |
| Gateway | Spring Cloud Gateway | extend JWT filter |
| Docs | SpringDoc OpenAPI 2.6.0 | keep same |
| Cache | Spring Data Redis | new — for tenant suspension cache |
| Tests | Testcontainers + JUnit 5 | keep same |
| Infra | Docker Compose only | remove LocalStack/CDK |

**Remove**: AWS CDK dependency, LocalStack from docker-compose.

---

## ARCHITECTURAL RULES (ALL MUST BE FOLLOWED)

1. **`ddl-auto=validate` everywhere** — Hibernate must never touch the schema. All DDL is owned exclusively by Flyway.

2. **Schema-per-tenant** — Platform data: `public` schema. Tenant data: `tenant_<tenantCode>` schema (e.g. `tenant_reliance`, `tenant_tatapower`).

3. **Tenant context via header** — Every inbound request to a tenant-aware service carries `X-Tenant-ID` header injected by the API Gateway from the JWT `tenantId` claim. A `TenantContextFilter` stores this in a `ThreadLocal`. JPA routing uses `SchemaBasedMultiTenantConnectionProvider` to set `SET search_path TO tenant_<id>, public` on each connection.

4. **Cross-schema references = raw Long fields** — When a tenant-schema entity references a `public`-schema entity (or a different service's entity), store only the raw `Long` ID. No `@ManyToOne`, no `@JoinColumn`, no JPA FK across schemas. Resolution happens at the service layer.

5. **Immutable entities have no `updatedAt`** — `MeterReading`, `MeterPhoto`, `Payment`, `ComplaintEscalation` never have `updatedAt`. They extend no base class.

6. **Entity primary keys are `Long` / `BIGSERIAL`** — Replace all `UUID` IDs with `Long`. The `@GeneratedValue(strategy = GenerationType.IDENTITY)` maps to PostgreSQL `BIGSERIAL`.

7. **Services communicate via gRPC (sync) or Kafka (async)** — No direct DB sharing, no REST calls between services.

8. **Flyway tenant migrations are classpath resources** — `classpath:db/tenant-migration/V1__*.sql` through `V11__*.sql`. The `tenant-provisioning-service` runs them programmatically per new tenant.

---

## FLYWAY CONFIGURATION PER SERVICE TYPE

### Platform services (e.g. `platform-service`, `auth-service`, `geography-service`)
```yaml
spring:
  flyway:
    enabled: true
    schemas: public
    locations: classpath:db/migration
  jpa:
    hibernate:
      ddl-auto: validate
```

### Tenant-aware services (e.g. `customer-service`, `billing-service`)
Flyway is **disabled** here — tenant schemas are created only by `tenant-provisioning-service`.
```yaml
spring:
  flyway:
    enabled: false
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        multiTenancy: SCHEMA
        multi_tenant_connection_provider: com.eps.common.multitenancy.SchemaBasedMultiTenantConnectionProvider
        tenant_identifier_resolver: com.eps.common.multitenancy.TenantIdentifierResolver
```

### `tenant-provisioning-service` (special)
Flyway runs programmatically, NOT via Spring auto-config. Spring Flyway is disabled. The service manually instantiates `Flyway` with a `DataSource` scoped to each new tenant's schema.

---

## `grpc-proto` MODULE — BUILD THIS FIRST

This is a standalone Maven module. All `.proto` files live here. All other services depend on it.

```
grpc-proto/
├── pom.xml
└── src/main/proto/
    ├── billing.proto               ← MeterReadingRequest, BillResponse
    ├── meter.proto                 ← ConnectionRequest, ConnectionResponse, MeterTypeResponse
    ├── geography.proto             ← AreaRequest/Response, CityRequest/Response
    ├── tenant_user.proto           ← AreaRequest→TechnicianResponse, CityRequest→BPOEmployeeResponse
    └── events/
        ├── tenant_events.proto     ← TenantRegisteredEvent, TenantProvisionedEvent, TenantSuspendedEvent
        ├── billing_events.proto    ← BillGeneratedEvent
        ├── payment_events.proto    ← PaymentReceivedEvent
        └── complaint_events.proto  ← ComplaintRaisedEvent, ComplaintResolvedEvent, ComplaintEscalatedEvent
```

`pom.xml` for `grpc-proto` uses the **exact same** gRPC Maven plugin block as the current `meter-service/pom.xml` — do not change those versions.

All services add:
```xml
<dependency>
    <groupId>com.eps</groupId>
    <artifactId>grpc-proto</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

---

## SHARED MULTI-TENANCY CLASSES

Create a shared library module `common-multitenancy` (or inline in each tenant service):

```java
// TenantContext.java
public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    public static void setCurrentTenant(String tenantId) { CURRENT_TENANT.set(tenantId); }
    public static String getCurrentTenant() { return CURRENT_TENANT.get(); }
    public static void clear() { CURRENT_TENANT.remove(); }
}

// TenantContextFilter.java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String tenantId = ((HttpServletRequest) req).getHeader("X-Tenant-ID");
        if (tenantId == null || tenantId.isBlank()) {
            ((HttpServletResponse) res).sendError(400, "Missing X-Tenant-ID header");
            return;
        }
        TenantContext.setCurrentTenant(tenantId);
        try {
            chain.doFilter(req, res);
        } finally {
            TenantContext.clear();
        }
    }
}

// SchemaBasedMultiTenantConnectionProvider.java
@Component
public class SchemaBasedMultiTenantConnectionProvider
        implements MultiTenantConnectionProvider<String> {
    @Autowired private DataSource dataSource;

    @Override
    public Connection getConnection(String tenantId) throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.createStatement().execute(
            "SET search_path TO tenant_" + tenantId + ", public"
        );
        return conn;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection)
            throws SQLException {
        connection.createStatement().execute("SET search_path TO public");
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() { return true; }
}

// TenantIdentifierResolver.java
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();
        return (tenant != null) ? tenant : "public";
    }
    @Override
    public boolean validateExistingCurrentSessions() { return true; }
}
```

Copy these four classes into **every tenant-aware service**: `tenant-user-service`, `customer-service`, `meter-service`, `meter-reading-service`, `billing-service`, `payment-service`, `complaint-service`.

---

## AUTH SERVICE — FIX THE JWT PAYLOAD

The current JWT payload only has `role`. It must carry `tenantId` and `userType`:

```java
// In JwtUtil.generateToken():
Map<String, Object> claims = new HashMap<>();
claims.put("role", user.getRole());
claims.put("userType", user.getUserType());   // PLATFORM | TENANT | CUSTOMER
claims.put("tenantId", user.getTenantId());   // null for platform users

// In User entity, add:
private String userType;   // PLATFORM | TENANT | CUSTOMER
private String tenantId;   // null for platform users, e.g. "reliance" for tenant users
```

Fix `data.sql` — seed users for all roles:
```sql
-- Platform users (tenantId = null)
INSERT INTO users (id, email, password, role, user_type, tenant_id) VALUES
  (gen_random_uuid(), 'superadmin@eps.com',   '$2b$12$...', 'SUPER_ADMIN',   'PLATFORM', null),
  (gen_random_uuid(), 'management@eps.com',   '$2b$12$...', 'MANAGEMENT',    'PLATFORM', null),
  (gen_random_uuid(), 'salespoc@eps.com',     '$2b$12$...', 'SALES_POC',     'PLATFORM', null),
  (gen_random_uuid(), 'statehead@eps.com',    '$2b$12$...', 'STATE_HEAD',    'PLATFORM', null),
  (gen_random_uuid(), 'districthead@eps.com', '$2b$12$...', 'DISTRICT_HEAD', 'PLATFORM', null),
  (gen_random_uuid(), 'cityhead@eps.com',     '$2b$12$...', 'CITY_HEAD',     'PLATFORM', null);

-- Tenant users (tenantId = 'demo_tenant')
INSERT INTO users (id, email, password, role, user_type, tenant_id) VALUES
  (gen_random_uuid(), 'operations@reliance.com',  '$2b$12$...', 'OPERATIONS',       'TENANT', 'reliance'),
  (gen_random_uuid(), 'bpo@reliance.com',         '$2b$12$...', 'BPO_EMPLOYEE',     'TENANT', 'reliance'),
  (gen_random_uuid(), 'mgrl1@reliance.com',       '$2b$12$...', 'BPO_MANAGER_L1',   'TENANT', 'reliance'),
  (gen_random_uuid(), 'mgrl2@reliance.com',       '$2b$12$...', 'BPO_MANAGER_L2',   'TENANT', 'reliance'),
  (gen_random_uuid(), 'biller@reliance.com',      '$2b$12$...', 'BILLER',           'TENANT', 'reliance'),
  (gen_random_uuid(), 'technician@reliance.com',  '$2b$12$...', 'TECHNICIAN',       'TENANT', 'reliance'),
  (gen_random_uuid(), 'crm@reliance.com',         '$2b$12$...', 'CRM',              'TENANT', 'reliance');

-- Customer users
INSERT INTO users (id, email, password, role, user_type, tenant_id) VALUES
  (gen_random_uuid(), 'customer@example.com', '$2b$12$...', 'CUSTOMER', 'CUSTOMER', 'reliance');
```

---

## API GATEWAY — FIX THE JWT FILTER

The existing JWT filter validates the token. Extend it to also extract and forward `tenantId`:

```java
// In the existing JwtAuthFilter (keep all existing logic, just add these lines):
String tenantId = jwtUtil.extractTenantId(token);   // new method in JwtUtil
if (tenantId != null && !tenantId.isBlank()) {
    exchange.getRequest().mutate()
        .header("X-Tenant-ID", tenantId)
        .build();
}

// Add route to api-gateway application.yml for all new services:
spring:
  cloud:
    gateway:
      routes:
        - id: platform-service
          uri: http://platform-service:8085
          predicates:
            - Path=/api/platform/**
        - id: tenant-provisioning-service
          uri: http://tenant-provisioning-service:8086
          predicates:
            - Path=/api/provisioning/**
        - id: geography-service
          uri: http://geography-service:8087
          predicates:
            - Path=/api/geography/**
        - id: tenant-user-service
          uri: http://tenant-user-service:8088
          predicates:
            - Path=/api/users/**
        - id: complaint-service
          uri: http://complaint-service:8092
          predicates:
            - Path=/api/complaints/**
        - id: payment-service
          uri: http://payment-service:8093
          predicates:
            - Path=/api/payments/**
```

Add Redis-based tenant suspension check in the filter:
```java
// Before routing any request to tenant services:
String tenantStatus = redisTemplate.opsForValue().get("tenant:status:" + tenantId);
if ("SUSPENDED".equals(tenantStatus)) {
    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
    return exchange.getResponse().setComplete();
}
```

---

## NEW SERVICE: `platform-service`

**Port**: 8085 | **DB**: `public` schema | **No multi-tenancy filter needed** (platform-only)

**Flyway migration** `V1__platform_core.sql`:
```sql
CREATE TABLE platform_users (
    id              BIGSERIAL PRIMARY KEY,
    auth_user_id    BIGINT NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    role            VARCHAR(50)  NOT NULL,
    phone           VARCHAR(20),
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE tenants (
    id                  BIGSERIAL PRIMARY KEY,
    tenant_code         VARCHAR(100) UNIQUE NOT NULL,
    company_name        VARCHAR(255) NOT NULL,
    gstin               VARCHAR(20),
    contact_email       VARCHAR(255),
    contact_phone       VARCHAR(20),
    onboarded_by_poc_id BIGINT,
    subscription_status VARCHAR(20) DEFAULT 'PROVISIONING',
    schema_name         VARCHAR(110) GENERATED ALWAYS AS ('tenant_' || tenant_code) STORED,
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW()
);

CREATE TABLE poc_tenant_assignments (
    id          BIGSERIAL PRIMARY KEY,
    poc_id      BIGINT NOT NULL,
    tenant_id   BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT NOW(),
    UNIQUE (poc_id, tenant_id)
);
```

**Kafka producer** — on `POST /api/platform/tenants`, after saving, publish:
```java
// Publish to topic: tenant-registered
TenantRegisteredEvent event = TenantRegisteredEvent.newBuilder()
    .setTenantId(tenant.getId())
    .setTenantCode(tenant.getTenantCode())
    .setCompanyName(tenant.getCompanyName())
    .build();
kafkaTemplate.send("tenant-registered", event.toByteArray());
```

**REST Endpoints**:
```
POST   /api/platform/users                    (SUPER_ADMIN, MANAGEMENT)
GET    /api/platform/users
POST   /api/platform/tenants                  (SALES_POC)
GET    /api/platform/tenants
GET    /api/platform/tenants/{id}
POST   /api/platform/tenants/{id}/suspend     (SUPER_ADMIN, MANAGEMENT)
POST   /api/platform/tenants/{id}/reinstate
POST   /api/platform/poc-assignments
```

---

## NEW SERVICE: `tenant-provisioning-service`

**Port**: 8086 | **No REST endpoints** | Pure Kafka consumer → Flyway runner

**Kafka consumer** — consumes `tenant-registered` topic:
```java
@KafkaListener(topics = "tenant-registered", groupId = "provisioning-group")
public void onTenantRegistered(byte[] data) throws Exception {
    TenantRegisteredEvent event = TenantRegisteredEvent.parseFrom(data);
    provisioningService.provisionTenant(event.getTenantCode());
}
```

**Core provisioning logic** (this is the most important class in the entire project):
```java
@Service
@Slf4j
public class TenantSchemaProvisioningService {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public void provisionTenant(String tenantCode) {
        String schemaName = "tenant_" + tenantCode;
        log.info("Provisioning tenant schema: {}", schemaName);

        try {
            // Step 1: Create the schema
            try (Connection conn = dataSource.getConnection()) {
                conn.createStatement()
                    .execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
                log.info("Schema created: {}", schemaName);
            }

            // Step 2: Build a schema-scoped DataSource
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setConnectionInitSql("SET search_path TO " + schemaName + ", public");
            config.setMaximumPoolSize(2);
            config.setPoolName("flyway-" + tenantCode);

            try (HikariDataSource tenantDs = new HikariDataSource(config)) {
                // Step 3: Run all tenant migrations
                Flyway flyway = Flyway.configure()
                    .dataSource(tenantDs)
                    .schemas(schemaName)
                    .locations("classpath:db/tenant-migration")
                    .table("flyway_schema_history")
                    .baselineOnMigrate(false)
                    .load();
                flyway.migrate();
                log.info("Flyway migrations complete for schema: {}", schemaName);
            }

            // Step 4: Publish provisioned event
            TenantProvisionedEvent provisioned = TenantProvisionedEvent.newBuilder()
                .setTenantCode(tenantCode)
                .setSchemaName(schemaName)
                .setStatus("ACTIVE")
                .build();
            kafkaTemplate.send("tenant-provisioned", provisioned.toByteArray());

        } catch (Exception e) {
            log.error("Failed to provision tenant {}: {}", tenantCode, e.getMessage(), e);
            // Publish failure event so platform-service can update tenant status
            TenantProvisionedEvent failed = TenantProvisionedEvent.newBuilder()
                .setTenantCode(tenantCode)
                .setStatus("FAILED")
                .setErrorMessage(e.getMessage())
                .build();
            kafkaTemplate.send("tenant-provisioned", failed.toByteArray());
        }
    }
}
```

**Tenant migration scripts** in `src/main/resources/db/tenant-migration/`:

`V1__tenant_users.sql`:
```sql
CREATE TABLE tenant_users (
    id           BIGSERIAL PRIMARY KEY,
    auth_user_id BIGINT NOT NULL,
    full_name    VARCHAR(255) NOT NULL,
    role         VARCHAR(50)  NOT NULL,
    phone        VARCHAR(20),
    district_id  BIGINT,
    city_id      BIGINT,
    area_id      BIGINT,
    is_active    BOOLEAN DEFAULT TRUE,
    created_at   TIMESTAMP DEFAULT NOW(),
    updated_at   TIMESTAMP DEFAULT NOW()
);
```

`V2__customers.sql`:
```sql
CREATE TABLE customers (
    id                 BIGSERIAL PRIMARY KEY,
    global_customer_id BIGINT NOT NULL,
    account_number     VARCHAR(50) UNIQUE NOT NULL,
    tariff_type        VARCHAR(50),
    billing_address    TEXT,
    city_id            BIGINT NOT NULL,
    area_id            BIGINT NOT NULL,
    crm_id             BIGINT,
    is_active          BOOLEAN DEFAULT TRUE,
    registered_at      TIMESTAMP DEFAULT NOW(),
    updated_at         TIMESTAMP DEFAULT NOW()
);
```

`V3__meter_types_and_meters.sql`:
```sql
CREATE TABLE meter_types (
    id                         BIGSERIAL PRIMARY KEY,
    name                       VARCHAR(100) NOT NULL,
    rate_per_unit              NUMERIC(8,4) NOT NULL,
    billing_cycle_days         INT DEFAULT 30,
    photo_upload_interval_days INT DEFAULT 30,
    is_active                  BOOLEAN DEFAULT TRUE,
    created_at                 TIMESTAMP DEFAULT NOW(),
    updated_at                 TIMESTAMP DEFAULT NOW()
);

CREATE TABLE meters (
    id            BIGSERIAL PRIMARY KEY,
    meter_serial  VARCHAR(100) UNIQUE NOT NULL,
    meter_type_id BIGINT NOT NULL REFERENCES meter_types(id),
    installed_at  DATE,
    is_active     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT NOW(),
    updated_at    TIMESTAMP DEFAULT NOW()
);

CREATE TABLE customer_connections (
    id              BIGSERIAL PRIMARY KEY,
    customer_id     BIGINT NOT NULL,
    meter_id        BIGINT NOT NULL REFERENCES meters(id),
    connected_at    TIMESTAMP DEFAULT NOW(),
    disconnected_at TIMESTAMP,
    is_active       BOOLEAN DEFAULT TRUE
);

CREATE TABLE meter_reading_schedules (
    id            BIGSERIAL PRIMARY KEY,
    meter_id      BIGINT NOT NULL UNIQUE REFERENCES meters(id),
    next_due_date DATE NOT NULL,
    last_read_at  TIMESTAMP
);
```

`V4__meter_readings.sql`:
```sql
CREATE TABLE meter_readings (
    id               BIGSERIAL PRIMARY KEY,
    connection_id    BIGINT NOT NULL,
    meter_id         BIGINT NOT NULL,
    biller_id        BIGINT NOT NULL,
    reading_value    NUMERIC(10,2) NOT NULL,
    photo_url        VARCHAR(500) NOT NULL,
    read_at          TIMESTAMP DEFAULT NOW(),
    billing_triggered BOOLEAN DEFAULT FALSE
    -- no updated_at: immutable
);
```

`V5__billing.sql`:
```sql
CREATE TABLE bills (
    id             BIGSERIAL PRIMARY KEY,
    connection_id  BIGINT NOT NULL,
    customer_id    BIGINT NOT NULL,
    reading_id     BIGINT NOT NULL,
    units_consumed NUMERIC(10,2) NOT NULL,
    rate_per_unit  NUMERIC(8,4) NOT NULL,
    amount_due     NUMERIC(12,2) NOT NULL,
    total_amount   NUMERIC(12,2) NOT NULL,
    billing_month  DATE NOT NULL,
    due_date       DATE NOT NULL,
    status         VARCHAR(20) DEFAULT 'UNPAID',
    created_at     TIMESTAMP DEFAULT NOW(),
    updated_at     TIMESTAMP DEFAULT NOW()
);
```

`V6__payments.sql`:
```sql
CREATE TABLE payments (
    id              BIGSERIAL PRIMARY KEY,
    bill_id         BIGINT NOT NULL,
    customer_id     BIGINT NOT NULL,
    amount_paid     NUMERIC(12,2) NOT NULL,
    payment_method  VARCHAR(50),
    transaction_ref VARCHAR(200),
    paid_at         TIMESTAMP DEFAULT NOW()
    -- no updated_at: immutable
);
```

`V7__complaints.sql`:
```sql
CREATE TABLE complaints (
    id              BIGSERIAL PRIMARY KEY,
    customer_id     BIGINT NOT NULL,
    connection_id   BIGINT NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    status          VARCHAR(30) DEFAULT 'OPEN',
    technician_id   BIGINT,
    bpo_employee_id BIGINT,
    city_id         BIGINT NOT NULL,
    area_id         BIGINT NOT NULL,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE complaint_escalations (
    id           BIGSERIAL PRIMARY KEY,
    complaint_id BIGINT NOT NULL REFERENCES complaints(id),
    escalated_by BIGINT NOT NULL,
    escalated_to VARCHAR(30) NOT NULL,
    reason       TEXT,
    escalated_at TIMESTAMP DEFAULT NOW()
    -- no updated_at: immutable
);
```

---

## NEW SERVICE: `geography-service`

**Port**: 8087 | **DB**: `public` schema | **Also exposes gRPC**

**Flyway** `V1__geography.sql`:
```sql
CREATE TABLE states    (id BIGSERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, code VARCHAR(10) UNIQUE NOT NULL, is_active BOOLEAN DEFAULT TRUE);
CREATE TABLE districts (id BIGSERIAL PRIMARY KEY, state_id BIGINT NOT NULL REFERENCES states(id), name VARCHAR(100) NOT NULL, is_active BOOLEAN DEFAULT TRUE);
CREATE TABLE cities    (id BIGSERIAL PRIMARY KEY, district_id BIGINT NOT NULL REFERENCES districts(id), name VARCHAR(100) NOT NULL, is_active BOOLEAN DEFAULT TRUE);
CREATE TABLE areas     (id BIGSERIAL PRIMARY KEY, city_id BIGINT NOT NULL REFERENCES cities(id), name VARCHAR(100) NOT NULL, pincode VARCHAR(10));
```

**gRPC server** (`geography.proto` from `grpc-proto`):
```protobuf
service GeographyService {
  rpc GetAreaById    (IdRequest)      returns (AreaResponse);
  rpc GetCityById    (IdRequest)      returns (CityResponse);
  rpc ValidateArea   (IdRequest)      returns (ValidateResponse);
}
```

---

## EXISTING SERVICE: `customer-service` — FIX THESE THINGS

1. Replace all `UUID` IDs with `Long` (`@GeneratedValue(strategy = GenerationType.IDENTITY)`)
2. Add `TenantContextFilter`, `SchemaBasedMultiTenantConnectionProvider`, `TenantIdentifierResolver`
3. Disable Flyway (`spring.flyway.enabled=false`) — schema is created by `tenant-provisioning-service`
4. Change `ddl-auto` from `update` to `validate`
5. Remove `customer_service-db` service from docker-compose — it uses the shared `postgres` container
6. Fix entity to match `V2__customers.sql`:
   - `id: Long` (not UUID)
   - `globalCustomerId: Long` (raw cross-schema ref to `public.customer_identities`)
   - `accountNumber: String`
   - `tariffType: String`
   - `cityId: Long`, `areaId: Long` (raw geography refs)
   - `crmId: Long`
7. Kafka producer — publish `CustomerOnboardedEvent` on new customer creation

---

## EXISTING SERVICE: `meter-service` — FIX AND SPLIT

**Split the current `meter-service` into two services:**

`meter-service` (keeps the gRPC server) — handles **only** meter types, physical meters, connections, and schedules.

`meter-reading-service` (new) — handles biller photo uploads, triggers billing via gRPC.

**Fix in `meter-service`**:
1. Replace UUID IDs with Long
2. Add tenant context classes
3. Entities now match `V3__meter_types_and_meters.sql`
4. gRPC server exposes `GetConnectionDetails` and `GetMeterTypeRate` (used by billing-service)
5. `MeterAccount` entity → replace with `MeterType` + `Meter` + `CustomerConnection`

**New `meter-reading-service`**:
```java
// On POST /api/readings, after saving MeterReading:
// 1. Update meter_reading_schedules.next_due_date
// 2. Call billing-service via gRPC:
MeterReadingRequest grpcReq = MeterReadingRequest.newBuilder()
    .setConnectionId(reading.getConnectionId())
    .setReadingValue(reading.getReadingValue().doubleValue())
    .setReadAt(reading.getReadAt().toString())
    .setTenantId(TenantContext.getCurrentTenant())
    .build();
BillResponse grpcResp = billingServiceStub.generateBill(grpcReq);
```

---

## EXISTING SERVICE: `billing-service` — RENAME AND FIX

This is the **gRPC server** called by `meter-reading-service`. In the current project, the gRPC server is in `meter-service`. Move it:

- `meter-service` = meter management (types, physical meters, connections) → gRPC server for `GetMeterTypeRate`
- `billing-service` = bill calculation + gRPC server for `GenerateBill`

```java
@GrpcService
public class BillingGrpcServiceImpl extends BillingGrpcServiceGrpc.BillingGrpcServiceImplBase {
    @Override
    public void generateBill(MeterReadingRequest request,
                             StreamObserver<BillResponse> observer) {
        TenantContext.setCurrentTenant(request.getTenantId());
        // 1. Find previous reading for this connection
        // 2. Calculate units = currentReading - previousReading
        // 3. Call meter-service gRPC to get rate_per_unit
        // 4. Save Bill to bills table
        // 5. Publish BillGeneratedEvent to Kafka
        // 6. Return BillResponse
    }
}
```

---

## NEW SERVICE: `tenant-user-service`

**Port**: 8088 | **DB**: tenant schema | **gRPC server**

Manages BPO employees, technicians, billers, CRM, operations — all within a tenant schema.

**gRPC server** (`tenant_user.proto`):
```protobuf
service TenantUserService {
  rpc GetTechnicianByArea   (IdRequest)      returns (TechnicianResponse);
  rpc GetBPOEmployeeByCity  (IdRequest)      returns (BPOEmployeeResponse);
  rpc GetManagerByLevel     (ManagerRequest) returns (ManagerResponse);
}
```

This is used by `complaint-service` to:
1. Auto-assign a BPO employee when a complaint is raised
2. Auto-assign a technician when a BPO employee accepts the complaint

---

## NEW SERVICE: `complaint-service`

**Port**: 8092 | **DB**: tenant schema | **gRPC client** (calls `tenant-user-service`)

```java
// On POST /api/complaints:
// 1. Save complaint (status=OPEN)
// 2. Call tenant-user-service gRPC: GetBPOEmployeeByCity(complaint.getCityId())
// 3. Update complaint.bpoEmployeeId, status=ASSIGNED
// 4. Publish ComplaintRaisedEvent

// On PUT /api/complaints/{id}/assign-technician (BPO_EMPLOYEE):
// 1. Call tenant-user-service gRPC: GetTechnicianByArea(complaint.getAreaId())
// 2. Update complaint.technicianId, status=IN_PROGRESS

// On PUT /api/complaints/{id}/resolve (TECHNICIAN):
// 1. Update status=RESOLVED
// 2. Publish ComplaintResolvedEvent

// On POST /api/complaints/{id}/escalate (CUSTOMER):
// 1. Create ComplaintEscalation record (immutable)
// 2. Update complaint status = ESCALATED_L1 or ESCALATED_L2
// 3. Publish ComplaintEscalatedEvent
```

---

## NEW SERVICE: `payment-service`

**Port**: 8093 | **DB**: tenant schema

```java
// On POST /api/payments (CUSTOMER):
// 1. Validate bill exists and is UNPAID (call billing-service via Kafka or gRPC)
// 2. Save Payment (immutable — no updatedAt)
// 3. Publish PaymentReceivedEvent
// billing-service consumes PaymentReceivedEvent and updates bill status = PAID
```

---

## EXISTING SERVICE: `analytics-service` — FIX

Currently just logs. Add a persistent store:

**Flyway** `V1__analytics.sql` (in `public` schema):
```sql
CREATE TABLE tenant_stats (
    id          BIGSERIAL PRIMARY KEY,
    tenant_code VARCHAR(100) NOT NULL,
    event_type  VARCHAR(100) NOT NULL,
    event_count INT DEFAULT 1,
    recorded_at TIMESTAMP DEFAULT NOW()
);
```

Consume all Kafka events and `INSERT INTO tenant_stats` for each. Add REST endpoints:
```
GET /api/analytics/platform/summary
GET /api/analytics/tenant/{tenantCode}/summary
```

---

## NOTIFICATION SERVICE (RENAME FROM analytics-service PATTERN)

Create `notification-service` consuming these topics and sending stub notifications (log for now, real email/SMS later):

| Topic                 | Action |
|-----------------------|--------|
| `tenant-provisioned`  | Log: welcome email sent to tenant operations |
| `customer-onboarded`  | Log: welcome SMS to customer |
| `bill-generated`      | Log: bill notification to customer |
| `payment-received`    | Log: payment confirmation |
| `complaint-raised`    | Log: BPO employee notified |
| `complaint-resolved`  | Log: customer notified |
| `complaint-escalated` | Log: manager notified |
| `tenant-suspended`    | Log: tenant ops + platform POC notified |

---

## DOCKER COMPOSE — REPLACE ENTIRELY

Remove LocalStack and CDK. Single shared PostgreSQL for all services:

```yaml
version: "3.9"

services:
  postgres:
    image: postgres:16
    container_name: eps-postgres
    environment:
      POSTGRES_DB: epsdb
      POSTGRES_USER: admin_user
      POSTGRES_PASSWORD: password
    ports: ["5432:5432"]
    volumes:
      - pg_data:/var/lib/postgresql/data
      - ./init-db/init.sql:/docker-entrypoint-initdb.d/init.sql

  kafka:
    image: bitnami/kafka:3.7
    container_name: eps-kafka
    environment:
      KAFKA_CFG_NODE_ID: 0
      KAFKA_CFG_PROCESS_ROLES: controller,broker
      KAFKA_CFG_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://:9094
      KAFKA_CFG_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,EXTERNAL://localhost:9094
      KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_CFG_CONTROLLER_QUORUM_VOTERS: 0@kafka:9093
      KAFKA_CFG_CONTROLLER_LISTENER_NAMES: CONTROLLER
    ports: ["9094:9094"]

  redis:
    image: redis:7
    container_name: eps-redis
    ports: ["6379:6379"]

  api-gateway:
    build: ./api-gateway
    ports: ["4004:4004"]
    environment:
      SPRING_REDIS_HOST: redis
      AUTH_SERVICE_URL: http://auth-service:8080
    depends_on: [auth-service, redis]

  auth-service:
    build: ./auth-service
    ports: ["8080:8080"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_SCHEMAS: public
      SPRING_SQL_INIT_MODE: always

  platform-service:
    build: ./platform-service
    ports: ["8085:8085"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_SCHEMAS: public
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    depends_on: [postgres, kafka]

  tenant-provisioning-service:
    build: ./tenant-provisioning-service
    ports: ["8086:8086"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_FLYWAY_ENABLED: false
      SPRING_JPA_HIBERNATE_DDL_AUTO: none
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    depends_on: [postgres, kafka]

  geography-service:
    build: ./geography-service
    ports: ["8087:8087"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_SCHEMAS: public
    depends_on: [postgres]

  customer-service:
    build: ./customer-service
    ports: ["8081:8081"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "false"
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    depends_on: [postgres, kafka]

  meter-service:
    build: ./meter-service
    ports: ["8082:8082"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "false"
    depends_on: [postgres]

  meter-reading-service:
    build: ./meter-reading-service
    ports: ["8089:8089"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "false"
      BILLING_SERVICE_ADDRESS: billing-service
      BILLING_SERVICE_GRPC_PORT: 9090
    depends_on: [postgres, billing-service]

  billing-service:
    build: ./billing-service
    ports: ["8090:8090"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "false"
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      METER_SERVICE_ADDRESS: meter-service
      METER_SERVICE_GRPC_PORT: 9088
    depends_on: [postgres, kafka, meter-service]

  tenant-user-service:
    build: ./tenant-user-service
    ports: ["8088:8088"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "false"
    depends_on: [postgres]

  complaint-service:
    build: ./complaint-service
    ports: ["8092:8092"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "false"
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      TENANT_USER_SERVICE_ADDRESS: tenant-user-service
      TENANT_USER_SERVICE_GRPC_PORT: 9088
    depends_on: [postgres, kafka, tenant-user-service]

  payment-service:
    build: ./payment-service
    ports: ["8093:8093"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: "false"
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    depends_on: [postgres, kafka]

  notification-service:
    build: ./notification-service
    ports: ["8094:8094"]
    environment:
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    depends_on: [kafka]

  analytics-service:
    build: ./analytics-service
    ports: ["8083:8083"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_FLYWAY_SCHEMAS: public
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    depends_on: [postgres, kafka]

  platform-billing-service:
    build: ./platform-billing-service
    ports: ["8091:8091"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/epsdb
      SPRING_DATASOURCE_USERNAME: admin_user
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_FLYWAY_SCHEMAS: public
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      SPRING_REDIS_HOST: redis
    depends_on: [postgres, kafka, redis]

volumes:
  pg_data:
```

`init-db/init.sql` — runs once at DB creation:
```sql
-- Enable uuid-ossp in case any module still uses UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- The public schema already exists in PostgreSQL; just set default search path
ALTER DATABASE epsdb SET search_path TO public;
```

---

## IMPLEMENTATION ORDER

Follow this sequence strictly. Each step depends on the previous:

**Phase 1 — Foundation**
1. `grpc-proto` — compile all `.proto` files, verify stubs generate
2. `auth-service` — fix JWT to carry `tenantId` and `userType`, fix `data.sql`
3. `api-gateway` — add `X-Tenant-ID` injection, Redis suspension check, new routes

**Phase 2 — Platform layer**
4. `platform-service` — tenant registry, platform users, Kafka producer for `TenantRegisteredEvent`
5. `tenant-provisioning-service` — `TenantSchemaProvisioningService` + all 7 Flyway migration SQLs
6. `geography-service` — geo master data + gRPC server

**Phase 3 — Tenant layer (fix existing services)**
7. `tenant-user-service` — BPO/CRM/technician/biller management + gRPC server
8. `customer-service` — fix IDs (UUID→Long), add tenant routing, fix entity
9. `meter-service` — fix entities, expose `GetMeterTypeRate` gRPC server
10. `meter-reading-service` — split from meter-service, gRPC client to billing-service
11. `billing-service` — gRPC server (`GenerateBill`), Kafka producer (`BillGeneratedEvent`)
12. `payment-service` — payment recording, `PaymentReceivedEvent`
13. `complaint-service` — full lifecycle, gRPC client to tenant-user-service

**Phase 4 — Cross-cutting**
14. `notification-service` — all Kafka consumers
15. `analytics-service` — fix to persist aggregated data
16. `platform-billing-service` — monthly invoicing scheduler

**Phase 5 — Infrastructure and tests**
17. `docker-compose.yml` — replace with the corrected file above
18. `integration-tests` — add tenant provisioning test, fix existing tests

---

## INTEGRATION TESTS — FIX AND EXTEND

Keep the existing `CustomerIntegrationTest` and `AuthIntegrationTest` patterns. Add:

```java
// TenantProvisioningIntegrationTest.java
@Testcontainers
class TenantProvisioningIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @Test
    void whenTenantRegisteredEventPublished_thenSchemaCreatedWithAllTables() throws Exception {
        // 1. Publish TenantRegisteredEvent to Kafka
        // 2. Wait for tenant-provisioning-service to consume and provision
        // 3. Assert schema tenant_test exists in PostgreSQL
        // 4. Assert all 7 tables (tenant_users, customers, meters...) exist in the schema
    }

    @Test
    void whenProvisioningComplete_thenCustomerServiceCanRouteToTenantSchema() {
        // 1. Provision tenant "test"
        // 2. POST /api/customers with X-Tenant-ID: test
        // 3. Assert customer saved in tenant_test.customers, not public.customers
    }
}
```

---

## DELIVERABLE CHECKLIST (UPDATED)

- [ ] `grpc-proto` module with all `.proto` files compiling
- [ ] `auth-service` — JWT with `tenantId` + `userType` claims, all roles seeded
- [ ] `api-gateway` — `X-Tenant-ID` injection + Redis suspension check
- [ ] `platform-service` — tenant registry + `TenantRegisteredEvent` Kafka producer
- [ ] `tenant-provisioning-service` — `TenantSchemaProvisioningService` with all 7 migration SQLs
- [ ] `geography-service` — geo master data + gRPC server
- [ ] `tenant-user-service` — full CRUD + gRPC server
- [ ] `customer-service` — fixed entities (Long IDs) + tenant routing working
- [ ] `meter-service` — fixed entities + `GetMeterTypeRate` gRPC server
- [ ] `meter-reading-service` — reading submission + gRPC call to billing-service
- [ ] `billing-service` — `GenerateBill` gRPC server + `BillGeneratedEvent` Kafka
- [ ] `payment-service` — immutable payments + `PaymentReceivedEvent` Kafka
- [ ] `complaint-service` — full lifecycle + escalation + gRPC client
- [ ] `notification-service` — consumes all 8 Kafka topics
- [ ] `analytics-service` — persists aggregated events + REST summary endpoints
- [ ] `platform-billing-service` — monthly invoice scheduler + suspension/reinstate events
- [ ] `docker-compose.yml` — single PostgreSQL + Kafka + Redis, no LocalStack
- [ ] `integration-tests` — tenant provisioning test + existing tests updated
- [ ] All services have Swagger at `/swagger-ui.html`
- [ ] All services use `ddl-auto=validate`, never `update`
- [ ] All tenant-aware services have `TenantContextFilter` registered

---

*Current project is at: `electricity-distribution-platform/` in the repository. Begin by fixing `grpc-proto`, then `auth-service`, then proceed in the order above.*