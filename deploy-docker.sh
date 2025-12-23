#!/bin/bash

echo "🚀 Flight Booking Microservices - Docker Deployment"
echo "=================================================="

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
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Check if Docker is running
if ! docker info &> /dev/null; then
    print_error "Docker is not running. Please start Docker first."
    exit 1
fi

print_status "Starting Flight Booking Microservices deployment..."

# Step 1: Clean up any existing containers
print_status "Cleaning up existing containers..."
docker-compose down --remove-orphans
docker system prune -f

# Step 2: Build all Maven projects
print_status "Building all Maven projects..."
chmod +x build-all.sh
if [ -f "build-all.sh" ]; then
    ./build-all.sh
else
    # Fallback: build each service individually
    services=("ServiceRegistry" "UserService" "FlightService" "BookingService" "PaymentService" "NotificationService" "API-Gateway")
    
    for service in "${services[@]}"; do
        if [ -d "$service" ]; then
            print_status "Building $service..."
            cd "$service"
            mvn clean package -DskipTests
            if [ $? -eq 0 ]; then
                print_success "$service built successfully"
            else
                print_error "Failed to build $service"
                exit 1
            fi
            cd ..
        fi
    done
fi

# Step 3: Build Docker images
print_status "Building Docker images..."
docker-compose build --no-cache
if [ $? -eq 0 ]; then
    print_success "All Docker images built successfully"
else
    print_error "Failed to build Docker images"
    exit 1
fi

# Step 4: Start all services
print_status "Starting all services with Docker Compose..."
docker-compose up -d
if [ $? -eq 0 ]; then
    print_success "All services started successfully"
else
    print_error "Failed to start services"
    exit 1
fi

# Step 5: Wait for services to be ready
print_status "Waiting for services to be ready..."
sleep 30

# Step 6: Check service health
print_status "Checking service health..."
chmod +x check-docker-health.sh
if [ -f "check-docker-health.sh" ]; then
    ./check-docker-health.sh
else
    # Fallback health check
    services=(
        "http://localhost:8761/actuator/health:Service Registry"
        "http://localhost:8085/actuator/health:API Gateway"
        "http://localhost:8082/api/v1/actuator/health:User Service"
        "http://localhost:8081/api/v1/actuator/health:Flight Service"
        "http://localhost:8083/api/v1/actuator/health:Booking Service"
        "http://localhost:8087/api/v1/actuator/health:Payment Service"
        "http://localhost:8084/api/v1/actuator/health:Notification Service"
    )
    
    for service in "${services[@]}"; do
        url=$(echo $service | cut -d: -f1-2)
        name=$(echo $service | cut -d: -f3)
        
        if curl -f "$url" > /dev/null 2>&1; then
            print_success "$name is healthy"
        else
            print_warning "$name is not responding (may still be starting up)"
        fi
    done
fi

# Step 7: Display access URLs
echo ""
echo "🎉 Deployment completed successfully!"
echo "======================================"
echo ""
echo "📊 Service URLs:"
echo "├── Service Registry: http://localhost:8761"
echo "├── API Gateway: http://localhost:8085"
echo "├── Swagger UI: http://localhost:8085/swagger-ui.html"
echo "├── Prometheus: http://localhost:9090"
echo "├── Grafana: http://localhost:3000 (admin/admin)"
echo "└── Zipkin: http://localhost:9411"
echo ""
echo "🧪 Testing:"
echo "├── Run system tests: ./test-system.sh"
echo "├── Check health: ./check-docker-health.sh"
echo "└── View logs: docker-compose logs -f"
echo ""
echo "📚 Documentation:"
echo "├── API Testing: TESTING_GUIDE.md"
echo "├── Deployment: DEPLOYMENT.md"
echo "└── Contributing: CONTRIBUTING.md"
echo ""
echo "🚀 Happy coding!"