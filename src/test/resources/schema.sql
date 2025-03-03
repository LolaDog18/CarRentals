-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email_verification_token VARCHAR(255),
    email_verification_status BOOLEAN NOT NULL DEFAULT FALSE,
    mobile_number VARCHAR(12) NOT NULL,
    age INT NOT NULL CHECK (age >= 21 AND age <= 65),
    driving_license VARCHAR(20) NOT NULL,
    address VARCHAR(100) NOT NULL
);

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    user_id CHAR(36) NOT NULL,
    roles VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);