# Schema-Based Multi-Tenancy Implementation

This document details how the electricity distribution platform implements **schema-based multi-tenancy**, where each tenant has its own isolated PostgreSQL schema with automatic schema creation and per-request schema switching.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client Request                           │
│                    (JWT with tenantId claim)                     │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      API Gateway (4004)                          │
│  • Extracts tenantId from JWT token                             │
│  • Adds X-Tenant-ID header to downstream request                │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                   Microservice (8080-8094)                       │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  TenantAwareFilter (Servlet Filter)                     │   │
│  │  • Extracts X-Tenant-ID from HTTP header               │   │
│  │  • Sets TenantContext.setCurrentTenant(tenantId)        │   │
│  │  • Clears context after request                         │   │
│  └──────────────────────────┬───────────────────────────────┘   │
│                             │                                    │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Business Logic & JPA Queries                           │   │
│  └──────────────────────────┬───────────────────────────────┘   │
│                             │                                    │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Hibernate + TenantIdentifierResolver                   │   │
│  │  • Reads TenantContext.getCurrentTenant()               │   │
│  │  • Resolves to schema name (tenant_xxx)                 │   │
│  └──────────────────────────┬───────────────────────────────┘   │
│                             │                                    │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  SchemaBasedMultiTenantConnectionProvider               │   │
│  │  • Executes: SET search_path TO tenant_xxx, public     │   │
│  │  • Routes connection to appropriate schema               │   │
│  └──────────────────────────┬───────────────────────────────┘   │
│                             │                                    │
│                             ▼                                    │
│                      PostgreSQL Connection                      │
└─────────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                   PostgreSQL Database (5432)                     │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  public schema        ┌──────────────────────────────┐  │   │
│  │  • Platform tables    │ tenant_acme schema           │  │   │
│  │  • User table         │ • customers table            │  │   │
│  │  • Tenant registry    │ • meters table               │  │   │
│  │  • Auth info          │ • bills table                │  │   │
│  │                       │ • payments table             │  │   │
│  │                       │ • meter_readings table       │  │   │
│  │                       │ • complaints table           │  │   │
│  │                       └──────────────────────────────┘  │   │
│  │                       ┌──────────────────────────────┐  │   │
│  │                       │ tenant_globex schema         │  │   │
│  │                       │ • customers table            │  │   │
│  │                       │ • meters table               │  │   │
│  │                       │ ... (separate data set)      │  │   │
│  │                       └──────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Key Components

### 1. **TenantContext** (Thread-Local Storage)
**File:** `shared/src/main/java/com/eps/shared/tenant/TenantContext.java`

Manages the current tenant identifier in thread-local storage:
```java
public static void setCurrentTenant(String tenantId)
public static String getCurrentTenant()
public static void clear()
public static boolean isSet()
```

**Purpose:** Stores the tenant ID for the current request so it's available throughout the request processing lifecycle.

---

### 2. **TenantAwareFilter** (Servlet Filter)
**File:** `shared/src/main/java/com/eps/shared/tenant/TenantAwareFilter.java`

HTTP filter that extracts the tenant ID from incoming requests:
```
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantAwareFilter extends OncePerRequestFilter
```

**Workflow:**
1. Extracts `X-Tenant-ID` header from HTTP request
2. Validates header is present (returns 400 if missing)
3. Sets tenant in TenantContext
4. Processes the request
5. Clears context in finally block

