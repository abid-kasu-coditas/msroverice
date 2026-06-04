# Platform Workforce Hierarchy - Understanding, Gap Analysis, and Implementation Plan

## Purpose

This document captures what I understand from the original platform requirements, what I found in the current implementation, and what I plan to implement to complete the missing platform-side workforce hierarchy and management structure.

The focus is specifically on platform workforce users and workflows:

- SUPER_ADMIN management
- MANAGEMENT users
- SALES_POC management
- STATE_HEAD management
- DISTRICT_HEAD management
- CITY_HEAD management
- CRM management
- TECHNICIAN management
- BILLER management

This document also checks whether the platform supports the expected hierarchy, permissions, APIs, services, database tables, and workflows.

## Current Implementation Status

The missing platform-side hierarchy support has now been implemented in the focused platform modules:

- `employee-service` now uses auth-consistent workforce roles and validates reporting hierarchy and territory assignment.
- `employee-service` now exposes hierarchy APIs for territory search, subordinate lookup, manager assignment, role lookup, and employee status updates.
- `client-onboarding-service` now requires and stores `salesPocId` so onboarded tenants are owned by a platform Sales POC.
- `client-onboarding-service` now exposes a lookup API for clients assigned to a Sales POC.
- `auth-service` now issues JWTs with `role` and `userId` claims and exposes `/auth/validate`.
- `auth-service` now has a JWT authentication filter so method-level authorization can protect registration and user-management APIs.
- `api-gateway` now validates JWTs, forwards authenticated user headers, and enforces route-level role authorization for workforce, onboarding, customer, field, billing, complaint, analytics, notification, audit, and auth-management routes.
- `api-gateway` no longer has duplicate unprotected property-based routes.
- `docker-compose.yml` now includes the newly routed platform services in the gateway startup dependencies.

Verification completed:

```text
mvn -q -DskipTests -pl api-gateway,auth-service,employee-service,client-onboarding-service compile
```

The targeted platform modules compiled successfully.

Note: a full root compile reached `meter-service` and then failed in the protobuf Maven plugin while deleting an existing generated `target/protoc-dependencies` directory. That failure was outside the edited platform hierarchy modules.

## Sources Reviewed

I reviewed the following local project/reference materials:

- `project.md`
- `electricity-distribution-platform/README.md`
- `electricity-distribution-platform/IMPLEMENTATION_GUIDE.md`
- `electricity-distribution-platform/PROJECT_STATUS.md`
- `electricity-distribution-platform/ARCHITECTURE.md`
- `electricity-distribution-platform/PROJECT_COMPLETE.md`
- `java-spring-microservices/README.md`
- Relevant source code under:
  - `auth-service`
  - `api-gateway`
  - `employee-service`
  - `client-onboarding-service`
  - customer/connection/meter/billing/payment/complaint/notification/analytics/audit services

I also checked the local `java-spring-microservices` reference project for the gateway/auth pattern. The reference gateway validates JWTs through a gateway filter and routes protected traffic through that gateway.

## High-Level Understanding

The platform is not only a customer-facing electricity management system. It is also a workforce-managed, multi-tenant electricity distribution platform.

The customer-facing and operational modules handle business objects such as:

- Customers
- Connections
- Meters
- Billing
- Payments
- Complaints
- Notifications
- Analytics
- Audit logs

However, the platform requirements also need a separate internal management layer that controls who can run and supervise those operations.

That internal layer must support:

- Platform-level ownership by SUPER_ADMIN.
- Management users who can manage and oversee platform workforce users.
- Sales POC users who onboard electricity provider clients/tenants.
- State, district, and city heads who manage operations within assigned territories.
- CRM users who handle customer support and relationship workflows.
- Technician users who handle field operations.
- Biller users who handle billing workflows.

## Required Platform Hierarchy

The expected hierarchy is:

```text
SUPER_ADMIN
  |
  +-- MANAGEMENT
        |
        +-- SALES_POC
        +-- STATE_HEAD
              |
              +-- DISTRICT_HEAD
                    |
                    +-- CITY_HEAD
                          |
                          +-- CRM
                          +-- TECHNICIAN
                          +-- BILLER
```

MANAGEMENT can also directly oversee SALES_POC, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD, CRM, TECHNICIAN, and BILLER users when needed.

SUPER_ADMIN has full platform access and can create/manage any platform workforce role.

