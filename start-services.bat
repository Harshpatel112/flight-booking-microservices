@echo off
echo Starting Flight Booking Microservices...

echo.
echo ================================
echo Starting Infrastructure Services
echo ================================

echo Starting MySQL, Kafka, Redis, Zipkin, Prometheus, Grafana...
docker-compose up -d mysql zookeeper kafka redis zipkin prometheus grafana

echo Waiting for infrastructure services to be ready...
timeout /t 30

echo.
echo ================================
echo Starting Service Registry
echo ================================
docker-compose up -d service-registry

echo Waiting for Service Registry to be ready...
timeout /t 20

echo.
echo ================================
echo Starting Core Services
echo ================================
docker-compose up -d user-service flight-service

echo Waiting for core services to be ready...
timeout /t 15

echo.
echo ================================
echo Starting Business Services
echo ================================
docker-compose up -d booking-service payment-service notification-service

echo Waiting for business services to be ready...
timeout /t 15

echo.
echo ================================
echo Starting API Gateway
echo ================================
docker-compose up -d api-gateway

echo.
echo ================================
echo All Services Started Successfully!
echo ================================
echo.
echo Service URLs:
echo - Service Registry: http://localhost:8761
echo - API Gateway: http://localhost:8085
echo - User Service: http://localhost:8082/api/v1
echo - Flight Service: http://localhost:8081/api/v1
echo - Booking Service: http://localhost:8083/api/v1
echo - Payment Service: http://localhost:8087/api/v1
echo - Notification Service: http://localhost:8084/api/v1
echo.
echo Monitoring URLs:
echo - Prometheus: http://localhost:9090
echo - Grafana: http://localhost:3000 (admin/admin)
echo - Zipkin: http://localhost:9411
echo.
echo Database:
echo - MySQL: localhost:3306 (root/H@rsh123)
echo.
echo Message Queue:
echo - Kafka: localhost:9092
echo.
echo Cache:
echo - Redis: localhost:6379
echo.

pause