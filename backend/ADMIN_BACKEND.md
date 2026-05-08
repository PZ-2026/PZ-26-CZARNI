# 🏢 Administrator Backend - Warehouse Management System

Complete backend implementation for the Administrator Panel of the Warehouse Management System.

## 📁 What's Included

### Java Classes

#### Controllers (1 new)
- **AdminController.java** - 27 REST API endpoints for admin operations

#### Services (1 new)
- **AdminService.java** - 30+ methods implementing all admin functionality

#### Entities (2 new, 1 enhanced)
- **DaneFinansowe.java** - Financial data tracking
- **Konfiguracja.java** - System configuration
- **Uzytkownik.java** - Enhanced with blocking capability

#### Repositories (4 new, 1 enhanced)
- **DaneFinansoweRepository.java** - Financial data access
- **KonfiguracijaRepository.java** - Configuration management
- **ZamowienieKlientaRepository.java** - Order queries
- **UzytkownikRepository.java** - Enhanced user queries

#### DTOs (4 new)
- **UzytkownikAdminDTO.java** - User data transfer
- **RaportFinansowyDTO.java** - Financial reports
- **KonfiguracijaDTO.java** - Configuration data
- **PanelAdminaDTO.java** - Dashboard data

#### Utilities (1 new)
- **AuthorizationUtil.java** - Role and authorization management

### Configuration
- **application.properties** - Spring Boot configuration

### Documentation Files

1. **ADMIN_API_DOCUMENTATION.md** (📖 21 KB)
   - Complete API reference
   - All 27 endpoints documented
   - Request/response examples
   - Query parameters
   - Error handling

2. **ADMIN_BACKEND_README.md** (📖 12 KB)
   - Setup and installation guide
   - Project structure
   - Common tasks
   - Troubleshooting
   - Deployment guide

3. **IMPLEMENTATION_SUMMARY.md** (📖 16 KB)
   - Complete component overview
   - File structure
   - Features implemented
   - Integration points
   - Future enhancements

4. **initial_configuration.sql** (📋 SQL)
   - Initial configuration data
   - Default parameters
   - Sample financial data

## 🎯 Features

### User Management
- ✅ Create, read, update, delete users
- ✅ Block/unblock users
- ✅ Change user roles
- ✅ User statistics by role

### Financial Management
- ✅ Track revenue and expenses
- ✅ Generate financial reports
- ✅ Monthly financial summary
- ✅ Financial entry history

### System Configuration
- ✅ Global parameter management
- ✅ Support for multiple data types
- ✅ Enable/disable parameters
- ✅ Configuration organization

### Admin Dashboard
- ✅ System statistics
- ✅ User overview
- ✅ Financial summary
- ✅ Order management overview
- ✅ Inventory alerts

## 🚀 Quick Start

### 1. Build
```bash
cd backend
./gradlew clean build
```