## Role Responsibilities Understood

### SUPER_ADMIN

SUPER_ADMIN is the top-level platform owner role.

Expected responsibilities:

- Full system access.
- Create, update, deactivate, terminate, and delete platform workforce users.
- Manage MANAGEMENT users.
- Override or inspect all platform operations.
- Access all employee, client onboarding, customer, billing, complaint, analytics, and audit information.
- Perform emergency administrative actions.

### MANAGEMENT

MANAGEMENT users are platform managers.

Expected responsibilities:

- Manage and oversee:
  - SALES_POC
  - STATE_HEAD
  - DISTRICT_HEAD
  - CITY_HEAD
  - TECHNICIAN
  - BILLER
  - CRM
- View the full workforce hierarchy.
- Assign or reassign reporting managers.
- Assign platform users to territories.
- Monitor platform operations across states, districts, and cities.
- Access operational data for oversight.

MANAGEMENT should not automatically have the same unrestricted ownership power as SUPER_ADMIN where destructive or top-level actions are concerned.

### SALES_POC

SALES_POC users are responsible for client/tenant onboarding.

Expected responsibilities:

- Onboard electricity provider companies such as Reliance Power, Tata Power, MSEB, etc.
- Manage the client onboarding lifecycle.
- Ensure each onboarded client/tenant is linked to the responsible Sales POC.
- View clients assigned to them.
- Track onboarding status and tenant schema/database setup.

SALES_POC should not be treated as a general employee manager. Their workflow is client onboarding and client relationship setup.

### STATE_HEAD

STATE_HEAD users manage platform operations within one assigned state.

Expected responsibilities:

- Be assigned to a state.
- View or manage district heads within that state.
- View territory-level operational data for the state.
- Oversee customer, connection, complaint, meter, and field activity inside the state.

STATE_HEAD should not manage users or operations outside their assigned state.

### DISTRICT_HEAD

DISTRICT_HEAD users manage operations within one assigned district.

Expected responsibilities:

- Be assigned to a state and district.
- Report to a STATE_HEAD whose assigned state matches.
- View or manage city heads within the assigned district.
- View operations inside the district.

DISTRICT_HEAD should not manage users or operations outside their assigned district.

### CITY_HEAD

CITY_HEAD users manage operations within one assigned city or town.

Expected responsibilities:

- Be assigned to a state, district, and city/town.
- Report to a DISTRICT_HEAD whose state and district match.
- Manage or oversee:
  - CRM users assigned to the city
  - TECHNICIAN users assigned to the city
  - BILLER users assigned to the city
- View city-level operations.

CITY_HEAD should not manage users or operations outside their assigned city/town.

### CRM

CRM users handle customer relationship and support activities.

Expected responsibilities:

- Create and update customer records.
- Handle customer communication and support.
- Open, update, and resolve customer complaints where applicable.
- Work within an assigned city/town.
- Report to a CITY_HEAD.

CRM should not manage platform employees.

### TECHNICIAN

TECHNICIAN users handle field operations.

Expected responsibilities:

- Meter installation.
- Meter replacement.
- Meter maintenance.
- Complaint assignment and field resolution.
- Field work within an assigned city/town.
- Report to a CITY_HEAD.

TECHNICIAN should not manage platform employees.

### BILLER

BILLER users handle billing-related operations.

Expected responsibilities:

- Upload meter readings/photos.
- Trigger or support bill generation.
- Work with billing records.
- Work within an assigned city/town.
- Report to a CITY_HEAD.

BILLER should not manage platform employees.

## Current Implementation Findings

### What Exists

The project currently contains these relevant services:

- `auth-service`
- `api-gateway`
- `employee-service`
- `client-onboarding-service`
- customer/connection/meter/billing/payment/complaint/notification/analytics/audit services

The `auth-service` has a `UserRole` enum with the expected platform role names:

- `SUPER_ADMIN`
- `MANAGEMENT`
- `SALES_POC`
- `STATE_HEAD`
- `DISTRICT_HEAD`
- `CITY_HEAD`
- `CRM`
- `TECHNICIAN`
- `BILLER`

The `auth-service` also seeds users for the major platform roles in `data.sql`.

The `employee-service` exists and contains:

- `Employee`
- `EmployeeRole`
- `EmployeeStatus`
- `EmployeeRequestDTO`
- `EmployeeResponseDTO`
- `EmployeeMapper`
- `EmployeeRepository`
- `EmployeeService`
- `EmployeeController`

