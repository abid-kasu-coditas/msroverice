-- Seed users for testing
INSERT INTO users (id, username, email, password, role, active, account_non_expired, account_non_locked, credentials_non_expired)
VALUES 
  ('550e8400-e29b-41d4-a716-446655440001', 'superadmin', 'superadmin@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'SUPER_ADMIN', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440002', 'management', 'management@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'MANAGEMENT', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440003', 'sales_poc', 'sales@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'SALES_POC', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440004', 'state_head', 'statehead@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'STATE_HEAD', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440005', 'district_head', 'disthead@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'DISTRICT_HEAD', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440006', 'city_head', 'cityhead@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'CITY_HEAD', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440007', 'biller', 'biller@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'BILLER', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440008', 'technician', 'tech@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'TECHNICIAN', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440009', 'crm_user', 'crm@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'CRM', true, true, true, true);

-- Note: Password is 'password' encoded with BCrypt
