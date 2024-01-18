DROP TABLE employee IF EXISTS;
DROP TABLE transaction IF EXISTS;
DROP TABLE employee_transaction IF EXISTS;
DROP TABLE employee_transaction_join IF EXISTS;

CREATE TABLE employee (
    id INTEGER IDENTITY PRIMARY KEY,
    employee_id     VARCHAR(40) NOT NULL,
    first_name      VARCHAR(40),
    last_name       VARCHAR(64),
    employee_number VARCHAR(40) NOT NULL
);

CREATE TABLE transaction (
    id INTEGER IDENTITY PRIMARY KEY,
    transaction_id VARCHAR(36),
    timestamp VARCHAR(20),
    vendor VARCHAR(64),
    amount VARCHAR(12)
);

CREATE TABLE employee_transaction (
    id INTEGER IDENTITY PRIMARY KEY,
    employee_id VARCHAR(36),
    transaction_id VARCHAR(36)
);

CREATE TABLE employee_transaction_join (
    id INTEGER IDENTITY PRIMARY KEY,
    employee_id VARCHAR(36),
    first_name VARCHAR(32),
    last_name VARCHAR(64),
    employee_number VARCHAR(32),
    transaction_id VARCHAR(36),
    timestamp VARCHAR(20),
    vendor VARCHAR(64),
    amount VARCHAR(12)
);

