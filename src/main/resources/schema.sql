-- Create the employees table
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,                -- Use BIGINT for id with auto-increment
    name VARCHAR(100) NOT NULL,                           -- Name can't be NULL
    email VARCHAR(150) UNIQUE NOT NULL,                   -- Email must be unique and can't be NULL
    department VARCHAR(100),                              -- Department is optional
    joining_date DATE NOT NULL,                           -- Joining date can't be NULL
    leave_balance INT DEFAULT 30,                         -- Default leave balance is 30
    version INT DEFAULT 0,                                -- Version field with default value of 0
    CONSTRAINT uq_email UNIQUE(email)                    -- Ensure email uniqueness
);

-- Create the leave_requests table
CREATE TABLE IF NOT EXISTS leave_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,                -- Use BIGINT for id with auto-increment
    employee_id BIGINT NOT NULL,                          -- Employee reference can't be NULL
    start_date DATE NOT NULL,                             -- Start date must be provided
    end_date DATE NOT NULL,                               -- End date must be provided
    status VARCHAR(20) DEFAULT 'PENDING',                -- Status field with a default of 'PENDING'
    days INT NOT NULL,                                    -- Days field must be provided
    reason TEXT,                                          -- Reason is optional
    CONSTRAINT fk_employee FOREIGN KEY (employee_id)     -- Foreign key to employees table
        REFERENCES employees(id) ON DELETE CASCADE       -- Cascade delete to remove leave requests when employee is deleted
);

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,                -- Use BIGINT for id with auto-increment
    username VARCHAR(255) NOT NULL UNIQUE,                -- Username can't be NULL and must be unique (can also be email)
    password VARCHAR(255) NOT NULL,                       -- Password can't be NULL
    role VARCHAR(50),                                     -- Role is optional (if you want to implement roles like ADMIN, USER, etc.)
    employee_id BIGINT NOT NULL,                          -- Employee reference can't be NULL
    CONSTRAINT fk_employee FOREIGN KEY (employee_id)     -- Foreign key to employees table
        REFERENCES employees(id),                         -- Reference employee id from employees table
    CONSTRAINT uq_username UNIQUE (username)             -- Ensure username is unique
);

--CREATE TABLE IF NOT EXISTS employees (
--    id SERIAL PRIMARY KEY,
--    name VARCHAR(100) NOT NULL,
--    email VARCHAR(150) UNIQUE NOT NULL,
--    department VARCHAR(100),
--    joining_date DATE NOT NULL,
--    leave_balance INT DEFAULT 30,
--    version INT DEFAULT 0
--);
--
--CREATE TABLE IF NOT EXISTS leave_requests (
--    id SERIAL PRIMARY KEY,
--    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
--    start_date DATE NOT NULL,
--    end_date DATE NOT NULL,
--    status VARCHAR(20) DEFAULT 'PENDING',
--    days INT NOT NULL,
--    reason TEXT
--);
--
--CREATE TABLE IF NOT EXISTS USERS (
--    id BIGINT NOT NULL AUTO_INCREMENT,
--    username VARCHAR(255) NOT NULL UNIQUE,
--    password VARCHAR(255) NOT NULL,
--    role VARCHAR(50),
--    employee_id BIGINT NOT NULL,
--    PRIMARY KEY (id),
--    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
--);