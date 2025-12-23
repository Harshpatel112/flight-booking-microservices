#!/bin/bash

echo "🧪 Flight Booking System - Automated Testing"
echo "============================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[✅ PASS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[⚠️  WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[❌ FAIL]${NC} $1"
}

# Test counters
TESTS_PASSED=0
TESTS_FAILED=0
TESTS_TOTAL=0

# Function to run test
run_test() {
    local test_name=$1
    local test_command=$2
    local expected_status=${3:-200}
    
    ((TESTS_TOTAL++))
    print_status "Testing: $test_name"
    
    if eval "$test_command"; then
        print_success "$test_name"
        ((TESTS_PASSED++))
        return 0
    else
        print_error "$test_name"
        ((TESTS_FAILED++))
        return 1
    fi
}

# Function to test HTTP endpoint
test_endpoint() {
    local url=$1
    local expected_status=${2:-200}
    local timeout=${3:-10}
    
    local response=$(curl -s -o /dev/null -w "%{http_code}" --max-time $timeout "$url")
    [ "$response" = "$expected_status" ]
}

# Function to test JSON response
test_json_endpoint() {
    local url=$1
    local expected_field=$2
    local timeout=${3:-10}
    
    local response=$(curl -s --max-time $timeout "$url")
    echo "$response" | grep -q "$expected_field"
}

echo ""
print_status "Starting system tests..."
echo ""

# Test 1: Health Checks
print_status "=== Health Check Tests ==="
run_test "Service Registry Health" "test_endpoint 'http://localhost:8761/actuator/health'"
run_test "API Gateway Health" "test_endpoint 'http://localhost:8085/actuator/health'"
run_test "User Service Health" "test_endpoint 'http://localhost:8082/api/v1/actuator/health'"
run_test "Flight Service Health" "test_endpoint 'http://localhost:8081/api/v1/actuator/health'"
run_test "Booking Service Health" "test_endpoint 'http://localhost:8083/api/v1/actuator/health'"
run_test "Payment Service Health" "test_endpoint 'http://localhost:8087/api/v1/actuator/health'"
run_test "Notification Service Health" "test_endpoint 'http://localhost:8084/api/v1/actuator/health'"

echo ""
print_status "=== Service Discovery Tests ==="
run_test "Eureka Dashboard" "test_endpoint 'http://localhost:8761'"
run_test "Service Registration" "test_json_endpoint 'http://localhost:8761/eureka/apps' 'USER-SERVICE'"

echo ""
print_status "=== API Gateway Tests ==="
run_test "Gateway Root Endpoint" "test_endpoint 'http://localhost:8085/actuator/health'"
run_test "Gateway Info Endpoint" "test_endpoint 'http://localhost:8085/actuator/info'"

echo ""
print_status "=== Public API Tests ==="
run_test "Flight Search API" "test_endpoint 'http://localhost:8085/api/v1/flights/search?origin=Delhi&destination=Mumbai&departureDate=2024-12-25&adults=1&travelClass=ECONOMY'"
run_test "Flight Details API" "test_endpoint 'http://localhost:8085/api/v1/flights/flight/AI101'"

echo ""
print_status "=== User Registration Test ==="

# Test user registration
REGISTER_PAYLOAD='{
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

REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8085/api/v1/user/register \
    -H "Content-Type: application/json" \
    -d "$REGISTER_PAYLOAD")

if echo "$REGISTER_RESPONSE" | grep -q "username"; then
    print_success "User Registration"
    ((TESTS_PASSED++))
else
    print_warning "User Registration (may already exist)"
fi
((TESTS_TOTAL++))

echo ""
print_status "=== User Login Test ==="

# Test user login
LOGIN_PAYLOAD='{
    "usernameOrEmail": "testuser123",
    "password": "Test@123456"
}'

LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8085/api/v1/user/login \
    -H "Content-Type: application/json" \
    -d "$LOGIN_PAYLOAD")

