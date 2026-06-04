CREATE TABLE IF NOT EXISTS meter_readings (
  id BIGSERIAL PRIMARY KEY,
  connection_id BIGINT NOT NULL,
  biller_id BIGINT,
  reading_value NUMERIC(14, 2) NOT NULL,
  previous_reading_value NUMERIC(14, 2),
  read_at TIMESTAMP NOT NULL,
  recorded_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS meter_photos (
  id BIGSERIAL PRIMARY KEY,
  meter_reading_id BIGINT NOT NULL,
  photo_url VARCHAR(1000) NOT NULL,
  uploaded_at TIMESTAMP NOT NULL DEFAULT NOW()
);
