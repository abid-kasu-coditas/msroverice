CREATE OR REPLACE VIEW tenant_bill_summary AS
SELECT status, COUNT(*) AS bill_count, COALESCE(SUM(total_amount), 0) AS amount
FROM bills
GROUP BY status;

CREATE OR REPLACE VIEW tenant_complaint_summary AS
SELECT status, COUNT(*) AS complaint_count
FROM complaints
GROUP BY status;
