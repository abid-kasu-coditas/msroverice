CREATE TABLE IF NOT EXISTS meter_accounts (
  id BIGSERIAL PRIMARY KEY,
  connection_id BIGINT NOT NULL,
  meter_serial_number VARCHAR(100) NOT NULL UNIQUE,
  meter_type VARCHAR(100) NOT NULL,
  installation_date DATE NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

ALTER TABLE meter_readings ADD COLUMN IF NOT EXISTS meter_account_id BIGINT;
ALTER TABLE meter_readings ADD COLUMN IF NOT EXISTS current_reading DOUBLE PRECISION;
ALTER TABLE meter_readings ADD COLUMN IF NOT EXISTS previous_reading DOUBLE PRECISION;
ALTER TABLE meter_readings ADD COLUMN IF NOT EXISTS reading_date DATE;
ALTER TABLE meter_readings ADD COLUMN IF NOT EXISTS units_consumed DOUBLE PRECISION;
