-- Password is 'password' encoded with BCrypt.
INSERT INTO users (id, username, email, password_hash, role, user_type, tenant_id, active,
                   account_non_expired, account_non_locked, credentials_non_expired)
VALUES
  ('550e8400-e29b-41d4-a716-446655440001', 'superadmin', 'superadmin@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'SUPER_ADMIN', 'PLATFORM', null, true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440002', 'management', 'management@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'MANAGEMENT', 'PLATFORM', null, true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440003', 'sales_poc', 'salespoc@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'SALES_POC', 'PLATFORM', null, true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440004', 'state_head', 'statehead@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'STATE_HEAD', 'PLATFORM', null, true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440005', 'district_head', 'districthead@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'DISTRICT_HEAD', 'PLATFORM', null, true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440006', 'city_head', 'cityhead@eps.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'CITY_HEAD', 'PLATFORM', null, true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440007', 'operations_reliance', 'operations@reliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'OPERATIONS', 'TENANT', 'reliance', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440008', 'bpo_reliance', 'bpo@reliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'BPO_EMPLOYEE', 'TENANT', 'reliance', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440009', 'mgrl1_reliance', 'mgrl1@reliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'BPO_MANAGER_L1', 'TENANT', 'reliance', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440010', 'mgrl2_reliance', 'mgrl2@reliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'BPO_MANAGER_L2', 'TENANT', 'reliance', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440011', 'biller_reliance', 'biller@reliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'BILLER', 'TENANT', 'reliance', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440012', 'technician_reliance', 'technician@reliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'TECHNICIAN', 'TENANT', 'reliance', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440013', 'crm_reliance', 'crm@reliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'CRM', 'TENANT', 'reliance', true, true, true, true),
  ('550e8400-e29b-41d4-a716-446655440014', 'customer_reliance', 'customer@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVi', 'CUSTOMER', 'CUSTOMER', 'reliance', true, true, true, true)
ON CONFLICT (username) DO NOTHING;
