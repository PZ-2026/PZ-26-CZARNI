# Admin Backend - Setup and Installation Guide

## System Overview

The Admin Backend provides comprehensive REST API endpoints for managing the warehouse management system. It includes user management, financial reporting, system configuration, and administrative dashboard functionality.

## Prerequisites

- **Java:** Version 21 or higher
- **PostgreSQL:** Version 12 or higher
- **Gradle:** Version 8.x
- **Spring Boot:** 3.2.4
- **Kotlin:** 2.3.10

## Project Structure

```
backend/
├── src/main/java/magazyn/
│   ├── controllers/
│   │   ├── AdminController.java          # Main admin REST endpoints
│   │   ├── UzytkownikController.java     # User authentication
│   │   ├── MagazynController.java        # Warehouse operations
│   │   └── ...
│   ├── service/
│   │   ├── AdminService.java             # Admin business logic
│   │   ├── ZamowienieService.java        # Order management
│   │   └── ...
│   ├── entity/
│   │   ├── Uzytkownik.java               # User entity
│   │   ├── DaneFinansowe.java            # Financial data entity
│   │   ├── Konfiguracja.java             # Configuration entity
│   │   └── ...
│   ├── repository/
│   │   ├── AdminRepository.java          # Admin data access
│   │   ├── DaneFinansoweRepository.java   # Financial data repository
│   │   ├── KonfiguracijaRepository.java   # Configuration repository
│   │   └── ...
│   ├── dto/
│   │   ├── UzytkownikAdminDTO.java       # User admin DTO
│   │   ├── RaportFinansowyDTO.java       # Financial report DTO
│   │   ├── KonfiguracijaDTO.java         # Configuration DTO
│   │   └── ...
│   ├── util/
│   │   ├── AuthorizationUtil.java        # Authorization utilities
│   │   └── ...
│   └── Main.java                          # Application entry point
├── src/main/resources/
│   └── application.properties             # Configuration file
├── build.gradle.kts                       # Gradle build configuration
└── README.md                              # This file
```

## Installation Steps

### 1. Database Setup

Create PostgreSQL database and run the initialization script:

```sql
-- Create database
CREATE DATABASE magazyn;

-- Switch to the database
\c magazyn;

-- Run the schema script provided in project documentation
-- Located in project documentation: Skrypt do utworzenia struktury bazy danych
```

### 2. Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/magazyn
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Server Configuration
server.port=8080
server.servlet.context-path=/api
```

### 3. Build Project

```bash
# Using Gradle wrapper
./gradlew clean build

# Or if you have Gradle installed
gradle clean build
```

### 4. Run Application

```bash
# Using Gradle wrapper
./gradlew bootRun

# Or run the JAR directly after building
java -jar build/libs/magazyn-1.0-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints Overview

### User Management
- `GET /api/admin/users` - List all users
- `POST /api/admin/users` - Create new user
- `PUT /api/admin/users/{id}` - Update user
- `DELETE /api/admin/users/{id}` - Delete user
- `POST /api/admin/users/{id}/block` - Block user
- `POST /api/admin/users/{id}/unblock` - Unblock user

### Role Management
- `PUT /api/admin/users/{id}/role/{newRoleId}` - Change user role
- `GET /api/admin/roles/statistics` - Get role statistics

### Financial Reports
- `GET /api/admin/financial/report` - Get financial report
- `GET /api/admin/financial/revenue-month` - Get monthly revenue
- `GET /api/admin/financial/expenses-month` - Get monthly expenses
- `GET /api/admin/financial/profit-month` - Get monthly profit
- `POST /api/admin/financial/entry` - Add financial entry

### Configuration Management
- `GET /api/admin/configuration` - List all configurations
- `POST /api/admin/configuration` - Create configuration
- `PUT /api/admin/configuration/{id}` - Update configuration
- `DELETE /api/admin/configuration/{id}` - Delete configuration

### Dashboard
- `GET /api/admin/dashboard` - Get admin dashboard data

For detailed API documentation, see [ADMIN_API_DOCUMENTATION.md](./ADMIN_API_DOCUMENTATION.md)

## User Roles

| Role ID | Name | Description |
|---------|------|-------------|
| 1 | Administrator | Full system access, user management, financial reports |
| 2 | Magazynier | Warehouse operations, order packing |
| 3 | Zaopatrzeniowiec | Product management, supplier orders |
| 4 | Klient | Customer order placement |

