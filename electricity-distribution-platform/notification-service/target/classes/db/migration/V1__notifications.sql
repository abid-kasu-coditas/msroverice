CREATE TABLE IF NOT EXISTS notifications (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  event_type VARCHAR(80) NOT NULL,
  message TEXT,
  email_body TEXT,
  sms_body TEXT,
  email_status VARCHAR(30),
  sms_status VARCHAR(30),
  sent_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP,
  tenant_code VARCHAR(100)
);

CREATE INDEX IF NOT EXISTS idx_notifications_customer_id ON notifications (customer_id);
CREATE INDEX IF NOT EXISTS idx_notifications_event_type ON notifications (event_type);
