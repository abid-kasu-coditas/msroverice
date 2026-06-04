INSERT INTO meter_types (code, name, rate_per_unit)
VALUES
  ('DOMESTIC', 'Domestic', 6.50),
  ('COMMERCIAL', 'Commercial', 10.00),
  ('INDUSTRIAL', 'Industrial', 12.50)
ON CONFLICT (code) DO NOTHING;
