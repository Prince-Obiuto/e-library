-- Create user_db database
CREATE DATABASE IF NOT EXISTS user_db;

-- Connect to user_db
\c user_db;

-- Create users table (will be auto-created by JPA, but this is for reference)
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20),
    department VARCHAR(100),
    matric_number VARCHAR(50) UNIQUE,
    staff_id VARCHAR(50) UNIQUE,
    graduation_year INTEGER,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    last_login_at TIMESTAMP,
    expiry_warning_email_sent_at TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_graduation_year ON users(graduation_year);

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE user_db TO username;