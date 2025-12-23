#!/bin/bash

echo "🔨 Building all Flight Booking Microservices"
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
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven first."
    exit 1
fi

# Services to build
services=("ServiceRegistry" "UserService" "FlightService" "BookingService" "PaymentService" "NotificationService" "API-Gateway")

# Build each service
for service in "${services[@]}"; do
    if [ -d "$service" ]; then
        print_status "Building $service..."
        cd "$service"
        
        # Clean and package
        mvn clean package -DskipTests
        
        if [ $? -eq 0 ]; then
            print_success "$service built successfully"
        else
            print_error "Failed to build $service"
            cd ..
            exit 1
        fi
        
        cd ..
    else
        print_error "Directory $service not found"
        exit 1
    fi
done

print_success "All services built successfully!"
echo ""
echo "📦 Built artifacts:"
for service in "${services[@]}"; do
    if [ -f "$service/target/*.jar" ]; then
        echo "├── $service/target/*.jar"
    fi
done
echo ""
echo "🐳 Ready for Docker build!"