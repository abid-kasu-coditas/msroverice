CREATE TABLE IF NOT EXISTS tenant_audit_log (
  id BIGSERIAL PRIMARY KEY,
  actor_user_id BIGINT,
  action VARCHAR(100) NOT NULL,
  entity_type VARCHAR(100) NOT NULL,
  entity_id BIGINT,
  metadata TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