The `client-onboarding-service` exists and can create tenant/client company schemas.

### What Is Partially Implemented

The platform has role names and a basic employee table, but it does not fully implement the workforce hierarchy.

The current employee service is mostly generic CRUD:

- Create employee
- Update employee
- Delete employee
- Terminate employee
- Get employees by role
- Get employees by state

The current client onboarding service creates client companies and schemas, but it does not clearly associate client onboarding with a responsible SALES_POC.

The gateway has routes for operational services but initially did not expose employee/client onboarding routes through the main entry point.

### What Is Missing or Incomplete

The following platform-side requirements are missing or incomplete:

1. No complete workforce hierarchy enforcement.
2. No validation that a DISTRICT_HEAD belongs under a matching STATE_HEAD.
3. No validation that a CITY_HEAD belongs under a matching DISTRICT_HEAD.
4. No validation that CRM, TECHNICIAN, and BILLER users belong under a matching CITY_HEAD.
5. No clear permission model for who can create/update/delete platform workforce users.
6. No complete role-based route permission model in the API gateway.
7. No employee API endpoints for:
   - getting subordinates
   - assigning/reassigning managers
   - filtering by territory
   - updating employee status
   - viewing hierarchy by manager
8. No link between `Employee` records and auth users.
9. No Sales POC ownership field on client onboarding records.
10. No endpoint to list clients assigned to a specific Sales POC.
11. No explicit workflow ensuring SALES_POC owns client/tenant onboarding.
12. Auth JWTs did not carry enough role information for gateway-level role enforcement.
13. Gateway did not yet mirror the reference project's JWT validation pattern completely for protected routes.
14. Documentation claimed role support, but source implementation was not yet complete.

## Important Role Name Mismatch Found

The `auth-service` role enum uses:

```text
MANAGEMENT
SALES_POC
CITY_HEAD
```

But `employee-service` initially used:

```text
MANAGEMENT_TEAM
SALES_TEAM
CITY_TOWN_HEAD
```

This mismatch breaks consistency between authentication roles and workforce records.

The employee service should use the same role names as auth:

```text
SUPER_ADMIN
MANAGEMENT
SALES_POC
STATE_HEAD
DISTRICT_HEAD
CITY_HEAD
CRM
TECHNICIAN
BILLER
```

## Current Platform Completion Assessment for Workforce Requirements

### SUPER_ADMIN Management

Status: Partially implemented.

What exists:

- Role exists in auth.
- Seed user exists.
- Auth user management endpoints exist.

Missing:

- Full gateway-level permission model.
- Full employee management workflows.
- Explicit SUPER_ADMIN access to all workforce hierarchy operations.

### MANAGEMENT Users

Status: Partially implemented.

What exists:

- Role exists in auth.
- Seed user exists.

Missing:

- Management oversight workflows.
- Workforce hierarchy APIs.
- Ability to manage Sales POC, State Head, District Head, City Head, Technician, Biller, and CRM users through enforced service logic.

### SALES_POC Management

Status: Partially implemented.

What exists:

- Role exists in auth.
- Seed user exists.
- Client onboarding service exists.

Missing:

- Client onboarding records are not linked to responsible Sales POC.
- SALES_POC-specific client onboarding APIs are missing.
- Gateway role permissions for onboarding are incomplete.

### STATE_HEAD Management

Status: Partially implemented.

What exists:

- Role exists in auth and employee role enum.
- Employee has `assignedState`.

Missing:

- Required territory validation.
- Reporting-line validation.
- State-level subordinate lookup.
- State-scoped operational access.

### DISTRICT_HEAD Management

Status: Partially implemented.

What exists:

- Role exists in auth and employee role enum.
- Employee has `assignedDistrict`.

Missing:

- Must report to matching STATE_HEAD.
- Required state and district validation.
- District-level subordinate lookup.
- District-scoped operational access.

### CITY_HEAD Management

Status: Partially implemented.

What exists:

- Role exists in auth.
- Employee has `assignedCity`.

Missing:

- Employee role enum initially did not match auth.
- Must report to matching DISTRICT_HEAD.
- Required city/town validation.
- City-level subordinate lookup.
- Ability to oversee CRM, TECHNICIAN, and BILLER users.

