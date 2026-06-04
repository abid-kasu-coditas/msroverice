#!/bin/bash
set -e

# Create databases
POSTGRES_DB="postgres"
POSTGRES_USER="postgres"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE auth_service_db;
    CREATE DATABASE customer_service_db;
    CREATE DATABASE connection_service_db;
    CREATE DATABASE meter_service_db;
    CREATE DATABASE billing_service_db;
    CREATE DATABASE payment_service_db;
    CREATE DATABASE complaint_service_db;
    CREATE DATABASE notification_service_db;
    CREATE DATABASE analytics_service_db;
    CREATE DATABASE audit_service_db;
    CREATE DATABASE electricity_distribution;
    CREATE DATABASE employee_service_db;
EOSQL

echo "All databases created successfully!"
