# Notification Service Guide

## Overview

The **Notification Service** is a **Kafka consumer service** that listens to events from other microservices and stores notifications for customers. It's responsible for tracking all customer-facing notifications and preparing them for delivery via Email or SMS.

---

## Purpose

The notification service serves as a **centralized notification hub** that:

1. **Listens to Events** - Consumes Kafka events from other services
2. **Stores Notifications** - Persists notification data in PostgreSQL
3. **Tracks Delivery** - Tracks email and SMS delivery status
4. **Provides Queryability** - REST API to retrieve notifications by various criteria
5. **Supports Retries** - Can retry failed email/SMS notifications

---

## Architecture

```
┌──────────────────────────────────────────────────────────┐
│         Other Microservices                              │
│  (customer-service, billing-service, payment-service)    │
└────────────────────┬─────────────────────────────────────┘
                     │
                     │ Kafka Events
                     ▼
┌──────────────────────────────────────────────────────────┐
│     Notification Service (Port 8094)                     │
│  ┌────────────────────────────────────────────────────┐  │
│  │  NotificationConsumer (Kafka Listener)            │  │
│  │  • bill-generated                                 │  │
│  │  • payment-received                               │  │
│  │  • customer-onboarded                             │  │
│  │  • tenant-provisioned                             │  │
│  │  • complaint-raised                               │  │
│  │  • complaint-resolved                             │  │
│  │  • complaint-escalated                            │  │
│  │  • tenant-suspended                               │  │
│  └────────────────────────────────────────────────────┘  │
│                     │                                     │
│                     ▼                                     │
│  ┌────────────────────────────────────────────────────┐  │
│  │  NotificationService                              │  │
│  │  • Create notifications                           │  │
│  │  • Send email notifications                       │  │
│  │  • Send SMS notifications                         │  │
│  │  • Query notifications                            │  │
│  │  • Retry failed notifications                     │  │
│  └────────────────────────────────────────────────────┘  │
│                     │                                     │
│                     ▼                                     │
│  ┌────────────────────────────────────────────────────┐  │
│  │  NotificationController (REST API)                │  │
│  │  • GET /api/notifications                         │  │
│  │  • GET /api/notifications/{id}                    │  │
│  │  • GET /api/notifications/customer/{customerId}   │  │
│  │  • POST /api/notifications/{id}/send-email        │  │
│  │  • POST /api/notifications/{id}/send-sms          │  │
│  └────────────────────────────────────────────────────┘  │
└────────────┬───────────────────────────────────────────────┘
             │
             ▼
       PostgreSQL Database
       (notifications table)
```

---

## What Does Notification Service Do?

### 1. Listens to Kafka Events

The service subscribes to multiple Kafka topics and reacts when events occur:

```java
@KafkaListener(topics = "bill-generated", groupId = "notification-service-group")
public void consumeBillGenerated(String message) {
    // Extract customer info
    // Create notification
    // Store in database
}
```

### 2. Stores Notifications

When an event is received, a notification record is created:

```
Notification Object:
├─ customerId: 123
├─ eventType: BILL_GENERATED
├─ message: "New bill generated: BILL-001"
├─ emailBody: "Your electricity bill is ready. Bill Amount: Rs. 1500"
├─ smsBody: "Your electricity bill is ready"
├─ emailStatus: PENDING (not sent yet)
├─ smsStatus: PENDING (not sent yet)
├─ tenantCode: "reliance_power"
└─ createdAt: 2026-06-04 10:30:00
```

### 3. Tracks Delivery Status

Each notification has two separate delivery channels:
- **Email Status**: PENDING → SENT → FAILED → RETRY
- **SMS Status**: PENDING → SENT → FAILED → RETRY

### 4. Provides Query APIs

REST endpoints to retrieve and manage notifications.

---

## Event Types Handled

The notification service listens to these events and stores notifications:

| Event Type | Source Service | What Triggers It |
|---|---|---|
| **BILL_GENERATED** | billing-service | New electricity bill created |
| **PAYMENT_SUCCESS** | payment-service | Payment received successfully |
| **PAYMENT_FAILED** | payment-service | Payment transaction failed |
| **CUSTOMER_REGISTERED** | customer-service | New customer on-boarded |
| **CONNECTION_ACTIVATED** | connection-service | Electricity connection activated |
| **CONNECTION_REJECTED** | connection-service | Connection request rejected |
| **COMPLAINT_REGISTERED** | complaint-service | New complaint raised by customer |
| **COMPLAINT_RESOLVED** | complaint-service | Complaint issue resolved |
| **SERVICE_BLOCKED** | platform-billing-service | Services blocked (payment overdue) |
| **SERVICE_UNBLOCKED** | platform-billing-service | Services unblocked (payment made) |
| **TENANT_PROVISIONED** | tenant-provisioning-service | New electricity provider on-boarded |
| **TENANT_SUSPENDED** | platform-service | Electricity provider suspended |