### CRM Management

Status: Partially implemented.

What exists:

- Role exists in auth and employee service.
- Customer and complaint services exist.

Missing:

- CRM assignment to city and CITY_HEAD not fully validated.
- Gateway permissions for CRM customer/support workflows need to be defined.
- CRM-specific support workflows are not fully encoded.

### TECHNICIAN Management

Status: Partially implemented.

What exists:

- Role exists in auth and employee service.
- Meter and complaint services exist.

Missing:

- Technician assignment to city and CITY_HEAD not fully validated.
- Technician-specific endpoints/workflows for meter installation/replacement/maintenance and complaint assignments are not fully role-protected.

### BILLER Management

Status: Partially implemented.

What exists:

- Role exists in auth and employee service.
- Billing and meter reading services exist.

Missing:

- Biller assignment to city and CITY_HEAD not fully validated.
- Billing/meter reading route permissions need to explicitly allow BILLER where appropriate.

## Implementation Strategy

The implementation should stay consistent with the existing Spring Boot microservice architecture and the `java-spring-microservices` reference style.

I will avoid a large unrelated refactor. The implementation should be scoped to:

- `employee-service`
- `client-onboarding-service`
- `auth-service`
- `api-gateway`
- documentation

Operational services may receive only minimal permission annotations or route permission changes if required.

## Planned Implementation Details

## 1. Employee Service - Workforce Hierarchy Core

### 1.1 Align Employee Roles with Auth Roles

Update `EmployeeRole` to match `auth-service` exactly:

```java
public enum EmployeeRole {
  SUPER_ADMIN,
  MANAGEMENT,
  SALES_POC,
  STATE_HEAD,
  DISTRICT_HEAD,
  CITY_HEAD,
  CRM,
  TECHNICIAN,
  BILLER
}
```

This ensures employee records and JWT roles use the same vocabulary.

### 1.2 Expand Employee Entity

The employee table should support:

- auth user linkage
- reporting manager
- territory assignment
- role
- status

Planned fields:

```java
UUID id;
UUID authUserId;
String firstName;
String lastName;
String email;
String phone;
EmployeeRole role;
UUID parentId;
String assignedState;
String assignedDistrict;
String assignedCity;
EmployeeStatus status;
LocalDate joinedDate;
```

Database table:

```text
employees
```

Important indexes:

- role
- parent_id
- assigned_state, assigned_district, assigned_city

### 1.3 Employee Territory Validation

Rules to implement:

| Role | Required Territory |
|---|---|
| SUPER_ADMIN | none |
| MANAGEMENT | optional |
| SALES_POC | optional |
| STATE_HEAD | state required |
| DISTRICT_HEAD | state + district required |
| CITY_HEAD | state + district + city required |
| CRM | state + district + city required |
| TECHNICIAN | state + district + city required |
| BILLER | state + district + city required |

### 1.4 Employee Reporting-Line Validation

Rules to implement:

| Employee Role | Allowed Parent Role |
|---|---|
| SUPER_ADMIN | no parent |
| MANAGEMENT | SUPER_ADMIN |
| SALES_POC | SUPER_ADMIN or MANAGEMENT |
| STATE_HEAD | SUPER_ADMIN or MANAGEMENT |
| DISTRICT_HEAD | SUPER_ADMIN, MANAGEMENT, or STATE_HEAD |
| CITY_HEAD | SUPER_ADMIN, MANAGEMENT, or DISTRICT_HEAD |
| CRM | SUPER_ADMIN, MANAGEMENT, or CITY_HEAD |
| TECHNICIAN | SUPER_ADMIN, MANAGEMENT, or CITY_HEAD |
| BILLER | SUPER_ADMIN, MANAGEMENT, or CITY_HEAD |

Additional territory validation:

- If parent is STATE_HEAD, child must have the same assigned state.
- If parent is DISTRICT_HEAD, child must have the same assigned state and district.
- If parent is CITY_HEAD, child must have the same assigned state, district, and city.

### 1.5 Employee Service Methods

Add or update service methods:

