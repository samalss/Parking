DROP SCHEMA IF EXISTS parking_db;
CREATE SCHEMA IF NOT EXISTS parking_db;
USE parking_db;

DROP TABLE IF EXISTS car;
CREATE TABLE IF NOT EXISTS car (
    id INT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(8) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS parking_records;
CREATE TABLE IF NOT EXISTS parking_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME,
    cost DOUBLE,
    FOREIGN KEY (car_id) REFERENCES car(id)
);
