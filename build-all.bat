@echo off
echo ================================
echo Building All Microservices
echo ================================

echo.
echo 1. Building ServiceRegistry...
cd ServiceRegistry
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ ServiceRegistry build failed
    exit /b 1
)
echo ✅ ServiceRegistry build successful
cd ..

echo.
echo 2. Building API-Gateway...
cd API-Gateway
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ API-Gateway build failed
    exit /b 1
)
echo ✅ API-Gateway build successful
cd ..

echo.
echo 3. Building UserService...
cd UserService
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ UserService build failed
    exit /b 1
)
echo ✅ UserService build successful
cd ..

echo.
echo 4. Building FlightService...
cd FlightService
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ FlightService build failed
    exit /b 1
)
echo ✅ FlightService build successful
cd ..

echo.
echo 5. Building BookingService...
cd BookingService
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ BookingService build failed
    exit /b 1
)
echo ✅ BookingService build successful
cd ..

echo.
echo 6. Building PaymentService...
cd PaymentService
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ PaymentService build failed
    exit /b 1
)
echo ✅ PaymentService build successful
cd ..

echo.
echo 7. Building NotificationService...
cd NotificationService
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ NotificationService build failed
    exit /b 1
)
echo ✅ NotificationService build successful
cd ..

echo.
echo ================================
echo All Services Built Successfully!
echo ================================
echo.
echo Next steps:
echo 1. Run: docker-compose build
echo 2. Run: docker-compose up -d
echo.

pause