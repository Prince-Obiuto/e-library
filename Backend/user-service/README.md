# User Service

## Overview
The User Service is responsible for managing user accounts, roles, and student account lifecycle in the Faculty E-Library System. It handles user registration, profile management, role-based access control, and automatic student account expiry based on graduation year.

## Features
- ✅ User registration with faculty email validation
- ✅ Role-based access control (ADMIN_STAFF, ACADEMIC_STAFF, STUDENT, GUEST)
- ✅ Student account auto-expiry based on graduation year
- ✅ Scheduled cleanup of expired accounts
- ✅ User profile management
- ✅ User search and filtering
- ✅ Integration with Eureka service discovery
- ✅ Kafka event publishing for notifications

## Technology Stack
- **Framework:** Spring Boot 3.5.7
- **Language:** Java 21
- **Database:** PostgreSQL
- **Message Broker:** Apache Kafka
- **Service Discovery:** Netflix Eureka
- **Build Tool:** Maven

## Architecture
```
user-service/
├── config/          # Configuration classes
├── controller/      # REST API endpoints
├── dto/             # Data Transfer Objects
│   ├── request/     # Request DTOs
│   └── response/    # Response DTOs
├── entity/          # JPA entities
├── enums/           # Enumerations
├── event/           # Kafka event producers
├── exception/       # Custom exceptions and handlers
├── repository/      # Data access layer
├── scheduler/       # Scheduled tasks
├── service/         # Business logic
└── util/            # Utility classes
```

## Database Schema

### Users Table
| Column          | Type         | Description               |
|-----------------|--------------|---------------------------|
| id              | UUID         | Primary key               |
| email           | VARCHAR(100) | Unique faculty email      |
| first_name      | VARCHAR(100) | User's first name         |
| last_name       | VARCHAR(100) | User's last name          |
| role            | VARCHAR(20)  | User role (enum)          |
| status          | VARCHAR(20)  | Account status (enum)     |
| account_type    | VARCHAR(20)  | Account type (enum)       |
| phone_number    | VARCHAR(20)  | Contact number            |
| department      | VARCHAR(100) | Department name           |
| matric_number   | VARCHAR(50)  | Student ID (unique)       |
| staff_id        | VARCHAR(50)  | Staff ID (unique)         |
| graduation_year | INTEGER      | Expected graduation year  |
| email_verified  | BOOLEAN      | Email verification status |
| created_at      | TIMESTAMP    | Creation timestamp        |
| updated_at      | TIMESTAMP    | Last update timestamp     |
| last_login_at   | TIMESTAMP    | Last login timestamp      |

## API Endpoints

### User Management
| Method | Endpoint                   | Description         | Access        |
|--------|----------------------------|---------------------|---------------|
| POST   | `/api/users/register`      | Register new user   | Public        |
| GET    | `/api/users`               | Get all users       | Admin         |
| GET    | `/api/users/{id}`          | Get user by ID      | Authenticated |
| GET    | `/api/users/email/{email}` | Get user by email   | Authenticated |
| PUT    | `/api/users/{id}/profile`  | Update user profile | Owner/Admin   |
| PUT    | `/api/users/{id}/role`     | Update user role    | Admin         |
| PUT    | `/api/users/{id}/status`   | Update user status  | Admin         |
| DELETE | `/api/users/{id}`          | Delete user         | Admin         |

### Search & Filter
| Method | Endpoint                       | Description             | Access |
|--------|--------------------------------|-------------------------|--------|
| GET    | `/api/users/search?keyword=`   | Search users            | Admin  |
| GET    | `/api/users/role/{role}`       | Get users by role       | Admin  |
| GET    | `/api/users/status/{status}`   | Get users by status     | Admin  |
| GET    | `/api/users/department/{dept}` | Get users by department | Admin  |

### Statistics & Reports
| Method | Endpoint                | Description          | Access |
|--------|-------------------------|----------------------|--------|
| GET    | `/api/users/statistics` | Get user statistics  | Admin  |
| GET    | `/api/users/expired`    | Get expired accounts | Admin  |

### Health Check
| Method | Endpoint            | Description          | Access |
|--------|---------------------|----------------------|--------|
| GET    | `/api/users/health` | Service health check | Public |

## Configuration

### Application Properties
```properties
server.port=8082

spring.application.name=user-service

spring.datasource.url=jdbc:postgresql://localhost:5432/user_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:password}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true
eureka.instance.prefer-ip-address=true
eureka.instance.instance-id=${spring.application.name}:${random.value}

faculty.email.allowed-domains=futo.edu.ng
faculty.email.validation-enabled=true

scheduler.student-account-cleanup.enabled=true
scheduler.student-account-cleanup.cron=0 0 2 * * ?
scheduler.student-account-cleanup.warning-days-before-expiry=30
```