---

## Data Model

### Notification Entity

```java
@Entity
@Table(name = "notifications")
public class Notification {
    Long id;                          // Primary key
    Long customerId;                  // Which customer
    EventType eventType;              // What event (BILL_GENERATED, etc.)
    String message;                   // Summary message
    String emailBody;                 // Full email content
    String smsBody;                   // SMS content
    NotificationStatus emailStatus;   // Email delivery status
    NotificationStatus smsStatus;     // SMS delivery status
    LocalDateTime sentAt;             // When sent
    LocalDateTime createdAt;          // Created time
    LocalDateTime updatedAt;          // Last updated
    String tenantCode;                // Which electricity provider
}
```

### Enums

**EventType**:
```
BILL_GENERATED
PAYMENT_SUCCESS
PAYMENT_FAILED
COMPLAINT_REGISTERED
COMPLAINT_RESOLVED
CUSTOMER_REGISTERED
CONNECTION_ACTIVATED
CONNECTION_REJECTED
SERVICE_BLOCKED
SERVICE_UNBLOCKED
```

**NotificationStatus**:
```
PENDING   → Not sent yet
SENT      → Successfully sent
FAILED    → Failed to send
RETRY     → Waiting to retry
```

---

## REST API Endpoints

### Get All Notifications
```bash
GET /api/notifications?page=0&size=10
```

### Get Notification by ID
```bash
GET /api/notifications/{id}
```

### Get Notifications by Customer
```bash
GET /api/notifications/customer/{customerId}?page=0&size=10
```

### Get Notifications by Event Type
```bash
GET /api/notifications/event/BILL_GENERATED?page=0&size=10
```

### Get Notifications by Email Status
```bash
GET /api/notifications/status/email/PENDING?page=0&size=10
```

### Get Notifications by SMS Status
```bash
GET /api/notifications/status/sms/PENDING?page=0&size=10
```

### Send Email Notification
```bash
POST /api/notifications/{id}/send-email
```

Response:
```json
{
  "id": 123,
  "customerId": 456,
  "eventType": "BILL_GENERATED",
  "emailStatus": "SENT",
  "sentAt": "2026-06-04T10:35:00"
}
```

### Send SMS Notification
```bash
POST /api/notifications/{id}/send-sms
```

### Get Notification Metrics
```bash
GET /api/notifications/metrics/summary
```

Response:
```json
{
  "pendingEmails": 25,
  "pendingSms": 18
}
```

### Search by Date Range
```bash
GET /api/notifications/search?startDate=2026-06-01T00:00:00&endDate=2026-06-04T23:59:59
```

### Delete Notification
```bash
DELETE /api/notifications/{id}
```

---

## Example Workflows

### Workflow 1: Customer Receives Bill Notification

```
1. Billing Service generates bill
   ↓
2. Billing Service publishes "bill-generated" event to Kafka
   {
     "customerId": 123,
     "billId": "BILL-001",
     "totalAmount": "1500"
   }
   ↓
3. Notification Service consumer (NotificationConsumer) receives event
   ↓
4. Creates Notification record:
   {
     "customerId": 123,
     "eventType": "BILL_GENERATED",
     "emailBody": "Your electricity bill is ready. Bill Amount: Rs. 1500",
     "smsBody": "Your electricity bill is ready",
     "emailStatus": "PENDING",
     "smsStatus": "PENDING"
   }
   ↓
5. Saves to database
   ↓
6. API admin/system calls: POST /api/notifications/{id}/send-email
   ↓
7. Email sent (or simulated) and status updated to "SENT"
   ↓
8. Customer retrieves notifications: GET /api/notifications/customer/123
   ↓
9. Customer sees: "Your electricity bill is ready. Bill Amount: Rs. 1500"
```

### Workflow 2: Payment Success Notification

```
1. Payment Service receives payment
   ↓
2. Publishes "payment-received" event
   {
     "customerId": 123,
     "transactionId": "TXN-12345",
     "amount": "1500"
   }
   ↓
3. Notification Service consumer receives event
   ↓
4. Creates Notification:
   {
     "customerId": 123,
     "eventType": "PAYMENT_SUCCESS",
     "emailBody": "Your payment of Rs. 1500 has been received successfully",
     "smsBody": "Payment confirmed. Thank you!"
   }
   ↓
5. Saves to database
   ↓
6. System automatically sends via email and SMS
   ↓
7. Customer sees notification on their dashboard/app
```

