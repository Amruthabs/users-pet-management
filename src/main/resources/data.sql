-- Minimal data.sql for testing

-- Example: user table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
);

-- Insert a test user
INSERT INTO users (id, name, email) VALUES (1, 'Test User', 'test@example.com');
