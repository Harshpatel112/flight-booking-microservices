# 🚀 Flight Booking System - Complete Testing Guide

## � QDocker Deployment (Recommended)

### Quick Start with Docker
```bash
# 1. Build all services and deploy with Docker
deploy-docker.bat

# 2. Check health of all containers
check-docker-health.bat

# 3. Test the system
test-system.bat
```

### Manual Docker Steps
```bash
# 1. Build Maven projects
build-all.bat

# 2. Build Docker images
docker-compose build

# 3. Start all services
docker-compose up -d

# 4. Check container health
docker ps
```

## 🔧 Local Development (Alternative)

### Step 1: Start Infrastructure
```bash
# Start only infrastructure services
docker-compose up -d mysql zookeeper kafka redis zipkin prometheus grafana
```

### Step 2: Start Services Locally
```bash
# Start each service in separate terminals
cd ServiceRegistry && mvn spring-boot:run
cd UserService && mvn spring-boot:run
cd FlightService && mvn spring-boot:run
cd BookingService && mvn spring-boot:run
cd PaymentService && mvn spring-boot:run
cd NotificationService && mvn spring-boot:run
cd API-Gateway && mvn spring-boot:run
```

## 📊 Monitoring URLs
- **Service Registry**: http://localhost:8761
- **API Gateway**: http://localhost:8085
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)
- **Zipkin**: http://localhost:9411

## 📚 API Documentation (Swagger)
- **User Service**: http://localhost:8082/swagger-ui.html
- **Flight Service**: http://localhost:8081/swagger-ui.html
- **Booking Service**: http://localhost:8083/swagger-ui.html
- **Payment Service**: http://localhost:8087/swagger-ui.html
- **Notification Service**: http://localhost:8084/swagger-ui.html

---

## 🔧 Testing Methods

### Method 1: Swagger UI Testing (Recommended for Beginners)
### Method 2: Postman Collection Testing
### Method 3: cURL Commands Testing
### Method 4: Automated Testing Scripts

---

## 🎯 Method 1: Swagger UI Testing

### 1.1 User Service Testing
**URL**: http://localhost:8082/swagger-ui.html

**Test Flow**:
1. **Register User** → POST `/api/v1/user/register`
2. **Login User** → POST `/api/v1/user/login`
3. **Get Profile** → GET `/api/v1/user/profile`

### 1.2 Flight Service Testing
**URL**: http://localhost:8081/swagger-ui.html

**Test Flow**:
1. **Search Flights** → GET `/api/v1/flights/search`
2. **Get Flight Details** → GET `/api/v1/flights/flight/{flightNumber}`
3. **Get Seat Map** → GET `/api/v1/flights/flight/{flightNumber}/seatmap`

### 1.3 Booking Service Testing
**URL**: http://localhost:8083/swagger-ui.html

**Test Flow**:
1. **Create Booking** → POST `/api/v1/bookings/create`
2. **Get Booking Details** → GET `/api/v1/bookings/{bookingId}`
3. **Confirm Booking** → PUT `/api/v1/bookings/{bookingId}/confirm`

---

## 🔥 Method 2: Complete API Testing with cURL

### 2.1 User Registration & Authentication

#### Register New User
```bash
curl -X POST http://localhost:8085/api/v1/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser123",
    "email": "test@example.com",
    "password": "Test@123456",
    "confirmPassword": "Test@123456",
    "title": "Mr",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+919876543210",
    "gender": "MALE",
    "dateOfBirth": "1990-01-01",
    "nationality": "Indian",
    "acceptTermsAndConditions": true,
    "acceptPrivacyPolicy": true
  }'
```

#### Login User
```bash
curl -X POST http://localhost:8085/api/v1/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser123",
    "password": "Test@123456"
  }'
```

**Save the JWT token from response for next requests!**

### 2.2 Flight Search & Details

#### Search Flights
```bash
curl -X GET "http://localhost:8085/api/v1/flights/search?origin=Delhi&destination=Mumbai&departureDate=2024-12-25&adults=2&children=1&travelClass=ECONOMY" \
  -H "Accept: application/json"
```

#### Get Flight Details
```bash
curl -X GET http://localhost:8085/api/v1/flights/flight/AI101 \
  -H "Accept: application/json"
```