if echo "$LOGIN_RESPONSE" | grep -q "accessToken"; then
    print_success "User Login"
    ((TESTS_PASSED++))
    
    # Extract JWT token for authenticated tests
    JWT_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
    
    if [ -n "$JWT_TOKEN" ]; then
        print_success "JWT Token Extraction"
        ((TESTS_PASSED++))
        
        echo ""
        print_status "=== Authenticated API Tests ==="
        
        # Test authenticated endpoints
        AUTH_HEADER="Authorization: Bearer $JWT_TOKEN"
        
        # Test booking creation
        BOOKING_PAYLOAD='{
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
        
        BOOKING_RESPONSE=$(curl -s -X POST http://localhost:8085/api/v1/bookings/create \
            -H "Content-Type: application/json" \
            -H "$AUTH_HEADER" \
            -d "$BOOKING_PAYLOAD")
        
        if echo "$BOOKING_RESPONSE" | grep -q "bookingId\|pnr"; then
            print_success "Booking Creation"
            ((TESTS_PASSED++))
        else
            print_error "Booking Creation"
            ((TESTS_FAILED++))
        fi
        ((TESTS_TOTAL++))
        
    else
        print_error "JWT Token Extraction"
        ((TESTS_FAILED++))
        ((TESTS_TOTAL++))
    fi
else
    print_error "User Login"
    ((TESTS_FAILED++))
fi
((TESTS_TOTAL++))

echo ""
print_status "=== Monitoring Services Tests ==="
run_test "Prometheus Metrics" "test_endpoint 'http://localhost:9090/-/healthy'"
run_test "Grafana Dashboard" "test_endpoint 'http://localhost:3000/api/health'"
run_test "Zipkin Tracing" "test_endpoint 'http://localhost:9411/health'"

echo ""
print_status "=== Swagger Documentation Tests ==="
run_test "User Service Swagger" "test_endpoint 'http://localhost:8082/swagger-ui.html'"
run_test "Flight Service Swagger" "test_endpoint 'http://localhost:8081/swagger-ui.html'"
run_test "Booking Service Swagger" "test_endpoint 'http://localhost:8083/swagger-ui.html'"
run_test "Payment Service Swagger" "test_endpoint 'http://localhost:8087/swagger-ui.html'"
run_test "Notification Service Swagger" "test_endpoint 'http://localhost:8084/swagger-ui.html'"

echo ""
print_status "=== Database Connectivity Tests ==="
if docker exec flight-booking-mysql mysql -u root -pH@rsh123 -e "SHOW DATABASES;" | grep -q "Users_db"; then
    print_success "MySQL Database Connection"
    ((TESTS_PASSED++))
else
    print_error "MySQL Database Connection"
    ((TESTS_FAILED++))
fi
((TESTS_TOTAL++))

if docker exec flight-booking-redis redis-cli ping | grep -q "PONG"; then
    print_success "Redis Connection"
    ((TESTS_PASSED++))
else
    print_error "Redis Connection"
    ((TESTS_FAILED++))
fi
((TESTS_TOTAL++))

if docker exec flight-booking-kafka kafka-topics --list --bootstrap-server localhost:9092 > /dev/null 2>&1; then
    print_success "Kafka Connection"
    ((TESTS_PASSED++))
else
    print_error "Kafka Connection"
    ((TESTS_FAILED++))
fi
((TESTS_TOTAL++))

# Test Summary
echo ""
echo "📊 Test Results Summary"
echo "======================"
echo "Total Tests: $TESTS_TOTAL"
echo "Passed: $TESTS_PASSED"
echo "Failed: $TESTS_FAILED"

if [ $TESTS_FAILED -eq 0 ]; then
    print_success "All tests passed! 🎉"
    echo ""
    echo "🚀 System is fully functional!"
    echo ""
    echo "🎯 Next Steps:"
    echo "├── Explore APIs: http://localhost:8085/swagger-ui.html"
    echo "├── Monitor system: http://localhost:3000 (admin/admin)"
    echo "├── View traces: http://localhost:9411"
    echo "└── Check metrics: http://localhost:9090"
    
    exit 0
else
    print_warning "$TESTS_FAILED test(s) failed"
    echo ""
    echo "🔧 Troubleshooting:"
    echo "├── Check service logs: docker-compose logs -f"
    echo "├── Verify health: ./check-docker-health.sh"
    echo "├── Restart services: docker-compose restart"
    echo "└── Check documentation: TESTING_GUIDE.md"
    
    exit 1
fi