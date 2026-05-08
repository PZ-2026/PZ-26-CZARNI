# Admin Backend - Quick Reference Card

## 🚀 Getting Started

### 1. Build & Run
```bash
cd backend
./gradlew clean build
./gradlew bootRun
```

### 2. API URL
```
Base URL: http://localhost:8080/api/admin
```

### 3. Default Database
```
URL: jdbc:postgresql://localhost:5432/magazyn
User: postgres
Pass: postgres
```

---

## 👤 User Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users` | List all users |
| GET | `/users/{id}` | Get user by ID |
| POST | `/users` | Create new user |
| PUT | `/users/{id}` | Update user |
| DELETE | `/users/{id}` | Delete user |
| POST | `/users/{id}/block` | Block user |
| POST | `/users/{id}/unblock` | Unblock user |
| GET | `/users/role/{roleId}` | Get users by role |

### Create User Example
```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -d '{
    "imie": "John",
    "nazwisko": "Doe",
    "email": "john@example.com",
    "telefon": "123456789",
    "rola": 2,
    "firma": "Company"
  }'
```

---

## 💼 Financial Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/financial/report?dataPoczatek={start}&dataKoniec={end}` | Get financial report |
| GET | `/financial/revenue-month` | Monthly revenue |
| GET | `/financial/expenses-month` | Monthly expenses |
| GET | `/financial/profit-month` | Monthly profit |
| GET | `/financial/history` | Financial history |
| POST | `/financial/entry` | Add financial entry |

### Financial Report Example
```bash
curl -X GET "http://localhost:8080/api/admin/financial/report?dataPoczatek=2026-05-01T00:00:00&dataKoniec=2026-05-31T23:59:59"
```

### Add Financial Entry Example
```bash
curl -X POST http://localhost:8080/api/admin/financial/entry \
  -H "Content-Type: application/json" \
  -d '{
    "data": "2026-05-08T14:30:00",
    "przychody": 1000.00,
    "wydatki": 500.00,
    "typ": "SPRZEDAZ",
    "idZamowienia": 42
  }'
```

---

## ⚙️ Configuration Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/configuration` | List all configurations |
| GET | `/configuration/active` | List active only |
| GET | `/configuration/{name}` | Get by name |
| POST | `/configuration` | Create new |
| PUT | `/configuration/{id}` | Update |
| DELETE | `/configuration/{id}` | Delete |

### Create Configuration Example
```bash
curl -X POST http://localhost:8080/api/admin/configuration \
  -H "Content-Type: application/json" \
  -d '{
    "nazwaParametru": "MIN_STOCK_LEVEL",
    "wartoscParametru": "10",
    "typParametru": "INTEGER",
    "opis": "Minimum stock level",
    "aktywna": true
  }'
```

---

## 📊 Dashboard Endpoint

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/dashboard` | Get admin dashboard data |

### Response Includes
- User count
- Product count
- Monthly financial data
- Orders in progress
- Out-of-stock products

---

## 👥 Role Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/users/{id}/role/{roleId}` | Change user role |
| GET | `/roles/statistics` | Get role statistics |

### Role IDs
- `1` = Administrator
- `2` = Magazynier (Warehouse Worker)
- `3` = Zaopatrzeniowiec (Supply Manager)
- `4` = Klient (Customer)

---

## 📝 Common Request/Response Patterns

### User DTO
```json
{
  "id": 1,
  "imie": "John",
  "nazwisko": "Doe",
  "email": "john@example.com",
  "telefon": "123456789",
  "rola": 2,
  "firma": "Company XYZ",
  "nip": "1234567890",
  "zablokowany": false
}
```

### Financial Report DTO
```json
{
  "dataPoczatek": "2026-05-01T00:00:00",
  "dataKoniec": "2026-05-31T23:59:59",
  "sumaPrzychodow": 15000.50,
  "sumaWydatkow": 8500.25,
  "sumaZysku": 6500.25,
  "liczbaTransakcji": 45
}
```

### Configuration DTO
```json
{
  "id": 1,
  "nazwaParametru": "MIN_STOCK_LEVEL",
  "wartoscParametru": "10",
  "typParametru": "INTEGER",
  "opis": "Minimum stock level",
  "aktywna": true
}
```

---

## 🔑 Authorization Util Helper Methods