### 2. Configure Database
```bash
# Update application.properties with your database credentials
spring.datasource.url=jdbc:postgresql://localhost:5432/magazyn
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 3. Run
```bash
./gradlew bootRun
```

### 4. API Available at
```
http://localhost:8080/api/admin
```

## 📚 Documentation Links

- 📖 [API Documentation](./ADMIN_API_DOCUMENTATION.md) - Complete endpoint reference
- 📖 [Setup Guide](./ADMIN_BACKEND_README.md) - Installation and configuration
- 📖 [Implementation Summary](./IMPLEMENTATION_SUMMARY.md) - Technical overview
- 📋 [Initial Configuration](./initial_configuration.sql) - Database setup

## 🔗 API Endpoints Summary

### Users (8 endpoints)
- `GET /users` - List all users
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create user
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user
- `POST /users/{id}/block` - Block user
- `POST /users/{id}/unblock` - Unblock user
- `GET /users/role/{roleId}` - Get users by role

### Roles (2 endpoints)
- `PUT /users/{id}/role/{newRoleId}` - Change role
- `GET /roles/statistics` - Role statistics

### Financial (6 endpoints)
- `GET /financial/report` - Financial report
- `GET /financial/revenue-month` - Monthly revenue
- `GET /financial/expenses-month` - Monthly expenses
- `GET /financial/profit-month` - Monthly profit
- `GET /financial/history` - Financial history
- `POST /financial/entry` - Add entry

### Configuration (6 endpoints)
- `GET /configuration` - List all
- `GET /configuration/active` - Active only
- `GET /configuration/{parametr}` - Get by name
- `POST /configuration` - Create
- `PUT /configuration/{id}` - Update
- `DELETE /configuration/{id}` - Delete

### Dashboard (1 endpoint)
- `GET /dashboard` - Dashboard data

## 👥 User Roles

| ID | Name | Permissions |
|---|---|---|
| 1 | Administrator | All system access, user management |
| 2 | Magazynier | Warehouse operations, order packing |
| 3 | Zaopatrzeniowiec | Product & supplier management |
| 4 | Klient | Customer orders only |

## 🛠️ Technology Stack

- **Java 21** - Programming language
- **Spring Boot 3.2.4** - Web framework
- **Spring Data JPA** - ORM framework
- **PostgreSQL** - Database
- **Kotlin 2.3.10** - Language support
- **Gradle 8.x** - Build tool
- **Lombok** - Code generation

## 📋 Requirements

- Java 21+
- PostgreSQL 12+
- Gradle 8.x
- 4GB RAM minimum
- 500MB disk space

## 🔒 Security Notes

⚠️ **Important:**
- Implement authentication middleware
- Use HTTPS in production
- Implement rate limiting
- Use bcrypt for password hashing
- Use environment variables for credentials
- Implement audit logging

## 📊 Database Tables

### New Tables
- `dane_finansowe` - Financial data
- `konfiguracja_systemu` - System configuration

### Enhanced Tables
- `uzytkownicy` - Added `zablokowany` field

## 🧪 Testing

### Run Tests
```bash
./gradlew test
```

### Test Categories
- Unit tests for services
- Integration tests for repositories
- Controller endpoint tests
- End-to-end workflows

## 📈 Performance

- **Database Indexes** - Optimized queries
- **Repository Pattern** - Flexible queries
- **Service Layer** - Centralized logic
- **Caching Ready** - Configuration parameters
- **Pagination Ready** - For large datasets

## 🔄 Integration

### Integrates With
- Existing user management
- Order system
- Warehouse operations
- Product management
- Supplier management

### Can Be Extended With
- PDF report generation
- Email notifications
- Advanced analytics
- Audit logging
- Two-factor authentication

## 📝 Configuration Parameters

Included default parameters:
- Theme settings (light/dark)
- Notifications (email, SMS, system)
- Stock management (min levels, thresholds)
- Financial settings (tax rate, currency)
- Security (session timeout, password rules)
- Report settings (format, retention)
- System maintenance (backups, updates)

## 🎓 Example Usage

### Create Admin User
```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -d '{
    "imie": "Jan",
    "nazwisko": "Kowalski",
    "email": "admin@magazyn.pl",
    "telefon": "123456789",
    "rola": 1
  }'
```

### Get Financial Report
```bash
curl -X GET "http://localhost:8080/api/admin/financial/report?dataPoczatek=2026-05-01T00:00:00&dataKoniec=2026-05-31T23:59:59"
```

### View Dashboard
```bash
curl -X GET http://localhost:8080/api/admin/dashboard
```

## 🐛 Troubleshooting

### Build Issues
```bash
./gradlew clean build --refresh-dependencies
```

### Database Connection Error
- Check PostgreSQL is running
- Verify credentials in application.properties
- Ensure database exists

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

## 📞 Support

- 📖 See [ADMIN_API_DOCUMENTATION.md](./ADMIN_API_DOCUMENTATION.md)
- 📖 See [ADMIN_BACKEND_README.md](./ADMIN_BACKEND_README.md)
- 📖 See [IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md)

## 👥 Team

**Czarni Team** - University of Rzeszow 2026
- Sebastian Mikoś - Project Lead
- Wojciech Koba - DevOps
- Amadeusz Nowak - Backend
- Michał Kalisiak - Backend  
- Kacper Kłósek - Frontend

## 📄 License

Part of the Warehouse Management System project at University of Rzeszow

---

**Status:** ✅ Complete and Ready for Integration

**Last Updated:** May 2026

**Version:** 1.0
