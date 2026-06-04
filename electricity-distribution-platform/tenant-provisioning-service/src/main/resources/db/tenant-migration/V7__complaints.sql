CREATE TABLE IF NOT EXISTS complaints (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  category VARCHAR(60) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  city_id BIGINT,
  area_id BIGINT,
  bpo_employee_id BIGINT,
  technician_id BIGINT,
  status VARCHAR(40) NOT NULL DEFAULT 'OPEN',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  assigned_at TIMESTAMP,
  resolved_at TIMESTAMP,
  resolution VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS complaint_escalations (
  id BIGSERIAL PRIMARY KEY,
  complaint_id BIGINT NOT NULL,
  level VARCHAR(20) NOT NULL,
  manager_id BIGINT,
  reason VARCHAR(1000),
  escalated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
