CREATE TABLE IF NOT EXISTS payment_blocks (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  bill_id BIGINT,
  is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
  block_reason TEXT,
  blocked_at TIMESTAMP,
  unblocked_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP,
  tenant_id VARCHAR(100)
);

CREATE INDEX IF NOT EXISTS idx_payment_blocks_customer_active
  ON payment_blocks (customer_id, is_blocked);