#### Get Seat Map
```bash
curl -X GET http://localhost:8085/api/v1/flights/flight/AI101/seatmap \
  -H "Accept: application/json"
```

### 2.3 Booking Creation (Protected - Requires JWT)

#### Create Booking
```bash
curl -X POST http://localhost:8085/api/v1/bookings/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "flightNumber": "AI101",
    "travelClass": "ECONOMY",
    "departureDate": "2024-12-25",
    "passengers": [
      {
        "passengerType": "ADULT",
        "title": "Mr",
        "firstName": "John",
        "lastName": "Doe",
        "dateOfBirth": "1990-01-01",
        "gender": "MALE",
        "nationality": "Indian"
      }
    ],
    "contactEmail": "test@example.com",
    "contactPhone": "+919876543210",
    "totalAmount": 5000.00,
    "currency": "INR"
  }'
```

### 2.4 Payment Processing (Protected - Requires JWT)

#### Process Payment
```bash
curl -X POST http://localhost:8085/api/v1/payments/process \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "bookingId": "BOOKING_ID_FROM_PREVIOUS_STEP",
    "userId": 1,
    "amount": 5000.00,
    "currency": "INR",
    "paymentMethod": "CARD",
    "customerName": "John Doe",
    "customerEmail": "test@example.com",
    "customerPhone": "+919876543210"
  }'
```

---

## 🎯 Method 3: Postman Collection Testing

### Import Postman Collection
Create a new Postman collection with these requests:

#### Environment Variables
```json
{
  "gateway_url": "http://localhost:8085",
  "jwt_token": "{{jwt_token}}",
  "booking_id": "{{booking_id}}",
  "payment_id": "{{payment_id}}"
}
```

#### Collection Structure
```
Flight Booking API Tests/
├── 1. User Management/
│   ├── Register User
│   ├── Login User
│   └── Get Profile
├── 2. Flight Management/
│   ├── Search Flights
│   ├── Get Flight Details
│   └── Get Seat Map
├── 3. Booking Management/
│   ├── Create Booking
│   ├── Get Booking Details
│   └── Confirm Booking
└── 4. Payment Management/
    ├── Process Payment
    ├── Get Payment Details
    └── Verify Payment
```

---

## 🔄 Method 4: End-to-End Testing Flow

### Complete User Journey Test

#### Step 1: User Registration
```bash
# Register user and save response
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8085/api/v1/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser123",
    "email": "test@example.com",
    "password": "Test@123456",
    "confirmPassword": "Test@123456",
    "title": "Mr",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+919876543210",
    "gender": "MALE",
    "dateOfBirth": "1990-01-01",
    "nationality": "Indian",
    "acceptTermsAndConditions": true,
    "acceptPrivacyPolicy": true
  }')

echo "Registration Response: $REGISTER_RESPONSE"
```

#### Step 2: User Login & Get JWT
```bash
# Login and extract JWT token
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8085/api/v1/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser123",
    "password": "Test@123456"
  }')

JWT_TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.accessToken')
echo "JWT Token: $JWT_TOKEN"
```

#### Step 3: Search Flights
```bash
# Search flights
FLIGHTS_RESPONSE=$(curl -s -X GET "http://localhost:8085/api/v1/flights/search?origin=Delhi&destination=Mumbai&departureDate=2024-12-25&adults=1&travelClass=ECONOMY")

echo "Available Flights: $FLIGHTS_RESPONSE"
```

#### Step 4: Create Booking
```bash
# Create booking with JWT token
BOOKING_RESPONSE=$(curl -s -X POST http://localhost:8085/api/v1/bookings/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "flightNumber": "AI101",
    "travelClass": "ECONOMY",
    "departureDate": "2024-12-25",
    "passengers": [
      {
        "passengerType": "ADULT",
        "title": "Mr",
        "firstName": "John",
        "lastName": "Doe",
        "dateOfBirth": "1990-01-01",
        "gender": "MALE",
        "nationality": "Indian"
      }
    ],
    "contactEmail": "test@example.com",
    "contactPhone": "+919876543210",
    "totalAmount": 5000.00,
    "currency": "INR"
  }')

BOOKING_ID=$(echo $BOOKING_RESPONSE | jq -r '.bookingId')
echo "Booking Created: $BOOKING_ID"
```