```java
// Check roles
AuthorizationUtil.isAdmin(user);
AuthorizationUtil.isMagazynier(user);
AuthorizationUtil.isZaopatrzeniowiec(user);
AuthorizationUtil.isKlient(user);

// Get role info
String roleName = AuthorizationUtil.getRoleName(roleId);
int roleId = AuthorizationUtil.getRoleId("Administrator");

// Check if blocked
AuthorizationUtil.isBlocked(user);

// Validate access
AuthorizationUtil.validateAdminAccess(user);
```

---

## 🗂️ Key Files

| File | Purpose |
|------|---------|
| `AdminController.java` | REST endpoints |
| `AdminService.java` | Business logic |
| `DaneFinansoweRepository.java` | Financial queries |
| `KonfiguracijaRepository.java` | Config queries |
| `AuthorizationUtil.java` | Authorization helpers |
| `application.properties` | Configuration |

---

## 🐛 HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Success |
| 400 | Bad Request - Invalid input |
| 404 | Not Found - Resource missing |
| 500 | Server Error |

---

## 📋 Default Configuration Parameters

| Parameter | Value | Type |
|-----------|-------|------|
| THEME_MODE | light | STRING |
| NOTIFICATIONS_ENABLED | true | BOOLEAN |
| DEFAULT_MIN_STOCK_LEVEL | 10 | INTEGER |
| SESSION_TIMEOUT_MINUTES | 30 | INTEGER |
| TAX_RATE | 23.00 | DECIMAL |
| CURRENCY | PLN | STRING |

---

## 🚀 Usage Examples

### List All Users
```bash
curl -X GET http://localhost:8080/api/admin/users
```

### Get User by ID
```bash
curl -X GET http://localhost:8080/api/admin/users/1
```

### Block User
```bash
curl -X POST http://localhost:8080/api/admin/users/5/block
```

### Change User Role
```bash
curl -X PUT http://localhost:8080/api/admin/users/3/role/1
```

### Get Monthly Revenue
```bash
curl -X GET http://localhost:8080/api/admin/financial/revenue-month
```

### Get Role Statistics
```bash
curl -X GET http://localhost:8080/api/admin/roles/statistics
```

### Get Dashboard
```bash
curl -X GET http://localhost:8080/api/admin/dashboard
```

### List All Configurations
```bash
curl -X GET http://localhost:8080/api/admin/configuration
```

### Get Specific Configuration
```bash
curl -X GET http://localhost:8080/api/admin/configuration/THEME_MODE
```

---

## 💾 Database Tables

### dane_finansowe
- `id` (PK)
- `data` - Transaction date
- `przychody` - Revenue
- `wydatki` - Expenses
- `zysk` - Profit (calculated)
- `typ` - Transaction type
- `id_zamowienia` - Order reference

### konfiguracja_systemu
- `id` (PK)
- `nazwa_parametru` - Parameter name (UNIQUE)
- `wartosc_parametru` - Parameter value
- `typ_parametru` - Data type
- `opis` - Description
- `aktywna` - Is active

---

## 🔐 Security Notes

⚠️ **Before Production:**
- [ ] Add authentication middleware
- [ ] Enable HTTPS
- [ ] Implement rate limiting
- [ ] Hash passwords with bcrypt
- [ ] Use environment variables for secrets
- [ ] Add CORS configuration
- [ ] Implement audit logging
- [ ] Add input sanitization

---

## 📚 Documentation

- 📖 [Full API Documentation](./ADMIN_API_DOCUMENTATION.md)
- 📖 [Setup Guide](./ADMIN_BACKEND_README.md)
- 📖 [Implementation Summary](./IMPLEMENTATION_SUMMARY.md)
- 📋 [Initial Configuration](./initial_configuration.sql)

---

## ❓ Troubleshooting

| Problem | Solution |
|---------|----------|
| Build fails | `./gradlew clean build --refresh-dependencies` |
| DB connection error | Check PostgreSQL running, verify credentials |
| Port in use | Change port in `application.properties` |
| Validation error | Check request body format matches DTO |
| 404 Not Found | Verify endpoint path and HTTP method |

---

**Quick Reference Version: 1.0**  
**Last Updated: May 2026**  
**For Full Documentation: See ADMIN_API_DOCUMENTATION.md**
