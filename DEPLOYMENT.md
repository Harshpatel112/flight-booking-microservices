# 🚀 Deployment Guide

This guide covers different deployment strategies for the Flight Booking Microservices system.

## 📋 Prerequisites

- Docker & Docker Compose
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- 4GB+ RAM recommended

## 🐳 Docker Deployment (Recommended)

### Quick Start
```bash
# 1. Clone the repository
git clone <repository-url>
cd flight-booking-microservices

# 2. Deploy with Docker
deploy-docker.bat

# 3. Check health
check-docker-health.bat

# 4. Test the system
test-system.bat
```

### Manual Docker Steps
```bash
# 1. Build all Maven projects
build-all.bat

# 2. Build Docker images
docker-compose build

# 3. Start all services
docker-compose up -d

# 4. Monitor logs
docker-compose logs -f

# 5. Stop services
docker-compose down
```

### Docker Services Overview
| Service | Container Name | Port | Health Check |
|---------|---------------|------|--------------|
| MySQL | flight-booking-mysql | 3306 | mysqladmin ping |
| Kafka | flight-booking-kafka | 9092 | kafka-broker-api-versions |
| Redis | flight-booking-redis | 6379 | redis-cli ping |
| Zipkin | flight-booking-zipkin | 9411 | HTTP /health |
| Prometheus | flight-booking-prometheus | 9090 | HTTP /-/healthy |
| Grafana | flight-booking-grafana | 3000 | HTTP /api/health |
| Service Registry | service-registry | 8761 | HTTP /actuator/health |
| User Service | user-service | 8082 | HTTP /api/v1/actuator/health |
| Flight Service | flight-service | 8081 | HTTP /api/v1/actuator/health |
| Booking Service | booking-service | 8083 | HTTP /api/v1/actuator/health |
| Payment Service | payment-service | 8087 | HTTP /api/v1/actuator/health |
| Notification Service | notification-service | 8084 | HTTP /api/v1/actuator/health |
| API Gateway | api-gateway | 8085 | HTTP /actuator/health |

## 🏠 Local Development Deployment

### Infrastructure Only
```bash
# Start only infrastructure services
docker-compose up -d mysql zookeeper kafka redis zipkin prometheus grafana

# Run services locally
cd ServiceRegistry && mvn spring-boot:run &
cd UserService && mvn spring-boot:run &
cd FlightService && mvn spring-boot:run &
cd BookingService && mvn spring-boot:run &
cd PaymentService && mvn spring-boot:run &
cd NotificationService && mvn spring-boot:run &
cd API-Gateway && mvn spring-boot:run &
```

### Database Setup
```sql
-- Connect to MySQL
mysql -h localhost -P 3306 -u root -p

-- Create databases
CREATE DATABASE IF NOT EXISTS Users_db;
CREATE DATABASE IF NOT EXISTS flight_db;
CREATE DATABASE IF NOT EXISTS flight_booking;
CREATE DATABASE IF NOT EXISTS payment_db;
CREATE DATABASE IF NOT EXISTS notification_db;

-- Verify databases
SHOW DATABASES;
```

## ☁️ Cloud Deployment

### AWS ECS Deployment
```bash
# 1. Build and push images to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# 2. Tag and push images
docker tag service-registry:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/service-registry:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/service-registry:latest

# 3. Deploy ECS services
aws ecs update-service --cluster flight-booking --service service-registry --force-new-deployment
```

### Kubernetes Deployment
```bash
# 1. Create namespace
kubectl create namespace flight-booking

# 2. Apply configurations
kubectl apply -f k8s/

# 3. Check deployments
kubectl get pods -n flight-booking

# 4. Expose services
kubectl port-forward svc/api-gateway 8085:8085 -n flight-booking
```

### Docker Swarm Deployment
```bash
# 1. Initialize swarm
docker swarm init

# 2. Deploy stack
docker stack deploy -c docker-compose.yml flight-booking

# 3. Check services
docker service ls

# 4. Scale services
docker service scale flight-booking_booking-service=3
```

## 🔧 Configuration Management

### Environment Variables
Create `.env` file:
```bash
# Database
DB_PASSWORD=your_strong_database_password
MYSQL_ROOT_PASSWORD=your_mysql_root_password

# JWT
JWT_SECRET=mySecretKey123456789012345678901234567890

# Razorpay
RAZORPAY_KEY_ID=your_key_id
RAZORPAY_KEY_SECRET=your_key_secret

# Email
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Kafka
KAFKA_SERVERS=localhost:9092

# Redis
REDIS_URL=redis://localhost:6379
```

