CREATE INDEX IF NOT EXISTS idx_customers_account_number ON customers (account_number);
CREATE INDEX IF NOT EXISTS idx_connections_customer_id ON customer_connections (customer_id);
CREATE INDEX IF NOT EXISTS idx_readings_connection_id ON meter_readings (connection_id);
CREATE INDEX IF NOT EXISTS idx_bills_customer_id ON bills (customer_id);
CREATE INDEX IF NOT EXISTS idx_bills_status ON bills (status);
CREATE INDEX IF NOT EXISTS idx_payments_bill_id ON payments (bill_id);
CREATE INDEX IF NOT EXISTS idx_complaints_status ON complaints (status);
