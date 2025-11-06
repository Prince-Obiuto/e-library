-- Connect to user_db
\c user_db;

-- Insert default admin user
INSERT INTO users (
    id, email, first_name, last_name, role, status, account_type,
    department, staff_id, email_verified, account_non_expired, account_non_locked,
    created_at, updated_at
) VALUES (
    gen_random_uuid()::text,
    'admin@futo.edu.ng',
    'System',
    'Administrator',
    'ADMIN_STAFF',
    'ACTIVE',
    'ADMIN',
    'IT Department',
    'ADMIN001',
    TRUE,
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (email) DO NOTHING;

-- Insert sample academic staff
INSERT INTO users (
    id, email, first_name, last_name, role, status, account_type,
    department, staff_id, email_verified, account_non_expired, account_non_locked,
    created_at, updated_at
) VALUES (
    gen_random_uuid()::text,
    'dr.john@futo.edu.ng',
    'John',
    'Doe',
    'ACADEMIC_STAFF',
    'ACTIVE',
    'STAFF',
    'Computer Science',
    '6521',
    TRUE,
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (email) DO NOTHING;

-- Insert sample student
INSERT INTO users (
    id, email, first_name, last_name, role, status, account_type,
    department, matric_number, graduation_year, email_verified, account_non_expired, account_non_locked,
    created_at, updated_at
) VALUES (
    gen_random_uuid()::text,
    'student@futo.edu.ng',
    'Jane',
    'Smith',
    'STUDENT',
    'ACTIVE',
    'STUDENT',
    'Information Technology',
    '20211200001',
    2027,
    TRUE,
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (email) DO NOTHING;