```java
List<EmployeeResponseDTO> getAllEmployees();
EmployeeResponseDTO getEmployeeById(UUID id);
EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);
EmployeeResponseDTO updateEmployee(UUID id, EmployeeRequestDTO dto);
void deleteEmployee(UUID id);
EmployeeResponseDTO updateEmployeeStatus(UUID id, EmployeeStatus status);
void terminateEmployee(UUID id);
List<EmployeeResponseDTO> getEmployeesByRole(EmployeeRole role);
List<EmployeeResponseDTO> getEmployeesByState(String state);
List<EmployeeResponseDTO> getEmployeesByTerritory(String state, String district, String city);
List<EmployeeResponseDTO> getSubordinates(UUID managerId);
EmployeeResponseDTO assignManager(UUID employeeId, UUID managerId);
```

### 1.6 Employee API Endpoints

Planned endpoints:

```text
GET    /api/employees
POST   /api/employees
GET    /api/employees/{id}
PUT    /api/employees/{id}
DELETE /api/employees/{id}

POST   /api/employees/{id}/terminate
PUT    /api/employees/{id}/status
PUT    /api/employees/{id}/manager/{managerId}

GET    /api/employees/role/{role}
GET    /api/employees/state/{state}
GET    /api/employees/territory?state=&district=&city=
GET    /api/employees/{id}/subordinates
```

### 1.7 Employee API Permissions

Gateway and/or controller permissions should enforce:

| Endpoint/Action | Roles |
|---|---|
| Create workforce user | SUPER_ADMIN, MANAGEMENT |
| Update workforce user | SUPER_ADMIN, MANAGEMENT |
| Delete workforce user | SUPER_ADMIN |
| Terminate workforce user | SUPER_ADMIN, MANAGEMENT |
| Assign manager | SUPER_ADMIN, MANAGEMENT |
| View all employees | SUPER_ADMIN, MANAGEMENT |
| View subordinates | SUPER_ADMIN, MANAGEMENT, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |
| View territory employees | SUPER_ADMIN, MANAGEMENT, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |

Service-level hierarchy validation is still required because route permissions alone cannot verify valid reporting lines.

## 2. Client Onboarding Service - SALES_POC Ownership

### 2.1 Add Sales POC Ownership to ClientCompany

Add:

```java
UUID salesPocId;
```

This links each onboarded tenant/client company to the responsible platform Sales POC employee.

### 2.2 Update Client Onboarding DTOs

Add `salesPocId` to:

- `ClientOnboardingRequestDTO`
- `ClientOnboardingResponseDTO`

### 2.3 Update Client Onboarding Mapper

Map:

```java
request.salesPocId -> ClientCompany.salesPocId
ClientCompany.salesPocId -> response.salesPocId
```

### 2.4 Add Repository Method

Add:

```java
List<ClientCompany> findBySalesPocId(UUID salesPocId);
```

### 2.5 Add Service Method

Add:

```java
List<ClientOnboardingResponseDTO> getClientsBySalesPoc(UUID salesPocId);
```

### 2.6 Add API Endpoint

Add:

```text
GET /api/onboarding/sales-poc/{salesPocId}
```

### 2.7 Client Onboarding Permissions

| Endpoint/Action | Roles |
|---|---|
| Onboard client | SUPER_ADMIN, MANAGEMENT, SALES_POC |
| View all clients | SUPER_ADMIN, MANAGEMENT |
| View client by ID | SUPER_ADMIN, MANAGEMENT, SALES_POC |
| View clients by Sales POC | SUPER_ADMIN, MANAGEMENT, SALES_POC |
| Delete/offboard client | SUPER_ADMIN, MANAGEMENT |

## 3. Auth Service - JWT Role Support

### 3.1 Add Role Claim to JWT

Current JWTs need to include the authenticated user's role.

Expected claims:

```text
sub = username
role = ROLE_NAME
```

Example:

```json
{
  "sub": "management",
  "role": "MANAGEMENT"
}
```

The gateway can then make authorization decisions without calling every backend service for user metadata.

### 3.2 Add Validate Token Endpoint

The reference gateway expects an auth validation endpoint.

Add:

```text
GET /auth/validate
```

Behavior:

- Reads `Authorization: Bearer <token>`.
- Returns `200 OK` if token is valid.
- Returns `401 Unauthorized` if token is missing, malformed, expired, or invalid.

### 3.3 Align Auth Security with Reference

The reference auth service permits requests internally and uses the gateway as the main protection layer.

The EPS auth service should:

- Permit login and validation.
- Allow gateway-driven authentication flow.
- Avoid accidentally blocking routes due to incomplete local Spring Security configuration.