## Key Features

### 1. User Management
- Create, read, update, delete user accounts
- User blocking/unblocking without deletion
- Role assignment and modification
- User statistics by role

### 2. Financial Management
- Track revenue and expenses
- Generate financial reports for specified periods
- Monthly financial summary
- Financial entry history

### 3. System Configuration
- Manage global system parameters
- Support for different parameter types (STRING, INTEGER, BOOLEAN, DECIMAL)
- Enable/disable configuration parameters
- Examples: theme mode, minimum stock levels, notification settings

### 4. Admin Dashboard
- Overview of system statistics
- User count by role
- Financial summary for current month
- Orders in progress
- Out-of-stock products count

## Common Tasks

### Create a New User
```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -d '{
    "imie": "John",
    "nazwisko": "Doe",
    "email": "john@example.com",
    "telefon": "123456789",
    "rola": 2,
    "firma": "Company XYZ"
  }'
```

### Get Financial Report
```bash
curl -X GET "http://localhost:8080/api/admin/financial/report?dataPoczatek=2026-05-01T00:00:00&dataKoniec=2026-05-31T23:59:59"
```

### Create Configuration Parameter
```bash
curl -X POST http://localhost:8080/api/admin/configuration \
  -H "Content-Type: application/json" \
  -d '{
    "nazwaParametru": "MIN_STOCK_LEVEL",
    "wartoscParametru": "10",
    "typParametru": "INTEGER",
    "opis": "Minimum stock level for alerts"
  }'
```

## Security Notes

⚠️ **Important:**
- All admin endpoints should be protected with authentication middleware
- Use HTTPS in production
- Implement rate limiting to prevent abuse
- Store passwords using bcrypt or similar hashing algorithms
- Use environment variables for sensitive credentials
- Implement audit logging for all admin actions

## Performance Optimization

### Database Indexes
Ensure the following indexes exist for optimal performance:
```sql
CREATE INDEX idx_uzytkownicy_email ON uzytkownicy(email);
CREATE INDEX idx_uzytkownicy_rola ON uzytkownicy(rola);
CREATE INDEX idx_dane_finansowe_data ON dane_finansowe(data);
CREATE INDEX idx_konfiguracja_nazwa ON konfiguracja_systemu(nazwa_parametru);
```

### Caching
Configuration parameters should be cached since they change infrequently.

### Pagination
For production, implement pagination on list endpoints to handle large datasets.

## Troubleshooting

### Database Connection Error
```
Error: Connection refused (localhost:5432)
Solution: 
- Ensure PostgreSQL is running
- Check database URL and credentials in application.properties
- Verify database exists and is accessible
```

### Port Already in Use
```
Error: Address already in use: bind
Solution:
- Change server port in application.properties
- Or kill the process using port 8080
```

### Build Failures
```
Solution:
- Clean build: ./gradlew clean build
- Check Java version: java -version (must be 21+)
- Verify all dependencies are accessible
```

## Development

### Running Tests
```bash
./gradlew test
```

### Code Quality
- Uses Lombok for reducing boilerplate
- Spring Boot validation annotations
- Jakarta Persistence API (JPA)
- Kotlin integration

### Git Workflow
1. Create feature branch
2. Implement changes
3. Test thoroughly
4. Create pull request
5. Merge to main after review

## Deployment

### Docker Deployment
```dockerfile
FROM openjdk:21-jdk-slim
COPY build/libs/magazyn-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Running with Docker
```bash
docker build -t magazyn-admin-backend .
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/magazyn magazyn-admin-backend
```

## Support and Documentation

- **API Documentation:** [ADMIN_API_DOCUMENTATION.md](./ADMIN_API_DOCUMENTATION.md)
- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **PostgreSQL Docs:** https://www.postgresql.org/docs/
- **Kotlin Docs:** https://kotlinlang.org/docs/

## License

This project is part of the Warehouse Management System developed by the Czarni team at University of Rzeszow.

## Contributors

- Sebastian Mikoś - Project Lead
- Wojciech Koba - DevOps
- Amadeusz Nowak - Backend
- Michał Kalisiak - Backend
- Kacper Kłósek - Frontend
