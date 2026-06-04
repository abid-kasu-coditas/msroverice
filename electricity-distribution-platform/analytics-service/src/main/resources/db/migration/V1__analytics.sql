CREATE TABLE IF NOT EXISTS analytics_metrics (
  id UUID PRIMARY KEY,
  metric_type VARCHAR(80) NOT NULL,
  customer_id UUID,
  connection_id UUID,
  bill_id UUID,
  payment_id UUID,
  metric_value DOUBLE PRECISION,
  metric_unit VARCHAR(50),
  description TEXT,
  recorded_at TIMESTAMP NOT NULL DEFAULT NOW(),
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  tenant_id UUID
);

CREATE TABLE IF NOT EXISTS tenant_stats (
  id BIGSERIAL PRIMARY KEY,
  tenant_code VARCHAR(100) NOT NULL,
  event_type VARCHAR(100) NOT NULL,
  event_count INT NOT NULL DEFAULT 1,
  recorded_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_tenant_stats_tenant ON tenant_stats (tenant_code);
CREATE INDEX IF NOT EXISTS idx_tenant_stats_event ON tenant_stats (event_type);