### Environment Variables
```bash
DB_PASSWORD=your_database_password
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
EUREKA_SERVER=http://localhost:8761/eureka/
```

## Business Rules

### Email Validation
- Only emails from approved faculty domains are allowed
- Configurable domains in `application.properties`
- Format: `user@futo.edu.ng`

### Student Account Expiry
- Students must provide graduation year during registration
- Accounts automatically expire after graduation year passes
- Daily scheduled check runs at 2:00 AM
- Warning emails sent 1 year before expiry (every Monday at 9:00 AM)
- Expired accounts deleted after 6 months (monthly cleanup on 1st at 3:00 AM)

### Role-Based Access
- **ADMIN_STAFF**: Full system access, user management
- **ACADEMIC_STAFF**: Upload teaching materials, manage own content
- **STUDENT**: Upload projects, access materials, account expires
- **GUEST**: Read-only access to approved materials

### Account Types
- **STUDENT**: Requires matric number and graduation year
- **STAFF**: Requires staff ID
- **ADMIN**: Requires staff ID, full privileges

## Scheduled Tasks

### Daily Expiry Check (2:00 AM)
```java
@Scheduled(cron = "0 0 2 * * ?")
public void checkAndExpireStudentAccounts()
```
- Checks for students whose graduation year has passed
- Updates status to EXPIRED
- Sends notification via Kafka

### Weekly Warning (Monday 9:00 AM)
```java
@Scheduled(cron = "0 0 9 * * MON")
public void sendExpiryWarnings()
```
- Finds students graduating next year
- Sends warning notification once

### Monthly Cleanup (1st at 3:00 AM)
```java
@Scheduled(cron = "0 0 3 1 * ?")
public void deleteExpiredAccounts()
```
- Deletes accounts expired for 6+ months
- Permanent deletion from database

## Kafka Events

### Event Types
- `USER_REGISTERED`: New user registration
- `USER_EXPIRED`: Student account expired
- `USER_EXPIRY_WARNING`: Warning before expiry
- `USER_DELETED`: Account deleted
- `USER_ROLE_CHANGED`: Role updated

### Event Structure
```json
{
  "eventId": "uuid",
  "eventType": "USER_EXPIRED",
  "userId": "user-uuid",
  "email": "student@students.futo.edu.ng",
  "firstName": "John",
  "lastName": "Doe",
  "message": "Your account has expired",
  "timestamp": "2025-01-01T00:00:00"
}
```

## Running the Service

### Prerequisites
- Java 21+
- PostgreSQL 15+
- Apache Kafka (optional, for events)
- Eureka Server running on port 8761

### Build
```bash
cd Backend/user-service
./mvnw clean install
```

### Run
```bash
./mvnw spring-boot:run
```

### Run with Docker
```bash
docker build -t user-service .
docker run -p 8082:8082 user-service
```

## Testing

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=UserServiceImplTest
```

### Test Coverage
```bash
./mvnw clean verify
# View report at: target/site/jacoco/index.html
```

## Development

### Adding New Endpoints
1. Define DTO in `dto/request` or `dto/response`
2. Add method to service interface
3. Implement in service implementation
4. Create controller endpoint
5. Write tests (TDD approach)
6. Update API documentation

### Database Migrations
- Using JPA auto-DDL for development
- For production, use Flyway or Liquibase
- Place migration scripts in `src/main/resources/db/migration`

## Monitoring

### Actuator Endpoints
- Health: `http://localhost:8082/actuator/health`
- Info: `http://localhost:8082/actuator/info`
- Metrics: `http://localhost:8082/actuator/metrics`

### Logs
- Location: `logs/user-service.log`
- Level: Configurable in `application.yml`

## Troubleshooting

### Common Issues

**Database Connection Failed**
```bash
# Check PostgreSQL is running
sudo systemctl status postgresql

# Check connection
psql -h localhost -U postgres -d user_db
```

**Eureka Registration Failed**
```bash
# Verify Eureka server is running
curl http://localhost:8761/eureka/apps

# Check service registration
curl http://localhost:8761/eureka/apps/USER-SERVICE
```

**Email Validation Failing**

```properties
# Disable validation for testing
faculty.email.validation-enabled=false
```

## Future Enhancements
- [ ] Integration with LDAP for faculty authentication
- [ ] Two-factor authentication
- [ ] Profile picture upload
- [ ] Email verification with OTP
- [ ] Password reset functionality
- [ ] User activity logging
- [ ] Audit trail for admin actions

## Contributing
1. Follow TDD approach - write tests first
2. Maintain test coverage above 80%
3. Follow Java coding conventions
4. Document all public methods
5. Update README for new features

## License
Apache License 2.0 - For academic use within Faculty LAN

## Support
For issues and questions, contact the development team.