### Workflow 3: Complaint Resolution Notification

```
1. Complaint Service resolves complaint
   ↓
2. Publishes "complaint-resolved" event
   ↓
3. Notification Service consumer receives
   ↓
4. Creates Notification (stored but not actively sent - logging purpose)
   ↓
5. System can query: GET /api/notifications/event/COMPLAINT_RESOLVED
   ↓
6. Operational team can see all resolved complaints
   ↓
7. Can manually trigger sending: POST /api/notifications/{id}/send-email
```

---

## Current Implementation Status

### ✅ Implemented
- Kafka consumer for multiple topics
- Notification storage in PostgreSQL
- Email status tracking
- SMS status tracking
- REST API endpoints
- Query by customer, event type, status
- Date range search
- Metrics endpoint

### ⚠️ Simulated (Not Real Implementation)
- **Email Sending**: Currently prints to console, doesn't send real emails
- **SMS Sending**: Currently prints to console, doesn't send real SMS
- **Retry Logic**: Exists but not automated

### 🔄 For Production, You Would Add
1. **Real Email Integration** (SendGrid, AWS SES, etc.)
2. **Real SMS Integration** (Twilio, AWS SNS, etc.)
3. **Scheduled Retry Job** (Spring @Scheduled to retry failed notifications)
4. **Email Templates** (HTML templates for different event types)
5. **Notification Preferences** (Allow customers to opt-in/out)
6. **Delivery Rate Limiting** (Don't spam customers)
7. **Webhook Notifications** (If customer uses third-party platforms)

---

## Service Configuration

**Port**: 8094
**Database**: PostgreSQL (public schema)
**Message Broker**: Kafka (localhost:9094)
**Consumer Group**: notification-service-group, notification-service-log-group

### Application Properties

```properties
spring.application.name=notification-service
server.port=8094

spring.datasource.url=jdbc:postgresql://localhost:5432/epsdb
spring.datasource.username=admin_user
spring.datasource.password=password

spring.kafka.bootstrap-servers=localhost:9094

spring.flyway.enabled=true
spring.flyway.schemas=public
spring.flyway.locations=classpath:db/migration
```

---

## Database Schema

```sql
CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    message TEXT,
    email_body TEXT,
    sms_body TEXT,
    email_status VARCHAR(20) DEFAULT 'PENDING',
    sms_status VARCHAR(20) DEFAULT 'PENDING',
    sent_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    tenant_code VARCHAR(50)
);

-- Indexes for fast queries
CREATE INDEX idx_customer_id ON notifications(customer_id);
CREATE INDEX idx_event_type ON notifications(event_type);
CREATE INDEX idx_email_status ON notifications(email_status);
CREATE INDEX idx_sms_status ON notifications(sms_status);
CREATE INDEX idx_created_at ON notifications(created_at);
```

---

## Kafka Topics Consumed

| Topic | Group | Purpose |
|-------|-------|---------|
| bill-generated | notification-service-group | Bill generated events |
| payment-received | notification-service-group | Payment success events |
| customer-onboarded | notification-service-group | Customer registration |
| tenant-provisioned | notification-service-log-group | Provider on-boarding |
| complaint-raised | notification-service-log-group | Complaint registration |
| complaint-resolved | notification-service-log-group | Complaint resolution |
| complaint-escalated | notification-service-log-group | Complaint escalation |
| tenant-suspended | notification-service-log-group | Provider suspension |

---

## Use Cases

### 1. Customer Receives Bill Notification
**Who**: Customer
**When**: New electricity bill generated
**How**: Email + SMS notification sent
**Content**: Bill amount, due date, payment link

### 2. Payment Confirmation Notification
**Who**: Customer
**When**: Payment successfully received
**How**: Email + SMS confirmation
**Content**: Amount paid, transaction ID, balance

### 3. Complaint Status Update
**Who**: Customer
**When**: Complaint resolved
**How**: Notification stored for tracking
**Content**: Issue resolved, technician remarks

### 4. Service Blocking Notification
**Who**: Customer
**When**: Services blocked due to non-payment
**How**: Alert notification
**Content**: "Services blocked. Pay pending amount to restore"

### 5. Service Unblocked Notification
**Who**: Customer
**When**: Payment received, service restored
**How**: Confirmation notification
**Content**: "Services restored. Thank you for payment"

### 6. Operational Notifications
**Who**: Admin/Operational team
**When**: New