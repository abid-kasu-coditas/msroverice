# Multi-Tenancy Quick Reference

## What is Implemented?

Your project uses **Schema-Based Multi-Tenancy** where each tenant gets a completely isolated PostgreSQL schema.

```
Tenant Registration → Kafka Event → Schema Created → Data Isolated → Requests Routed
```

---

## The Flow in 6 Steps

### 1️⃣ Tenant Registers
```
POST /api/platform/tenants
{
  "code": "ACME",
  "companyName": "ACME Corp"
}
```

### 2️⃣ Platform Service Saves Tenant
- Saves to **public** schema (platform metadata)
- Publishes `tenant-registered` Kafka event

### 3️⃣ Provisioning Service Creates Schema
- Receives Kafka event
- Executes: `CREATE SCHEMA IF NOT EXISTS tenant_acme`
- Runs Flyway migrations in `tenant_acme` schema
- Tables created: customers, meters, bills, payments, etc.

### 4️⃣ User Gets JWT Token
```json
{
  "sub": "user@acme.com",
  "tenantId": "acme",
  "role": "ADMIN"
}
```

### 5️⃣ Request Comes to API Gateway
```
GET /api/customers
Authorization: Bearer <jwt>
↓
API Gateway extracts tenantId: "acme"
Adds header: X-Tenant-ID: acme
Forwards to customer-service
```

### 6️⃣ Service Automatically Routes to Correct Schema
```
GET /api/customers (with X-Tenant-ID: acme header)
↓
TenantAwareFilter sets TenantContext = "acme"
↓
Hibernate asks: "Which tenant?"
↓
TenantIdentifierResolver reads TenantContext → "acme"
↓
SchemaBasedMultiTenantConnectionProvider executes:
  SET search_path TO tenant_acme, public;
↓
Query runs on tenant_acme schema only
↓
No data leakage possible!
```

---

## Key Files

| Component | File | Purpose |
|-----------|------|---------|
| **Thread Storage** | `shared/src/main/java/com/eps/shared/tenant/TenantContext.java` | Stores current tenant per request |
| **HTTP Filter** | `shared/src/main/java/com/eps/shared/tenant/TenantAwareFilter.java` | Extracts `X-Tenant-ID` header |
| **Tenant Resolver** | `shared/src/main/java/com/eps/shared/tenant/TenantIdentifierResolver.java` | Tells Hibernate which tenant |
| **Connection Provider** | `shared/src/main/java/com/eps/shared/tenant/SchemaBasedMultiTenantConnectionProvider.java` | Routes DB connections to correct schema |
| **Provisioning** | `tenant-provisioning-service/src/main/java/com/eps/tenantprovisioning/service/TenantSchemaProvisioningService.java` | Creates schemas on-demand |
| **Registration** | `platform-service/src/main/java/com/eps/platformservice/service/PlatformTenantService.java` | Tenant registration logic |

---

## Data Isolation Guarantee

### Database Level
```sql
-- ACME Company sees only their schema
SET search_path TO tenant_acme, public;
SELECT * FROM customers; -- Only ACME's customers

-- GLOBEX Company sees only their schema
SET search_path TO tenant_globex, public;
SELECT * FROM customers; -- Only GLOBEX's customers

-- Physical separation at database level
-- No way to accidentally access other tenant's data
```

### Application Level
```java
// TenantContext stored in ThreadLocal
// Cleared after every request
// No context bleeding between requests

try {
    TenantContext.setCurrentTenant("acme");
    // Process request
} finally {
    TenantContext.clear(); // Always cleared
}
```

---

## Services Using Multi-Tenancy

```
✅ Multi-Tenant Services:
  • customer-service (8081)
  • meter-service (8082)
  • billing-service (8090)
  • platform-billing-service (8091)
  • complaint-service (8092)
  • payment-service (8093)
  • meter-reading-service (8089)
  • connection-service (8095)

❌ Platform-Only Services:
  • auth-service (8080)          - Platform users
  • geography-service (8087)      - Shared master data
  • notification-service (8094)   - Transactional
  • platform-service (8085)       - Manages tenants
  • tenant-provisioning-service   - Creates schemas
  • tenant-user-service (8088)    - gRPC service
  • api-gateway (4004)            - Routing only
```

---

## Tenant Code Rules

```java
// Valid tenant codes:
"ACME"           → "acme"
"Acme Corp"      → "acme_corp"      (space → underscore)
"ACME-2024"      → "acme_2024"      (dash → underscore)
"acme_corp"      → "acme_corp"      (already lowercase)

// Invalid tenant codes:
"ACME CORP!"     ❌ (special characters)
"123-ABC"        ❌ (spaces not allowed)
"@acme"          ❌ (special characters)
```

Schema naming: `tenant_{normalized_code}`
- `tenant_acme`
- `tenant_acme_corp`
- `tenant_acme_2024`

