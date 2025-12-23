@echo off
echo ================================
echo Docker Health Check
echo ================================

echo.
echo Checking container status...
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo.
echo ================================
echo Health Check Results:
echo ================================

echo.
echo Infrastructure Services:
echo ------------------------

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' flight-booking-mysql 2>nul
if %errorlevel% neq 0 echo flight-booking-mysql: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' flight-booking-kafka 2>nul
if %errorlevel% neq 0 echo flight-booking-kafka: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' flight-booking-redis 2>nul
if %errorlevel% neq 0 echo flight-booking-redis: NOT RUNNING

echo.
echo Microservices:
echo --------------

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' service-registry 2>nul
if %errorlevel% neq 0 echo service-registry: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' api-gateway 2>nul
if %errorlevel% neq 0 echo api-gateway: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' user-service 2>nul
if %errorlevel% neq 0 echo user-service: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' flight-service 2>nul
if %errorlevel% neq 0 echo flight-service: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' booking-service 2>nul
if %errorlevel% neq 0 echo booking-service: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' payment-service 2>nul
if %errorlevel% neq 0 echo payment-service: NOT RUNNING

docker inspect --format='{{.Name}}: {{.State.Health.Status}}' notification-service 2>nul
if %errorlevel% neq 0 echo notification-service: NOT RUNNING

echo.
echo ================================
echo Service Endpoints Test:
echo ================================

echo Testing Service Registry...
curl -s http://localhost:8761 > nul && echo ✅ Service Registry: ACCESSIBLE || echo ❌ Service Registry: NOT ACCESSIBLE

echo Testing API Gateway...
curl -s http://localhost:8085/actuator/health > nul && echo ✅ API Gateway: ACCESSIBLE || echo ❌ API Gateway: NOT ACCESSIBLE

echo Testing User Service...
curl -s http://localhost:8082/api/v1/actuator/health > nul && echo ✅ User Service: ACCESSIBLE || echo ❌ User Service: NOT ACCESSIBLE

echo Testing Flight Service...
curl -s http://localhost:8081/api/v1/actuator/health > nul && echo ✅ Flight Service: ACCESSIBLE || echo ❌ Flight Service: NOT ACCESSIBLE

echo Testing Booking Service...
curl -s http://localhost:8083/api/v1/actuator/health > nul && echo ✅ Booking Service: ACCESSIBLE || echo ❌ Booking Service: NOT ACCESSIBLE

echo Testing Payment Service...
curl -s http://localhost:8087/api/v1/actuator/health > nul && echo ✅ Payment Service: ACCESSIBLE || echo ❌ Payment Service: NOT ACCESSIBLE

echo Testing Notification Service...
curl -s http://localhost:8084/api/v1/actuator/health > nul && echo ✅ Notification Service: ACCESSIBLE || echo ❌ Notification Service: NOT ACCESSIBLE

echo.
echo ================================
echo Monitoring Services:
echo ================================

echo Testing Prometheus...
curl -s http://localhost:9090 > nul && echo ✅ Prometheus: ACCESSIBLE || echo ❌ Prometheus: NOT ACCESSIBLE

echo Testing Grafana...
curl -s http://localhost:3000 > nul && echo ✅ Grafana: ACCESSIBLE || echo ❌ Grafana: NOT ACCESSIBLE

echo Testing Zipkin...
curl -s http://localhost:9411 > nul && echo ✅ Zipkin: ACCESSIBLE || echo ❌ Zipkin: NOT ACCESSIBLE

echo.
echo ================================
echo Health Check Complete!
echo ================================

pause