#### Step 5: Process Payment
```bash
# Process payment
PAYMENT_RESPONSE=$(curl -s -X POST http://localhost:8085/api/v1/payments/process \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "bookingId": "'$BOOKING_ID'",
    "userId": 1,
    "amount": 5000.00,
    "currency": "INR",
    "paymentMethod": "CARD",
    "customerName": "John Doe",
    "customerEmail": "test@example.com",
    "customerPhone": "+919876543210"
  }')

echo "Payment Response: $PAYMENT_RESPONSE"
```

---

## 🧪 Testing Scenarios

### Scenario 1: Happy Path Testing
1. ✅ User Registration
2. ✅ User Login
3. ✅ Flight Search
4. ✅ Booking Creation
5. ✅ Payment Processing
6. ✅ Booking Confirmation
7. ✅ Email Notification

### Scenario 2: Error Handling Testing
1. ❌ Invalid User Registration
2. ❌ Wrong Login Credentials
3. ❌ Unauthorized Access
4. ❌ Invalid Flight Search
5. ❌ Booking with Unavailable Seats
6. ❌ Payment Failure

### Scenario 3: Edge Cases Testing
1. 🔄 Token Expiry
2. 🔄 Service Unavailability
3. 🔄 Circuit Breaker Testing
4. 🔄 Rate Limiting
5. 🔄 Concurrent Bookings

---

## 📊 Performance Testing

### Load Testing with Apache Bench
```bash
# Test user registration endpoint
ab -n 100 -c 10 -p user_data.json -T application/json http://localhost:8085/api/v1/user/register

# Test flight search endpoint
ab -n 1000 -c 50 "http://localhost:8085/api/v1/flights/search?origin=Delhi&destination=Mumbai&departureDate=2024-12-25"
```

### Monitoring During Testing
1. **Prometheus Metrics**: http://localhost:9090
2. **Grafana Dashboards**: http://localhost:3000
3. **Zipkin Traces**: http://localhost:9411
4. **Service Health**: Check actuator endpoints

---

## 🔍 Debugging & Troubleshooting

### Check Service Logs
```bash
# Check individual service logs
docker logs booking-service
docker logs payment-service
docker logs notification-service
```

### Check Database Connections
```bash
# Connect to MySQL
mysql -h localhost -P 3306 -u root -p

# Check databases
SHOW DATABASES;
USE Users_db;
SHOW TABLES;
```

### Check Kafka Topics
```bash
# List Kafka topics
docker exec flight-booking-kafka kafka-topics --list --bootstrap-server localhost:9092

# Check topic messages
docker exec flight-booking-kafka kafka-console-consumer --topic booking.created --bootstrap-server localhost:9092 --from-beginning
```

---

## ✅ Expected Test Results

### Successful Registration Response
```json
{
  "id": 1,
  "username": "testuser123",
  "email": "test@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "accountStatus": "ACTIVE",
  "registrationDate": "2024-12-23T10:30:00"
}
```

### Successful Login Response
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "username": "testuser123",
    "email": "test@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

### Successful Booking Response
```json
{
  "bookingId": "BK123456789",
  "pnr": "ABC123",
  "status": "CONFIRMED",
  "flightDetails": {
    "flightNumber": "AI101",
    "airline": "Air India",
    "origin": "Delhi",
    "destination": "Mumbai"
  },
  "totalAmount": 5000.00,
  "currency": "INR"
}
```

---

## 🎯 Quick Testing Checklist

- [ ] All services are running (Health checks pass)
- [ ] Service Registry shows all services
- [ ] User can register successfully
- [ ] User can login and get JWT token
- [ ] Flight search returns results
- [ ] Booking creation works with JWT
- [ ] Payment processing works
- [ ] Email notifications are sent
- [ ] Swagger UI is accessible for all services
- [ ] Monitoring dashboards show metrics
- [ ] Circuit breakers work during failures

---

## 🚀 Next Steps

1. **Run Basic Health Checks**
2. **Test User Registration & Login**
3. **Test Flight Search**
4. **Test Complete Booking Flow**
5. **Monitor System Performance**
6. **Test Error Scenarios**
7. **Load Testing**

**Happy Testing! 🎉**