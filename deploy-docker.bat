@echo off
echo ================================
echo Docker Deployment Script
echo ================================

echo.
echo 1. Stopping existing containers...
docker-compose down

echo.
echo 2. Removing old images...
docker-compose down --rmi all --volumes --remove-orphans

echo.
echo 3. Building Maven projects...
call build-all.bat

echo.
echo 4. Building Docker images...
docker-compose build --no-cache

echo.
echo 5. Starting infrastructure services...
docker-compose up -d mysql zookeeper kafka redis zipkin prometheus grafana

echo Waiting for infrastructure to be ready...
timeout /t 60

echo.
echo 6. Starting Service Registry...
docker-compose up -d service-registry

echo Waiting for Service Registry...
timeout /t 30

echo.
echo 7. Starting core services...
docker-compose up -d user-service flight-service

echo Waiting for core services...
timeout /t 30

echo.
echo 8. Starting business services...
docker-compose up -d booking-service payment-service notification-service

echo Waiting for business services...
timeout /t 30

echo.
echo 9. Starting API Gateway...
docker-compose up -d api-gateway

echo.
echo ================================
echo Deployment Complete!
echo ================================
echo.
echo 📊 Service URLs:
echo - Service Registry: http://localhost:8761
echo - API Gateway: http://localhost:8085
echo - User Service: http://localhost:8082/swagger-ui.html
echo - Flight Service: http://localhost:8081/swagger-ui.html
echo - Booking Service: http://localhost:8083/swagger-ui.html
echo - Payment Service: http://localhost:8087/swagger-ui.html
echo - Notification Service: http://localhost:8084/swagger-ui.html
echo.
echo 📊 Monitoring URLs:
echo - Prometheus: http://localhost:9090
echo - Grafana: http://localhost:3000 (admin/admin)
echo - Zipkin: http://localhost:9411
echo.
echo 🧪 Testing:
echo - Run: test-system.bat
echo - Import Postman collection: Flight-Booking-API-Tests.postman_collection.json
echo.

pause