**Applied to:** All microservices except `auth-service`, `geography-service`, and `notification-service` (which don't need multi-tenancy)

---

### 3. **TenantIdentifierResolver** (Hibernate SPI)
**File:** `shared/src/main/java/com/eps/shared/tenant/TenantIdentifierResolver.java`

Implements Hibernate's `CurrentTenantIdentifierResolver` interface:
```java
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {
    
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();
        return tenant == null || tenant.isBlank() ? "public" : tenant;
    }
}
```

**Purpose:** When Hibernate needs to perform a database operation, it calls this resolver to determine which tenant's data to access.

**Default:** Falls back to "public" schema if no tenant is set (for platform-level operations).

---

### 4. **SchemaBasedMultiTenantConnectionProvider** (Hibernate SPI)
**File:** `shared/src/main/java/com/eps/shared/tenant/SchemaBasedMultiTenantConnectionProvider.java`

Implements Hibernate's `MultiTenantConnectionProvider` interface:
```java
@Component
public class SchemaBasedMultiTenantConnectionProvider 
    implements MultiTenantConnectionProvider<String>
```

**Key Methods:**

| Method | Purpose |
|--------|---------|
| `getConnection(String tenantIdentifier)` | Gets connection and executes `SET search_path TO tenant_xxx, public` |
| `setSearchPath(Connection, String)` | Routes PostgreSQL connection to correct schema |
| `setPublicSearchPath(Connection)` | Resets to public schema before releasing connection |
| `normalizeTenantCode(String)` | Validates tenant code format (alphanumeric + underscore) |

**Schema Switching:**
```sql
-- For tenant "acme":
SET search_path TO tenant_acme, public;

-- For platform operations:
SET search_path TO public;
```

---

### 5. **JWT Token with TenantId** (Authentication)
**File:** `auth-service/src/main/java/com/eps/authservice/util/JwtUtil.java`

JWT tokens include the tenant ID as a claim:
```java
claims.put("tenantId", user.getTenantId());
```

**Flow:**
1. User authenticates via auth-service
2. JWT token includes `tenantId` claim
3. API Gateway extracts this claim
4. Adds `X-Tenant-ID` header to downstream requests

---

### 6. **API Gateway Routing**
**File:** `api-gateway/src/main/java/com/eps/apigateway/ApiGatewayApplication.java`

```java
@Override
public GatewayFilter apply(Config config) {
    // Extract tenantId from JWT
    String tenantId = claims.get("tenantId", String.class);
    
    // Add to request header
    if (tenantId != null && !tenantId.isBlank()) {
        requestBuilder.header("X-Tenant-ID", tenantId);
    }
    
    // Forward to service
    return chain.filter(exchange.mutate()
        .request(requestBuilder.build())
        .build());
}
```

---

## Tenant Provisioning Flow

### How New Tenants Are Created

```
┌──────────────────────────────────────────────────────────────────┐
│ 1. TENANT REGISTRATION                                           │
│    POST /api/platform/tenants                                    │
│    {                                                              │
│      "code": "ACME",                                             │
│      "companyName": "ACME Corp",                                │
│      "contactEmail": "admin@acme.com",                          │
│      "contactPhone": "+1234567890"                              │
│    }                                                              │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│ 2. PLATFORM SERVICE (8085)                                       │
│    PlatformTenantService.register()                              │
│    • Normalizes tenant code: "ACME" → "acme"                     │
│    • Creates Tenant entity in public schema                      │
│    • Sets schemaName: "tenant_acme"                              │
│    • Publishes TenantRegisteredEvent to Kafka                    │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│ 3. KAFKA MESSAGE                                                 │
│    Topic: tenant-registered                                      │
│    Event: TenantRegisteredEvent {                                │
│      eventId: "uuid",                                            │
│      tenantCode: "acme",                                         │
│      companyName: "ACME Corp",                                   │
│      registeredAt: "2026-06-04T10:00:00"                        │
│    }                                                              │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│ 4. TENANT PROVISIONING SERVICE (8086)                            │
│    TenantRegisteredConsumer.onTenantRegistered()                 │
│    • Receives Kafka message                                      │
│    • Calls TenantSchemaProvisioningService.provisionTenant()    │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│ 5. SCHEMA CREATION                                               │
│    TenantSchemaProvisioningService.provisionTenant()             │
│    • SQL: CREATE SCHEMA IF NOT EXISTS tenant_acme;               │
│    • Initializes Flyway for tenant_acme schema                   │
│    • Runs migration scripts:                                     │
│      - V1__tenant_users.sql                                      │
│      - V2__customers.sql                                         │
│      - V3__meter_types_and_meters.sql                            │
│      - V4__meter_readings.sql                                    │
│      - V5__bills.sql                                             │
│      - V6__payments.sql                                          │
│      - V7__complaints.sql                                        │
│      - V8__tenant_indexes.sql                                    │
│      - V9__seed_meter_types.sql                                  │
│      - ... (V10-V14 additional tables/views)                     │
└────────────────────────────┬─────────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│ 6. SCHEMA READY                                                  │
│    PostgreSQL now has:                                           │
│    • tenant_acme schema with all tables                          │
│    • Data isolated from other tenants                            │
│    • Tenant can begin receiving requests                         │
└──────────────────────────────────────────────────────────────────┘
```

---

## Request Processing Example

### Request Flow for Customer Lookup

```
Step 1: Client sends request with JWT
────────────────────────────────────
GET /api/customers
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                              │ (contains tenantId: "acme")
                              │
                              ▼
Step 2: API Gateway (Port 4004)
────────────────────────────────────
JwtValidationGatewayFilterFactory:
  • Extracts JWT token
  • Validates signature
  • Extracts claims including tenantId: "acme"
  • Adds header: X-Tenant-ID: acme
  • Routes to customer-service:8081
                              │
                              ▼
Step 3: Customer Service (Port 8081)
────────────────────────────────────
TenantAwareFilter:
  • Reads header: X-Tenant-ID: acme
  • Calls TenantContext.setCurrentTenant("acme")
  • Proceeds with request
                              │
                              ▼
CustomerController:
  • Calls customerService.getAllCustomers()
  • JPA query: SELECT * FROM customers
                              │
                              ▼
Step 4: Hibernate Processing
────────────────────────────────────
TenantIdentifierResolver:
  • Calls resolveCurrentTenantIdentifier()
  • Reads TenantContext.getCurrentTenant() → "acme"
  • Returns "acme"
                              │
                              ▼
SchemaBasedMultiTenantConnectionProvider:
  • Receives tenant identifier: "acme"
  • Gets database connection
  • Executes: SET search_path TO tenant_acme, public;
  • Returns connection ready for tenant_acme schema
                              │
                              ▼
Step 5: Database Execution
────────────────────────────────────
PostgreSQL Connection on tenant_acme schema:
  SELECT * FROM customers;
  
  (Queries only tenant_acme.customers table)
  (Other tenants' customer data not accessible)
                              │
                              ▼
Step 6: Response
────────────────────────────────────
Customer Service returns customer list for ACME only
TenantAwareFilter.finally block clears context
Response sent to client
```

---

## Tenant Code Normalization

Both services use consistent tenant code validation:

```java
private String normalizeTenantCode(String code) {
    if (code == null || code.isBlank()) {
        throw new IllegalArgumentException("Tenant code is required");
    }
    String normalized = code.trim().toLowerCase().replace('-', '_');
    if (!normalized.matches("[a-z0-9_]+")) {
        throw new IllegalArgumentException("Invalid tenant code");
    }
    return normalized;
}
```

**Examples:**
- `ACME` → `acme`
- `Acme-Corp` → `acme_corp`
- `ACME_2024` → `acme_2024`
- `ACME-CORP!` → **ERROR** (invalid characters)

---

## Hibernate Configuration

Services using multi-tenancy have this configuration in `application.properties`:

```properties
spring.jpa.properties.hibernate.multiTenancy=SCHEMA
spring.jpa.properties.hibernate.multi_tenant_connection_provider=com.eps.shared.tenant.SchemaBasedMultiTenantConnectionProvider
spring.jpa.properties.hibernate.tenant_identifier_resolver=com.eps.shared.tenant.TenantIdentifierResolver
spring.flyway.enabled=false
```

**Why `spring.flyway.enabled=false`?**
- Tenant schemas are created dynamically
- Flyway migrations run programmatically via `TenantSchemaProvisioningService`
- Platform schema migrations use `spring.flyway.enabled=true`

---

## Services with Multi-Tenancy

✅ **Services with Schema-Based Multi-Tenancy:**
- `customer-service` (8081)
- `meter-service` (8082)
- `billing-service` (8090)
- `platform-billing-service` (8091)
- `complaint-service` (8092)
- `payment-service` (8093)
- `meter-reading-service` (8089)
- `connection-service` (8095)

❌ **Services WITHOUT Multi-Tenancy (Platform-only):**
- `auth-service` (8080) - Users are platform-level
- `geography-service` (8087) - Shared master data
- `notification-service` (8094) - Transactional service
- `platform-service` (8085) - Manages platform tenants (not tenant data)
- `tenant-user-service` (8088) - gRPC service for tenant users
- `tenant-provisioning-service` (8086) - Schema creation only
- `api-gateway` (4004) - Routing only

---

## Data Isolation Guarantees

### Complete Isolation
- Each tenant's data is in a separate PostgreSQL schema
- SQL queries automatically route to correct schema via `SET search_path`
- No cross-tenant data leakage possible at database level

### Request-Based Isolation
- Tenant context is set per-request via HTTP header
- Context is cleared after request completes (try-finally)
- No context bleeding between concurrent requests (ThreadLocal)

### Schema Naming Convention
- Schema name format: `tenant_{normalized_code}`
- Examples: `tenant_acme`, `tenant_globex`, `tenant_acme_corp`
- Platform data in `public` schema

---

## Fallback Behavior

**What if X-Tenant-ID header is missing?**
- `TenantAwareFilter` returns 400 Bad Request
- Message: "Missing X-Tenant-ID header"

**What if tenant doesn't exist in database?**
- `TenantIdentifierResolver` defaults to `public` schema
- May cause issues if accessing tenant-specific tables
- Best practice: Always validate tenant existence before querying

---

## Performance Considerations

### Connection Pooling
- Single datasource with connection pooling
- `SET search_path` executed per connection (low overhead)
- Pooled connections reused across tenants

### Schema-Level Isolation Benefits
- Database-enforced isolation (no application layer errors)
- Per-tenant backups/recovery possible
- Per-tenant performance monitoring available
- Per-tenant data deletion simple (DROP SCHEMA)

### Potential Bottlenecks
- Large number of tenants = large number of schemas
- Flyway migration time during tenant provisioning
- Connection pool size must account for concurrent tenants

---

## Summary

**Your project implements true schema-based multi-tenancy:**

✅ Tenants created on-demand (dynamic schema creation)
✅ Each tenant has isolated schema (`tenant_acme`, `tenant_globex`, etc.)
✅ Schema switching per-request via `SET search_path`
✅ ThreadLocal-based tenant context for request processing
✅ Kafka-driven asynchronous schema provisioning
✅ Complete data isolation at database level
✅ JWT tokens carry tenant information
✅ API Gateway injects `X-Tenant-ID` header
✅ Hibernate automatically routes queries to correct schema

This is a **production-grade multi-tenancy implementation**.
