CREATE TABLE IF NOT EXISTS notifications (
  id UUID PRIMARY KEY,
  customer_id UUID NOT NULL,
  event_type VARCHAR(80) NOT NULL,
  message TEXT,
  email_body TEXT,
  sms_body TEXT,
  email_status VARCHAR(30),
  sms_status VARCHAR(30),
  sent_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP,
  tenant_id UUID
);

CREATE INDEX IF NOT EXISTS idx_notifications_customer_id ON notifications (customer_id);
CREATE INDEX IF NOT EXISTS idx_notifications_event_type ON notifications (event_type);