## 4. API Gateway - JWT Validation and Role-Based Route Permissions

### 4.1 Add JWT Validation Filter

Add a gateway filter similar to the reference project:

```text
JwtValidationGatewayFilterFactory
```

Expected behavior:

- Check `Authorization` header.
- Require `Bearer <token>` for protected routes.
- Validate token signature and expiration.
- Extract `role` claim.
- Enforce allowed roles for the route.

The gateway should return:

- `401 Unauthorized` for missing/invalid token.
- `403 Forbidden` for valid token with insufficient role.

### 4.2 Add Gateway Route Coverage

The gateway should route all relevant platform APIs:

```text
/auth/**              -> auth-service
/api/customers/**     -> customer-service
/api/connections/**   -> connection-service
/api/meters/**        -> meter-service
/api/bills/**         -> billing-service
/api/payments/**      -> payment-service
/api/complaints/**    -> complaint-service
/api/notifications/** -> notification-service
/api/analytics/**     -> analytics-service
/api/audit/**         -> audit-service
/api/onboarding/**    -> client-onboarding-service
/api/employees/**     -> employee-service
```

### 4.3 Route-Level Role Model

Planned route permissions:

| Route | Allowed Roles |
|---|---|
| `/auth/**` | public or auth-managed |
| `/api/employees/**` | SUPER_ADMIN, MANAGEMENT, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |
| `/api/onboarding/**` | SUPER_ADMIN, MANAGEMENT, SALES_POC |
| `/api/customers/**` | SUPER_ADMIN, MANAGEMENT, CRM, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |
| `/api/connections/**` | SUPER_ADMIN, MANAGEMENT, CRM, TECHNICIAN, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |
| `/api/meters/**` | SUPER_ADMIN, MANAGEMENT, TECHNICIAN, BILLER, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |
| `/api/bills/**` | SUPER_ADMIN, MANAGEMENT, BILLER |
| `/api/payments/**` | SUPER_ADMIN, MANAGEMENT, BILLER |
| `/api/complaints/**` | SUPER_ADMIN, MANAGEMENT, CRM, TECHNICIAN, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |
| `/api/notifications/**` | SUPER_ADMIN, MANAGEMENT, CRM |
| `/api/analytics/**` | SUPER_ADMIN, MANAGEMENT, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD |
| `/api/audit/**` | SUPER_ADMIN, MANAGEMENT |

## 5. Operational Workflow Mapping

The workforce hierarchy must support these workflows.

### 5.1 Management Creates Workforce

```text
MANAGEMENT logs in
  -> creates STATE_HEAD
  -> creates DISTRICT_HEAD under STATE_HEAD
  -> creates CITY_HEAD under DISTRICT_HEAD
  -> creates CRM/TECHNICIAN/BILLER under CITY_HEAD
```

Validation:

- Roles must be valid.
- Parent role must be valid.
- Territory must match parent territory.

### 5.2 Sales POC Onboards Client

```text
SALES_POC logs in
  -> POST /api/onboarding/register
  -> client company created
  -> tenant schema created
  -> client company linked to salesPocId
```

Validation:

- SALES_POC has permission to onboard clients.
- Client registration number must be unique.
- Client schema name must be generated safely.

### 5.3 Territory Heads Oversee Operations

```text
STATE_HEAD views employees/operations for assigned state
DISTRICT_HEAD views employees/operations for assigned district
CITY_HEAD views employees/operations for assigned city
```

Validation:

- Territory lookup exists.
- Territory assignments exist on workforce records.
- Gateway permits hierarchy roles.

Future improvement:

- Add request-context based territory scoping so a STATE_HEAD cannot query other states.

### 5.4 CRM Handles Customer Support

```text
CRM logs in
  -> creates/updates customers
  -> creates/updates complaints
  -> resolves customer support issues
```

Validation:

- CRM has route access to customer/complaint workflows.
- CRM belongs to assigned territory.

### 5.5 Technician Handles Field Work

```text
TECHNICIAN logs in
  -> handles meter installation/replacement/maintenance
  -> handles assigned field complaints
```

Validation:

- TECHNICIAN has route access to meter/connection/complaint workflows.
- Technician belongs to assigned city.

### 5.6 Biller Handles Billing Work

```text
BILLER logs in
  -> uploads meter reading/photo
  -> supports bill generation
  -> views billing/payment status
```

