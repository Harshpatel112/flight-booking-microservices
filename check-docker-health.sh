#!/bin/bash

echo "🏥 Flight Booking Microservices - Health Check"
echo "=============================================="

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
    echo -e "${GREEN}[✅ HEALTHY]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[⚠️  WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[❌ UNHEALTHY]${NC} $1"
}

# Function to check service health
check_service() {
    local url=$1
    local name=$2
    local timeout=${3:-10}
    
    if curl -f --max-time $timeout "$url" > /dev/null 2>&1; then
        print_success "$name"
        return 0
    else
        print_error "$name"
        return 1
    fi
}

# Function to check container status
check_container() {
    local container_name=$1
    local service_name=$2
    
    if docker ps --format "table {{.Names}}\t{{.Status}}" | grep -q "$container_name.*Up"; then
        print_success "$service_name Container"
        return 0
    else
        print_error "$service_name Container"
        return 1
    fi
}

print_status "Checking Docker containers..."
echo ""

# Check container status
containers=(
    "flight-booking-mysql:MySQL Database"
    "flight-booking-kafka:Apache Kafka"
    "flight-booking-redis:Redis Cache"
    "flight-booking-zipkin:Zipkin Tracing"
    "flight-booking-prometheus:Prometheus Monitoring"
    "flight-booking-grafana:Grafana Dashboard"
    "service-registry:Service Registry"
    "user-service:User Service"
    "flight-service:Flight Service"
    "booking-service:Booking Service"
    "payment-service:Payment Service"
    "notification-service:Notification Service"
    "api-gateway:API Gateway"
)

container_health=0
for container in "${containers[@]}"; do
    container_name=$(echo $container | cut -d: -f1)
    service_name=$(echo $container | cut -d: -f2)
    
    if ! check_container "$container_name" "$service_name"; then
        ((container_health++))
    fi
done

echo ""
print_status "Checking service health endpoints..."
echo ""

# Check service health endpoints
services=(
    "http://localhost:8761/actuator/health:Service Registry"
    "http://localhost:8085/actuator/health:API Gateway"
    "http://localhost:8082/api/v1/actuator/health:User Service"
    "http://localhost:8081/api/v1/actuator/health:Flight Service"
    "http://localhost:8083/api/v1/actuator/health:Booking Service"
    "http://localhost:8087/api/v1/actuator/health:Payment Service"
    "http://localhost:8084/api/v1/actuator/health:Notification Service"
)

service_health=0
for service in "${services[@]}"; do
    url=$(echo $service | cut -d: -f1-2)
    name=$(echo $service | cut -d: -f3)
    
    if ! check_service "$url" "$name" 15; then
        ((service_health++))
    fi
done

echo ""
print_status "Checking infrastructure services..."
echo ""

# Check infrastructure services
infra_services=(
    "http://localhost:9090/-/healthy:Prometheus"
    "http://localhost:3000/api/health:Grafana"
    "http://localhost:9411/health:Zipkin"
)

infra_health=0
for service in "${infra_services[@]}"; do
    url=$(echo $service | cut -d: -f1-2)
    name=$(echo $service | cut -d: -f3)
    
    if ! check_service "$url" "$name" 10; then
        ((infra_health++))
    fi
done

# Check database connectivity
echo ""
print_status "Checking database connectivity..."
if docker exec flight-booking-mysql mysqladmin ping -h localhost --silent; then
    print_success "MySQL Database Connection"
else
    print_error "MySQL Database Connection"
    ((infra_health++))
fi

# Check Redis connectivity
if docker exec flight-booking-redis redis-cli ping | grep -q "PONG"; then
    print_success "Redis Cache Connection"
else
    print_error "Redis Cache Connection"
    ((infra_health++))
fi

# Check Kafka connectivity
if docker exec flight-booking-kafka kafka-broker-api-versions --bootstrap-server localhost:9092 > /dev/null 2>&1; then
    print_success "Apache Kafka Connection"
else
    print_error "Apache Kafka Connection"
    ((infra_health++))
fi

# Summary
echo ""
echo "📊 Health Check Summary"
echo "======================"
total_issues=$((container_health + service_health + infra_health))

if [ $total_issues -eq 0 ]; then
    print_success "All services are healthy! 🎉"
    echo ""
    echo "🚀 System is ready for testing!"
    echo "├── API Gateway: http://localhost:8085"
    echo "├── Swagger UI: http://localhost:8085/swagger-ui.html"
    echo "├── Service Registry: http://localhost:8761"
    echo "├── Grafana: http://localhost:3000 (admin/admin)"
    echo "├── Prometheus: http://localhost:9090"
    echo "└── Zipkin: http://localhost:9411"
    echo ""
    echo "🧪 Run tests: ./test-system.sh"
else
    print_warning "Found $total_issues issue(s)"
    echo ""
    echo "🔧 Troubleshooting:"
    echo "├── Check logs: docker-compose logs -f"
    echo "├── Restart services: docker-compose restart"
    echo "├── View containers: docker ps -a"
    echo "└── Check resources: docker stats"
    
    if [ $container_health -gt 0 ]; then
        echo ""
        print_warning "Container issues detected. Try:"
        echo "docker-compose down && docker-compose up -d"
    fi
    
    if [ $service_health -gt 0 ]; then
        echo ""
        print_warning "Service health issues detected. Services may still be starting up."
        echo "Wait 30-60 seconds and run this script again."
    fi
fi

echo ""
echo "📚 For more help, check:"
echo "├── TESTING_GUIDE.md"
echo "├── DEPLOYMENT.md"
echo "└── docker-compose logs [service-name]"

exit $total_issues