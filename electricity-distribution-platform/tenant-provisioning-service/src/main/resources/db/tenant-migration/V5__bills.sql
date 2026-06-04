CREATE TABLE IF NOT EXISTS bills (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  connection_id BIGINT NOT NULL,
  meter_id BIGINT,
  bill_number VARCHAR(80) NOT NULL UNIQUE,
  bill_date DATE NOT NULL,
  due_date DATE NOT NULL,
  units_consumed NUMERIC(14, 2) NOT NULL,
  rate_per_unit NUMERIC(12, 2) NOT NULL,
  base_amount NUMERIC(14, 2) NOT NULL,
  taxes NUMERIC(14, 2) NOT NULL DEFAULT 0,
  penalties NUMERIC(14, 2) NOT NULL DEFAULT 0,
  discounts NUMERIC(14, 2) NOT NULL DEFAULT 0,
  total_amount NUMERIC(14, 2) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'UNPAID',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