### Production Configuration
```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  api-gateway:
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JAVA_OPTS=-Xmx512m -Xms256m
    deploy:
      replicas: 2
      resources:
        limits:
          memory: 512M
        reservations:
          memory: 256M
```

## 📊 Monitoring Setup

### Prometheus Configuration
```yaml
# monitoring/prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-boot-apps'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['user-service:8082', 'flight-service:8081', 'booking-service:8083']
```

### Grafana Dashboards
1. Access Grafana: http://localhost:3000 (admin/admin)
2. Add Prometheus data source: http://prometheus:9090
3. Import Spring Boot dashboard: ID 6756
4. Create custom dashboards for business metrics

## 🔐 Security Configuration

### SSL/TLS Setup
```bash
# Generate certificates
keytool -genkeypair -alias flight-booking -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650

# Update application.yml
server:
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
    key-alias: flight-booking
  port: 8443
```

### Secrets Management
```bash
# Using Docker secrets
echo "your_secure_password" | docker secret create db_password -
echo "your_jwt_secret_key" | docker secret create jwt_secret -

# Update docker-compose.yml
services:
  user-service:
    secrets:
      - db_password
      - jwt_secret
    environment:
      - DB_PASSWORD_FILE=/run/secrets/db_password
      - JWT_SECRET_FILE=/run/secrets/jwt_secret

secrets:
  db_password:
    external: true
  jwt_secret:
    external: true
```

## 🧪 Health Checks & Monitoring

### Service Health Endpoints
```bash
# Check all services
curl http://localhost:8761/actuator/health  # Service Registry
curl http://localhost:8085/actuator/health  # API Gateway
curl http://localhost:8082/api/v1/actuator/health  # User Service
curl http://localhost:8081/api/v1/actuator/health  # Flight Service
curl http://localhost:8083/api/v1/actuator/health  # Booking Service
curl http://localhost:8087/api/v1/actuator/health  # Payment Service
curl http://localhost:8084/api/v1/actuator/health  # Notification Service
```

### Automated Health Checks
```bash
#!/bin/bash
# health-check.sh
services=("8761" "8085" "8082" "8081" "8083" "8087" "8084")
for port in "${services[@]}"; do
  if curl -f http://localhost:$port/actuator/health > /dev/null 2>&1; then
    echo "✅ Service on port $port is healthy"
  else
    echo "❌ Service on port $port is unhealthy"
  fi
done
```

## 🚨 Troubleshooting

### Common Issues

#### Service Discovery Issues
```bash
# Check Eureka dashboard
curl http://localhost:8761/eureka/apps

# Restart service registry
docker-compose restart service-registry
```

#### Database Connection Issues
```bash
# Check MySQL logs
docker logs flight-booking-mysql

# Test connection
mysql -h localhost -P 3306 -u root -p -e "SHOW DATABASES;"
```

#### Kafka Issues
```bash
# Check Kafka logs
docker logs flight-booking-kafka

# List topics
docker exec flight-booking-kafka kafka-topics --list --bootstrap-server localhost:9092

# Check consumer groups
docker exec flight-booking-kafka kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

#### Memory Issues
```bash
# Check container memory usage
docker stats

# Increase memory limits in docker-compose.yml
services:
  booking-service:
    deploy:
      resources:
        limits:
          memory: 1G
```

### Log Analysis
```bash
# View service logs
docker-compose logs -f booking-service
docker-compose logs -f payment-service
docker-compose logs -f notification-service

# Search for errors
docker-compose logs | grep ERROR

# Follow specific service logs
docker logs -f booking-service
```

## 📈 Performance Tuning

### JVM Optimization
```bash
# Production JVM settings
JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication"
```

### Database Optimization
```sql
-- MySQL configuration
[mysqld]
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
max_connections = 200
query_cache_size = 64M
```

### Connection Pool Tuning
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

## 🔄 Backup & Recovery

### Database Backup
```bash
# Create backup
docker exec flight-booking-mysql mysqldump -u root -p --all-databases > backup.sql

# Restore backup
docker exec -i flight-booking-mysql mysql -u root -p < backup.sql
```

### Configuration Backup
```bash
# Backup configurations
tar -czf config-backup.tar.gz */src/main/resources/application*.yml docker-compose.yml
```

## 📞 Support

For deployment issues:
1. Check service logs
2. Verify configuration
3. Test connectivity between services
4. Check resource usage
5. Review monitoring dashboards

---

**Happy Deploying! 🚀**