Validation:

- BILLER has route access to meter/billing/payment workflows.
- Biller belongs to assigned city.

## 6. Database Tables Needed

### Existing Tables

Current implementation already has or should have:

- `users` in auth service
- `employees` in employee service
- `client_company` or equivalent client onboarding table
- customer, connection, meter, billing, payment, complaint, analytics, notification, audit tables

### Tables/Columns to Add or Update

Employee service:

```text
employees
  id
  auth_user_id
  first_name
  last_name
  email
  phone
  role
  parent_id
  assigned_state
  assigned_district
  assigned_city
  status
  joined_date
```

Client onboarding service:

```text
client_company
  sales_poc_id
```

Auth service:

No new table required for the immediate plan.

JWT must include role claim.

## 7. API Request Files to Add Later

After code implementation, add or update `.http` files for:

```text
api-requests/platform-employees.http
api-requests/client-onboarding.http
api-requests/auth.http
```

Examples should cover:

- Login as SUPER_ADMIN
- Login as MANAGEMENT
- Create MANAGEMENT user
- Create SALES_POC
- Create STATE_HEAD
- Create DISTRICT_HEAD under STATE_HEAD
- Create CITY_HEAD under DISTRICT_HEAD
- Create CRM/TECHNICIAN/BILLER under CITY_HEAD
- Assign manager
- Query subordinates
- Query territory employees
- Onboard client as SALES_POC
- Query clients by Sales POC

## 8. Tests to Add Later

Recommended tests:

### Employee Service Unit Tests

- Create SUPER_ADMIN without parent.
- Reject SUPER_ADMIN with parent.
- Create MANAGEMENT under SUPER_ADMIN.
- Create SALES_POC under MANAGEMENT.
- Create STATE_HEAD with assigned state.
- Reject STATE_HEAD without assigned state.
- Create DISTRICT_HEAD under matching STATE_HEAD.
- Reject DISTRICT_HEAD under non-matching STATE_HEAD.
- Create CITY_HEAD under matching DISTRICT_HEAD.
- Reject CITY_HEAD under non-matching DISTRICT_HEAD.
- Create CRM/TECHNICIAN/BILLER under matching CITY_HEAD.
- Reject CRM/TECHNICIAN/BILLER under non-matching CITY_HEAD.
- Reject delete when employee has subordinates.

### Client Onboarding Tests

- Onboard client with `salesPocId`.
- Reject duplicate registration number.
- List clients by Sales POC.

### Gateway Tests

- Missing token returns 401.
- Invalid token returns 401.
- Valid token with wrong role returns 403.
- Valid MANAGEMENT token can access `/api/employees/**`.
- Valid SALES_POC token can access `/api/onboarding/**`.
- Valid BILLER token can access billing routes.

## 9. Implementation Order

Recommended implementation order:

1. Finish `employee-service` role alignment and hierarchy validations.
2. Add employee hierarchy endpoints.
3. Add Sales POC ownership to `client-onboarding-service`.
4. Add client onboarding endpoints for Sales POC lookup.
5. Update `auth-service` JWT role claims and validation endpoint.
6. Update `api-gateway` JWT validation and route permissions.
7. Update Docker/gateway routes for employee and onboarding services.
8. Compile impacted modules.
9. Fix compile issues.
10. Add documentation and API request examples.
11. Add targeted tests.

## 10. Files Expected to Change

Likely files:

```text
employee-service/src/main/java/com/eps/employeeservice/model/EmployeeRole.java
employee-service/src/main/java/com/eps/employeeservice/model/Employee.java
employee-service/src/main/java/com/eps/employeeservice/dto/EmployeeRequestDTO.java
employee-service/src/main/java/com/eps/employeeservice/dto/EmployeeResponseDTO.java
employee-service/src/main/java/com/eps/employeeservice/mapper/EmployeeMapper.java
employee-service/src/main/java/com/eps/employeeservice/repository/EmployeeRepository.java
employee-service/src/main/java/com/eps/employeeservice/service/EmployeeService.java
employee-service/src/main/java/com/eps/employeeservice/controller/EmployeeController.java

client-onboarding-service/src/main/java/com/eps/clientonboarding/model/ClientCompany.java
client-onboarding-service/src/main/java/com/eps/clientonboarding/dto/ClientOnboardingRequestDTO.java
client-onboarding-service/src/main/java/com/eps/clientonboarding/dto/ClientOnboardingResponseDTO.java
client-onboarding-service/src/main/java/com/eps/clientonboarding/mapper/ClientOnboardingMapper.java
client-onboarding-service/src/main/java/com/eps/clientonboarding/repository/ClientCompanyRepository.java
client-onboarding-service/src/main/java/com/eps/clientonboarding/service/ClientOnboardingService.java
client-onboarding-service/src/main/java/com/eps/clientonboarding/controller/ClientOnboardingController.java

auth-service/src/main/java/com/eps/authservice/util/JwtUtil.java
auth-service/src/main/java/com/eps/authservice/controller/AuthController.java
auth-service/src/main/java/com/eps/authservice/config/SecurityConfig.java

api-gateway/src/main/java/com/eps/apigateway/ApiGatewayApplication.java
api-gateway/src/main/java/com/eps/apigateway/filter/JwtValidationGatewayFilterFactory.java
api-gateway/src/main/java/com/eps/apigateway/exception/JwtValidationException.java
api-gateway/src/main/resources/application.properties

docker-compose.yml
README.md
PROJECT_STATUS.md
```

## 11. Work Already Started Before This Document

Some initial employee-service code edits were started before this document was requested:

- `EmployeeRole` was changed to align with auth role names.
- `Employee` was expanded with `authUserId`, table/index metadata, and stricter column definitions.
- `EmployeeRequestDTO` and `EmployeeResponseDTO` were updated with `authUserId` and validation annotations.
- `EmployeeMapper` was updated for `authUserId`.
- `EmployeeRepository` was updated to use `EmployeeRole` instead of `String` for role lookup and to support territory/subordinate queries.
- `EmployeeService` was started with hierarchy validation logic.

The employee controller, client onboarding, auth, gateway, tests, and docs still need to be finished.

## 12. Key Risks and Notes

### Route Permission vs Data Permission

Gateway role checks can confirm a user has a role such as STATE_HEAD, but they do not automatically prove the user is only accessing their assigned state.

For full production-grade territory security, the platform should eventually:

- Include employee ID or user ID in JWT claims.
- Resolve employee territory from employee-service.
- Add service-level filters by assigned state/district/city.

For the immediate implementation, I will enforce:

- role-based route access in gateway
- hierarchy and territory consistency during employee creation/update
- territory search endpoints

### Auth User and Employee Link

The `auth-service` manages login users.

The `employee-service` manages workforce metadata.

The `authUserId` field links the two. This is required because auth users alone do not contain:

- parent manager
- assigned state
- assigned district
- assigned city
- employee status
- workforce reporting structure

### Existing Project Uses Mixed Security Patterns

The reference project relies heavily on gateway validation.

The EPS project currently has `@PreAuthorize` annotations only in auth service and most other controllers do not have controller-level security.

The most consistent next step is:

- enforce external access through gateway
- implement JWT validation and role checks at gateway
- keep service-level business validation in the service itself

### Existing Service Completion Claims Are Not Fully Accurate

Some docs call the architecture production-ready, but source review shows platform workforce functionality is incomplete.

This is why the implementation must add real APIs and validation logic rather than only adding role names.

## 13. Definition of Done

This platform-side hierarchy work should be considered complete when:

- Auth roles and employee roles are aligned.
- Employee records support reporting manager and territory assignment.
- Employee APIs support hierarchy management.
- Service logic validates reporting lines and territory consistency.
- Client onboarding records are linked to Sales POC.
- Sales POC can onboard and view assigned clients.
- Gateway exposes employee and onboarding routes.
- Gateway validates JWTs for protected APIs.
- Gateway enforces role-based access per route.
- Auth JWTs include role claims.
- Auth exposes a token validation endpoint.
- Project compiles for affected modules.
- Documentation clearly states implemented platform hierarchy support.

## 14. Summary

The current implementation has many operational modules and does include role names in auth, but the actual platform workforce hierarchy is only partially implemented.

The main missing piece is not just more roles. The missing piece is the management model:

- who reports to whom
- who is assigned to which territory
- who can manage which roles
- which workflows each role owns
- how gateway permissions protect those workflows
- how client onboarding is tied to SALES_POC ownership

The planned implementation will complete those pieces while preserving the current Spring Boot microservice structure and following the local `java-spring-microservices` gateway/auth reference pattern.
