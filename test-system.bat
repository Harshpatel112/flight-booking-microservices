@echo off
echo ================================
echo Flight Booking System - Quick Test
echo ================================

echo.
echo 1. Checking Service Health...
echo ================================

echo Checking Service Registry...
curl -s http://localhost:8761 > nul
if %errorlevel% == 0 (
    echo ✅ Service Registry: RUNNING
) else (
    echo ❌ Service Registry: DOWN
)

echo Checking API Gateway...
curl -s http://localhost:8085/actuator/health > nul
if %errorlevel% == 0 (
    echo ✅ API Gateway: RUNNING
) else (
    echo ❌ API Gateway: DOWN
)

echo Checking User Service...
curl -s http://localhost:8082/api/v1/actuator/health > nul
if %errorlevel% == 0 (
    echo ✅ User Service: RUNNING
) else (
    echo ❌ User Service: DOWN
)

echo Checking Flight Service...
curl -s http://localhost:8081/api/v1/actuator/health > nul
if %errorlevel% == 0 (
    echo ✅ Flight Service: RUNNING
) else (
    echo ❌ Flight Service: DOWN
)

echo Checking Booking Service...
curl -s http://localhost:8083/api/v1/actuator/health > nul
if %errorlevel% == 0 (
    echo ✅ Booking Service: RUNNING
) else (
    echo ❌ Booking Service: DOWN
)

echo Checking Payment Service...
curl -s http://localhost:8087/api/v1/actuator/health > nul
if %errorlevel% == 0 (
    echo ✅ Payment Service: RUNNING
) else (
    echo ❌ Payment Service: DOWN
)

echo Checking Notification Service...
curl -s http://localhost:8084/api/v1/actuator/health > nul
if %errorlevel% == 0 (
    echo ✅ Notification Service: RUNNING
) else (
    echo ❌ Notification Service: DOWN
)

echo.
echo 2. Testing User Registration...
echo ================================

curl -X POST http://localhost:8085/api/v1/user/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser123\",\"email\":\"test@example.com\",\"password\":\"Test@123456\",\"confirmPassword\":\"Test@123456\",\"title\":\"Mr\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"phoneNumber\":\"+919876543210\",\"gender\":\"MALE\",\"dateOfBirth\":\"1990-01-01\",\"nationality\":\"Indian\",\"acceptTermsAndConditions\":true,\"acceptPrivacyPolicy\":true}"

echo.
echo 3. Testing User Login...
echo ================================

curl -X POST http://localhost:8085/api/v1/user/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"testuser123\",\"password\":\"Test@123456\"}"

echo.
echo 4. Testing Flight Search...
echo ================================

curl -X GET "http://localhost:8085/api/v1/flights/search?origin=Delhi&destination=Mumbai&departureDate=2024-12-25&adults=1&travelClass=ECONOMY"

echo.
echo ================================
echo Testing Complete!
echo ================================
echo.
echo 📊 Access Points:
echo - Service Registry: http://localhost:8761
echo - API Gateway: http://localhost:8085
echo - Swagger UI: http://localhost:8082/swagger-ui.html
echo - Prometheus: http://localhost:9090
echo - Grafana: http://localhost:3000
echo - Zipkin: http://localhost:9411
echo.

pause