---

## Configuration

### Services with Multi-Tenancy
```properties
spring.jpa.properties.hibernate.multiTenancy=SCHEMA
spring.jpa.properties.hibernate.multi_tenant_connection_provider=com.eps.shared.tenant.SchemaBasedMultiTenantConnectionProvider
spring.jpa.properties.hibernate.tenant_identifier_resolver=com.eps.shared.tenant.TenantIdentifierResolver
spring.flyway.enabled=false  # Migrations run programmatically
```

### Platform Services (no multi-tenancy)
```properties
spring.flyway.enabled=true    # Migrations run automatically
spring.flyway.schemas=public  # Target public schema only
```

---

## Migration Files

When a new tenant is created, these SQL files run automatically:

```
V1__tenant_users.sql           → tenant_users table
V2__customers.sql              → customers table
V3__meter_types_and_meters.sql → meter_types, meters
V4__meter_readings.sql         → meter_readings table
V5__bills.sql                  → bills table
V6__payments.sql               → payments table
V7__complaints.sql             → complaints table
V8__tenant_indexes.sql         → Performance indexes
V9__seed_meter_types.sql       → Initial data
V10-V14__...                   → Additional tables/views
```

Location: `tenant-provisioning-service/src/main/resources/db/tenant-migration/`

---

## Request Header Flow

```
Client
  ↓
[JWT token contains tenantId: "acme"]
  ↓
API Gateway (4004)
  ↓ JwtValidationGatewayFilterFactory
  │ Extracts: tenantId = "acme"
  │ Adds header: X-Tenant-ID: acme
  ↓
Customer Service (8081)
  ↓ TenantAwareFilter
  │ Reads: X-Tenant-ID header = "acme"
  │ Sets: TenantContext.setCurrentTenant("acme")
  │ Validates: Must not be blank (returns 400 if missing)
  ↓
CustomerService Business Logic
  │ JPA query: repo.findAll()
  ↓
Hibernate ORM
  │ Asks TenantIdentifierResolver: "Which tenant?"
  │ Gets: "acme"
  ↓
SchemaBasedMultiTenantConnectionProvider
  │ Executes: SET search_path TO tenant_acme, public;
  │ Returns connection pointing to tenant_acme schema
  ↓
PostgreSQL
  │ Query runs on tenant_acme schema
  │ Returns only ACME's customer data
  ↓
TenantAwareFilter (finally block)
  │ Clears: TenantContext.clear()
  ↓
Response sent to client
```

---

## Kafka Event Flow

```
1. Tenant Registered
   POST /api/platform/tenants
   ↓
   PlatformTenantService.register()
   ↓
   TenantEventPublisher.publishRegistered()
   ↓
   Kafka: tenant-registered topic
   
2. Kafka Message Published
   Topic: tenant-registered
   Key: tenant_code (e.g., "acme")
   Value: TenantRegisteredEvent protobuf message
   
3. Consumed by Provisioning Service
   @KafkaListener(topics = "tenant-registered")
   TenantRegisteredConsumer.onTenantRegistered()
   ↓
   TenantSchemaProvisioningService.provisionTenant()
   ↓
   Creates schema tenant_acme
   ↓
   Runs Flyway migrations
   ↓
   Publishes tenant-provisioned event
   
4. Ready for Use
   Tenant can now receive requests
   Data is isolated in tenant_acme schema
```

---

## Error Cases

### Missing X-Tenant-ID Header
```
Request: GET /api/customers
         (no X-Tenant-ID header)

Response: 400 Bad Request
          "Missing X-Tenant-ID header"
```

### Invalid Tenant Code
```
Request: POST /api/platform/tenants
         { "code": "ACME-CORP!" }

Response: 400 Bad Request
          "Invalid tenant code: ACME-CORP!"
```

---

## Production Readiness Checklist

✅ **Implemented:**
- Database-level schema isolation
- Per-request tenant context management
- ThreadLocal-based (no cross-request bleeding)
- Flyway migrations per tenant
- Kafka-driven async provisioning
- Tenant code validation
- Complete data isolation

✅ **Tested Pattern:**
- Schema-based multi-tenancy is a proven pattern
- Used by SaaS platforms worldwide
- Better isolation than row-level security

⚠️ **Consider for Production:**
- Connection pool sizing (account for tenant concurrency)
- Monitoring per-tenant schema health
- Backup strategy per-tenant
- Per-tenant audit logging
- Tenant quota enforcement (max records, etc.)

---

## Summary

Your implementation is **production-grade**:
- ✅ True data isolation (schema-level)
- ✅ Dynamic tenant provisioning
- ✅ Per-request tenant routing
- ✅ Zero configuration per service
- ✅ Automatic connection pool management
- ✅ Event-driven architecture

The system is designed to scale to hundreds or thousands of tenants with zero code changes.
