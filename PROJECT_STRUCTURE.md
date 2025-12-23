# 📁 Project Structure Overview

## 🏗️ Complete Project Architecture

```
flight-booking-microservices/
├── 📄 README.md                           # Main project documentation
├── 📄 LICENSE                             # MIT License
├── 📄 CONTRIBUTING.md                     # Contribution guidelines
├── 📄 DEPLOYMENT.md                       # Deployment guide
├── 📄 TESTING_GUIDE.md                    # Testing instructions
├── 📄 PROJECT_STRUCTURE.md                # This file
├── 📄 .gitignore                          # Git ignore rules
├── 📄 docker-compose.yml                  # Docker orchestration
├── 📄 Flight-Booking-API-Tests.postman_collection.json  # Postman tests
│
├── 🚀 Deployment Scripts/
│   ├── 📄 deploy-docker.bat               # Windows Docker deployment
│   ├── 📄 deploy-docker.sh                # Linux/Mac Docker deployment
│   ├── 📄 build-all.bat                   # Windows build script
│   ├── 📄 build-all.sh                    # Linux/Mac build script
│   ├── 📄 check-docker-health.bat         # Windows health check
│   ├── 📄 check-docker-health.sh          # Linux/Mac health check
│   ├── 📄 test-system.bat                 # Windows system test
│   ├── 📄 test-system.sh                  # Linux/Mac system test
│   └── 📄 start-services.bat              # Windows service starter
│
├── 🔧 CI/CD/
│   └── 📁 .github/
│       └── 📁 workflows/
│           └── 📄 ci-cd.yml                # GitHub Actions workflow
│
├── 🗄️ Database/
│   └── 📁 init-scripts/
│       └── 📄 01-create-databases.sql      # Database initialization
│
├── 📊 Monitoring/
│   └── 📁 monitoring/
│       ├── 📄 prometheus.yml               # Prometheus configuration
│       └── 📄 grafana-dashboards/          # Grafana dashboards
│
├── 🏢 Microservices/
│   ├── 📁 ServiceRegistry/                 # Netflix Eureka Server
│   │   ├── 📄 Dockerfile
│   │   ├── 📄 pom.xml
│   │   └── 📁 src/main/java/com/project/Service/registry/
│   │       ├── 📄 ServiceRegistryApplication.java
│   │       └── 📁 resources/
│   │           ├── 📄 application.yml
│   │           └── 📄 application-docker.yml
│   │
│   ├── 📁 API-Gateway/                     # Spring Cloud Gateway
│   │   ├── 📄 Dockerfile
│   │   ├── 📄 pom.xml
│   │   └── 📁 src/main/java/com/project/Service/gateway/
│   │       ├── 📄 ApiGatewayApplication.java
│   │       ├── 📁 config/
│   │       │   ├── 📄 GatewayConfig.java
│   │       │   └── 📄 CorsConfig.java
│   │       ├── 📁 filter/
│   │       │   └── 📄 AuthenticationFilter.java
│   │       ├── 📁 util/
│   │       │   └── 📄 JwtUtil.java
│   │       ├── 📁 controller/
│   │       │   └── 📄 FallbackController.java
│   │       └── 📁 resources/
│   │           ├── 📄 application.yml
│   │           └── 📄 application-docker.yml
│   │
│   ├── 📁 UserService/                     # User Management
│   │   ├── 📄 Dockerfile
│   │   ├── 📄 pom.xml
│   │   └── 📁 src/main/java/com/project/User/
│   │       ├── 📄 UserServiceApplication.java
│   │       ├── 📁 entity/
│   │       │   └── 📄 User.java
│   │       ├── 📁 dto/
│   │       │   ├── 📄 UserRegistrationDTO.java
│   │       │   ├── 📄 LoginRequestDTO.java
│   │       │   └── 📄 LoginResponseDTO.java
│   │       ├── 📁 repository/
│   │       │   └── 📄 UserRepository.java
│   │       ├── 📁 service/
│   │       │   ├── 📄 UserService.java
│   │       │   └── 📄 UserServiceImpl.java
│   │       ├── 📁 controller/
│   │       │   └── 📄 UserController.java
│   │       ├── 📁 config/
│   │       │   ├── 📄 SwaggerConfig.java
│   │       │   └── 📄 SecurityConfig.java
│   │       ├── 📁 exception/
│   │       │   └── 📄 GlobalExceptionHandler.java
│   │       └── 📁 resources/
│   │           ├── 📄 application.yml
│   │           └── 📄 application-docker.yml
│   │
│   ├── 📁 FlightService/                   # Flight Management
│   │   ├── 📄 Dockerfile
│   │   ├── 📄 pom.xml
│   │   └── 📁 src/main/java/com/project/flight/
│   │       ├── 📄 FlightServiceApplication.java
│   │       ├── 📁 entity/
│   │       │   ├── 📄 Flight.java
│   │       │   ├── 📄 Aircraft.java
│   │       │   └── 📄 SeatMap.java
│   │       ├── 📁 dto/
│   │       │   ├── 📄 FlightSearchRequestDTO.java
│   │       │   ├── 📄 FlightSearchResponseDTO.java
│   │       │   ├── 📄 FlightDetailsDTO.java
│   │       │   └── 📄 SeatMapDTO.java
│   │       ├── 📁 repository/
│   │       │   └── 📄 FlightRepository.java
│   │       ├── 📁 service/
│   │       │   └── 📄 FlightService.java
│   │       ├── 📁 controller/
│   │       │   └── 📄 FlightController.java
│   │       ├── 📁 config/
│   │       │   └── 📄 SwaggerConfig.java
│   │       └── 📁 resources/
│   │           ├── 📄 application.yml
│   │           └── 📄 application-docker.yml
│   │
│   ├── 📁 BookingService/                  # Booking Management (Central Hub)
│   │   ├── 📄 Dockerfile
│   │   ├── 📄 pom.xml
│   │   └── 📁 src/main/java/com/project/Service/booking/
│   │       ├── 📄 BookingServiceApplication.java
│   │       ├── 📁 entity/
│   │       │   ├── 📄 Booking.java
│   │       │   └── 📄 Passenger.java
│   │       ├── 📁 dto/
│   │       │   ├── 📄 BookingRequestDTO.java
│   │       │   ├── 📄 BookingResponseDTO.java
│   │       │   ├── 📄 PassengerDTO.java
│   │       │   └── 📄 SeatReservationResponse.java
│   │       ├── 📁 repository/
│   │       │   └── 📄 BookingRepository.java
│   │       ├── 📁 service/
│   │       │   └── 📄 BookingService.java
│   │       ├── 📁 controller/
│   │       │   └── 📄 BookingController.java
│   │       ├── 📁 client/
│   │       │   ├── 📄 UserServiceClient.java
│   │       │   └── 📄 FlightServiceClient.java
│   │       ├── 📁 event/
│   │       │   ├── 📄 EventPublisher.java
│   │       │   └── 📄 PaymentEventListener.java
│   │       ├── 📁 config/
│   │       │   ├── 📄 SwaggerConfig.java
│   │       │   └── 📄 FeignConfig.java
│   │       └── 📁 resources/
│   │           ├── 📄 application.yml
│   │           └── 📄 application-docker.yml
│   │
│   ├── 📁 PaymentService/                  # Payment Processing
│   │   ├── 📄 Dockerfile
│   │   ├── 📄 pom.xml
│   │   └── 📁 src/main/java/com/project/Service/payment/
│   │       ├── 📄 PaymentServiceApplication.java
│   │       ├── 📁 entity/
│   │       │   └── 📄 Payment.java
│   │       ├── 📁 dto/
│   │       │   ├── 📄 PaymentRequestDTO.java
│   │       │   ├── 📄 PaymentResponseDTO.java
│   │       │   └── 📄 RazorpayWebhookDTO.java
│   │       ├── 📁 repository/
│   │       │   └── 📄 PaymentRepository.java
│   │       ├── 📁 service/
│   │       │   └── 📄 PaymentService.java
│   │       ├── 📁 controller/
│   │       │   └── 📄 PaymentController.java
│   │       ├── 📁 event/
│   │       │   └── 📄 PaymentEventPublisher.java
│   │       ├── 📁 config/
│   │       │   ├── 📄 SwaggerConfig.java
│   │       │   └── 📄 RazorpayConfig.java
│   │       └── 📁 resources/
│   │           ├── 📄 application.yml
│   │           └── 📄 application-docker.yml
│   │
│   └── 📁 NotificationService/             # Email/SMS Notifications
│       ├── 📄 Dockerfile
│       ├── 📄 pom.xml
│       └── 📁 src/main/java/com/project/Service/notification/
│           ├── 📄 NotificationServiceApplication.java
│           ├── 📁 entity/
│           │   └── 📄 Notification.java
│           ├── 📁 dto/
│           │   ├── 📄 NotificationRequestDTO.java
│           │   └── 📄 EmailTemplateDTO.java
│           ├── 📁 repository/
│           │   └── 📄 NotificationRepository.java
│           ├── 📁 service/
│           │   ├── 📄 NotificationService.java
│           │   └── 📄 EmailService.java
│           ├── 📁 controller/
│           │   └── 📄 NotificationController.java
│           ├── 📁 event/
│           │   └── 📄 BookingEventListener.java
│           ├── 📁 config/
│           │   ├── 📄 SwaggerConfig.java
│           │   └── 📄 EmailConfig.java
│           └── 📁 resources/
│               ├── 📄 application.yml
│               ├── 📄 application-docker.yml
│               └── 📁 templates/
│                   ├── 📄 booking-confirmation.html
│                   └── 📄 payment-success.html
```

