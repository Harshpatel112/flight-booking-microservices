-- Create databases for all microservices
CREATE DATABASE IF NOT EXISTS Users_db;
CREATE DATABASE IF NOT EXISTS flight_db;
CREATE DATABASE IF NOT EXISTS flight_booking;
CREATE DATABASE IF NOT EXISTS payment_db;
CREATE DATABASE IF NOT EXISTS notification_db;

-- Grant privileges
GRANT ALL PRIVILEGES ON Users_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON flight_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON flight_booking.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON payment_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON notification_db.* TO 'root'@'%';

FLUSH PRIVILEGES;