# Contributing to Flight Booking Microservices

Thank you for your interest in contributing to this project! This document provides guidelines and information for contributors.

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Git

### Development Setup
1. Fork the repository
2. Clone your fork: `git clone https://github.com/your-username/flight-booking-microservices.git`
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Set up the development environment:
   ```bash
   # Start infrastructure services
   docker-compose up -d mysql kafka redis zipkin prometheus grafana
   
   # Run services locally for development
   cd ServiceRegistry && mvn spring-boot:run
   # ... repeat for other services
   ```

## 📋 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add appropriate comments for complex logic
- Maintain consistent indentation (4 spaces)

### Architecture Principles
- Maintain microservice boundaries
- Use event-driven communication for async operations
- Implement proper error handling and circuit breakers
- Follow REST API best practices
- Ensure proper security measures

### Testing
- Write unit tests for new features
- Ensure integration tests pass
- Test API endpoints with Swagger UI
- Verify Docker deployment works

## 🔧 Making Changes

### Branch Naming
- Feature: `feature/description`
- Bug fix: `bugfix/description`
- Hotfix: `hotfix/description`

### Commit Messages
Follow conventional commit format:
```
type(scope): description

Examples:
feat(booking): add seat selection functionality
fix(payment): resolve Razorpay integration issue
docs(readme): update API documentation
```

### Pull Request Process
1. Ensure your code follows the style guidelines
2. Update documentation if needed
3. Add or update tests as appropriate
4. Ensure all tests pass
5. Update the README.md if you've made significant changes
6. Create a pull request with a clear description

## 🧪 Testing Your Changes

### Local Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Test with Docker
docker-compose up --build
```

### API Testing
- Use Swagger UI: http://localhost:8085/swagger-ui.html
- Import Postman collection: `Flight-Booking-API-Tests.postman_collection.json`
- Run automated tests: `test-system.bat`

## 📚 Documentation

### Code Documentation
- Add JavaDoc comments for public methods
- Document complex algorithms or business logic
- Update API documentation when changing endpoints

### README Updates
- Update feature lists when adding new functionality
- Add new configuration options
- Update deployment instructions if changed

## 🐛 Reporting Issues

### Bug Reports
Include:
- Clear description of the issue
- Steps to reproduce
- Expected vs actual behavior
- Environment details (OS, Java version, etc.)
- Relevant logs or error messages

### Feature Requests
Include:
- Clear description of the proposed feature
- Use case and business justification
- Proposed implementation approach (if any)

## 🔍 Code Review Process

### What We Look For
- Code quality and maintainability
- Proper error handling
- Security considerations
- Performance implications
- Test coverage
- Documentation updates

### Review Timeline
- Initial review within 2-3 business days
- Follow-up reviews within 1-2 business days
- Merge after approval from at least one maintainer

## 🏗️ Architecture Decisions

### Adding New Services
1. Follow the existing service structure
2. Implement proper health checks
3. Add service discovery registration
4. Include monitoring and tracing
5. Update Docker Compose configuration
6. Add to API Gateway routing

### Database Changes
1. Create migration scripts
2. Update Docker initialization scripts
3. Test with existing data
4. Document schema changes

### API Changes
1. Maintain backward compatibility when possible
2. Version APIs appropriately
3. Update Swagger documentation
4. Update Postman collection
5. Test with existing clients

## 📞 Getting Help

- Create an issue for questions
- Check existing documentation
- Review similar implementations in the codebase
- Ask in pull request comments

## 🎯 Areas for Contribution

### High Priority
- Performance optimizations
- Additional test coverage
- Security enhancements
- Documentation improvements

### Medium Priority
- New payment gateway integrations
- Additional notification channels
- Enhanced monitoring dashboards
- API rate limiting improvements

### Low Priority
- UI/Frontend development
- Additional deployment options
- Performance benchmarking tools

## 📜 Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Help others learn and grow
- Maintain professional communication

Thank you for contributing to make this project better! 🚀