## 🎯 Key Features by Service

### 🏢 **Service Registry (Port: 8761)**
- Netflix Eureka Server
- Service discovery and registration
- Health monitoring
- Load balancing support

### 🚪 **API Gateway (Port: 8085)**
- Single entry point for all requests
- JWT authentication and authorization
- Rate limiting with Redis
- Circuit breaker patterns
- CORS configuration
- Request routing and load balancing

### 👤 **User Service (Port: 8082)**
- User registration and authentication
- JWT token generation and validation
- Role-based access control
- Profile management
- Password encryption with BCrypt

### ✈️ **Flight Service (Port: 8081)**
- Flight search and filtering
- Flight details and schedules
- Seat map management
- Aircraft information
- Seat reservation functionality

### 📋 **Booking Service (Port: 8083)**
- **Central Hub** for all booking operations
- Passenger management
- Booking creation and confirmation
- Integration with User and Flight services
- Event publishing for payment and notifications
- Booking status management

### 💳 **Payment Service (Port: 8087)**
- Razorpay payment gateway integration
- Payment processing and validation
- Webhook handling
- Payment status tracking
- Event publishing for booking updates

### 📧 **Notification Service (Port: 8084)**
- Email notifications (Gmail SMTP)
- SMS notifications (future)
- Event-driven notification triggers
- Template-based email generation
- Notification history tracking

