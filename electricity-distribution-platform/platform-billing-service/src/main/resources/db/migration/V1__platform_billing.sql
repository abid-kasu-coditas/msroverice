CREATE TABLE IF NOT EXISTS platform_invoices (
  id BIGSERIAL PRIMARY KEY,
  tenant_code VARCHAR(100) NOT NULL,
  invoice_month VARCHAR(7) NOT NULL,
  amount NUMERIC(14, 2) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'UNPAID',
  due_date DATE NOT NULL,
  paid_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE (tenant_code, invoice_month)
);

CREATE INDEX IF NOT EXISTS idx_platform_invoices_status ON platform_invoices (status);