## 🔧 **Infrastructure Components**

### 🗄️ **Databases**
- **MySQL 8.0** (Port: 3306)
  - Users_db (User Service)
  - flight_db (Flight Service)
  - flight_booking (Booking Service)
  - payment_db (Payment Service)
  - notification_db (Notification Service)

### 📨 **Message Broker**
- **Apache Kafka** (Port: 9092)
- **Zookeeper** (Port: 2181)
- Event-driven communication
- Async processing
- Message persistence

### 🚀 **Caching & Session**
- **Redis** (Port: 6379)
- Rate limiting
- Session management
- Caching frequently accessed data

### 📊 **Monitoring Stack**
- **Zipkin** (Port: 9411) - Distributed tracing
- **Prometheus** (Port: 9090) - Metrics collection
- **Grafana** (Port: 3000) - Monitoring dashboards

## 🔄 **Communication Patterns**

### **Synchronous Communication**
- REST APIs between services
- Feign clients for service-to-service calls
- Circuit breakers for fault tolerance

### **Asynchronous Communication**
- Kafka events for booking workflow
- Event-driven notifications
- Decoupled service interactions

### **Event Flow**
```
1. Booking Created → Kafka Event
2. Payment Processed → Kafka Event  
3. Booking Confirmed → Kafka Event
4. Notification Sent → Email/SMS
```

## 🛡️ **Security Features**

- **JWT Authentication** - Stateless token-based auth
- **Role-Based Access Control** - User roles and permissions
- **API Gateway Security** - Centralized authentication
- **Password Encryption** - BCrypt hashing
- **CORS Configuration** - Cross-origin request handling
- **Rate Limiting** - API abuse prevention

## 📈 **Scalability Features**

- **Horizontal Scaling** - Multiple service instances
- **Load Balancing** - Client-side with Eureka
- **Circuit Breakers** - Fault tolerance
- **Caching Strategy** - Redis for performance
- **Connection Pooling** - Optimized database connections
- **Async Processing** - Non-blocking operations

## 🧪 **Testing Strategy**

- **Unit Tests** - Individual component testing
- **Integration Tests** - Service interaction testing
- **API Tests** - Swagger UI and Postman
- **Health Checks** - Automated service monitoring
- **End-to-End Tests** - Complete user journey testing

## 🚀 **Deployment Options**

1. **Docker Compose** (Recommended)
   - Single command deployment
   - All services and infrastructure
   - Development and production ready

2. **Local Development**
   - Infrastructure in Docker
   - Services running locally
   - Hot reloading for development

3. **Cloud Deployment**
   - Kubernetes ready
   - AWS ECS compatible
   - Docker Swarm support

## 📚 **Documentation Coverage**

- ✅ **README.md** - Complete project overview
- ✅ **TESTING_GUIDE.md** - Comprehensive testing instructions
- ✅ **DEPLOYMENT.md** - Deployment guide for all environments
- ✅ **CONTRIBUTING.md** - Contribution guidelines
- ✅ **API Documentation** - Swagger/OpenAPI for all services
- ✅ **Postman Collection** - Ready-to-use API tests
- ✅ **GitHub Actions** - CI/CD pipeline

---

## 🎯 **Project Highlights**

This project demonstrates:
- **Industry-standard microservices architecture**
- **Production-ready patterns and practices**
- **Modern technology stack**
- **Comprehensive documentation**
- **Multiple deployment strategies**
- **Extensive testing coverage**
- **Real-world business logic**

Perfect for learning, interviews, and production use! 🚀