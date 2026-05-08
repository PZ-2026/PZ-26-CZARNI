# Administrator Backend - API Documentation

## Overview
Admin Backend for Warehouse Management System. Provides comprehensive REST API endpoints for system administration, user management, financial reporting, and system configuration.

## Base URL
```
http://localhost:8080/api/admin
```

## Authentication
All endpoints require a valid JWT token or session. Include the token in the Authorization header:
```
Authorization: Bearer {token}
```

---

## Endpoints Reference

### 1. USER MANAGEMENT

#### 1.1 Get All Users
- **Endpoint:** `GET /api/admin/users`
- **Description:** Retrieve list of all users in the system
- **Response:** 
  ```json
  [
    {
      "id": 1,
      "imie": "John",
      "nazwisko": "Doe",
      "email": "john@example.com",
      "telefon": "123456789",
      "rola": 1,
      "firma": "Company XYZ",
      "nip": "1234567890",
      "zablokowany": false
    }
  ]
  ```
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 1.2 Get User by ID
- **Endpoint:** `GET /api/admin/users/{id}`
- **Parameters:** 
  - `id` (path): User ID (integer)
- **Response:** Single user object
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

#### 1.3 Create New User
- **Endpoint:** `POST /api/admin/users`
- **Request Body:**
  ```json
  {
    "imie": "Jane",
    "nazwisko": "Smith",
    "email": "jane@example.com",
    "telefon": "987654321",
    "rola": 2,
    "firma": "ABC Corp",
    "nip": "0987654321"
  }
  ```
- **Status Codes:** 200 (Created), 400 (Bad Request), 500 (Server Error)

#### 1.4 Update User
- **Endpoint:** `PUT /api/admin/users/{id}`
- **Parameters:** 
  - `id` (path): User ID
- **Request Body:** Same as Create
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

#### 1.5 Delete User
- **Endpoint:** `DELETE /api/admin/users/{id}`
- **Parameters:** 
  - `id` (path): User ID
- **Response:** `{"message": "Użytkownik został usunięty"}`
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

#### 1.6 Block User
- **Endpoint:** `POST /api/admin/users/{id}/block`
- **Parameters:** 
  - `id` (path): User ID
- **Response:** `{"message": "Użytkownik został zablokowany"}`
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

#### 1.7 Unblock User
- **Endpoint:** `POST /api/admin/users/{id}/unblock`
- **Parameters:** 
  - `id` (path): User ID
- **Response:** `{"message": "Użytkownik został odblokowany"}`
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

#### 1.8 Get Users by Role
- **Endpoint:** `GET /api/admin/users/role/{roleId}`
- **Parameters:** 
  - `roleId` (path): Role ID (1-4)
- **Response:** Array of users with specified role
- **Status Codes:** 200 (OK), 500 (Server Error)

---

### 2. ROLE MANAGEMENT

#### 2.1 Change User Role
- **Endpoint:** `PUT /api/admin/users/{id}/role/{newRoleId}`
- **Parameters:** 
  - `id` (path): User ID
  - `newRoleId` (path): New role ID (1-4)
- **Response:** Updated user object
- **Status Codes:** 200 (OK), 400 (Bad Request), 404 (Not Found), 500 (Server Error)

**Role IDs:**
- `1` - Administrator
- `2` - Magazynier (Warehouse Worker)
- `3` - Zaopatrzeniowiec (Supply Manager)
- `4` - Klient (Customer)

#### 2.2 Get Role Statistics
- **Endpoint:** `GET /api/admin/roles/statistics`
- **Response:**
  ```json
  {
    "administrator": 2,
    "magazynier": 5,
    "zaopatrzeniowiec": 3,
    "klient": 15
  }
  ```
- **Status Codes:** 200 (OK), 500 (Server Error)

---

### 3. FINANCIAL DATA

#### 3.1 Get Financial Report
- **Endpoint:** `GET /api/admin/financial/report`
- **Query Parameters:**
  - `dataPoczatek` (required): Start date (ISO 8601 format: `2026-05-01T00:00:00`)
  - `dataKoniec` (required): End date (ISO 8601 format: `2026-05-31T23:59:59`)
- **Response:**
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
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 3.2 Get Monthly Revenue
- **Endpoint:** `GET /api/admin/financial/revenue-month`
- **Response:** `{"przychody": 15000.50}`
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 3.3 Get Monthly Expenses
- **Endpoint:** `GET /api/admin/financial/expenses-month`
- **Response:** `{"wydatki": 8500.25}`
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 3.4 Get Monthly Profit
- **Endpoint:** `GET /api/admin/financial/profit-month`
- **Response:** `{"zysk": 6500.25}`
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 3.5 Get Financial History
- **Endpoint:** `GET /api/admin/financial/history`
- **Query Parameters:**
  - `dataPoczatek` (required): Start date
  - `dataKoniec` (required): End date
- **Response:** Array of financial records
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 3.6 Add Financial Entry
- **Endpoint:** `POST /api/admin/financial/entry`
- **Request Body:**
  ```json
  {
    "data": "2026-05-08T14:30:00",
    "przychody": 1000.00,
    "wydatki": 500.00,
    "typ": "SPRZEDAZ",
    "idZamowienia": 42
  }
  ```
- **Response:** Created financial record
- **Status Codes:** 200 (OK), 500 (Server Error)

---

### 4. SYSTEM CONFIGURATION

#### 4.1 Get All Configuration Parameters
- **Endpoint:** `GET /api/admin/configuration`
- **Response:**
  ```json
  [
    {
      "id": 1,
      "nazwaParametru": "MIN_STOCK_LEVEL",
      "wartoscParametru": "10",
      "typParametru": "INTEGER",
      "opis": "Minimalny poziom zapasów",
      "aktywna": true
    }
  ]
  ```
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 4.2 Get Active Configuration Parameters
- **Endpoint:** `GET /api/admin/configuration/active`
- **Response:** Array of active configuration parameters
- **Status Codes:** 200 (OK), 500 (Server Error)

#### 4.3 Get Configuration Parameter by Name
- **Endpoint:** `GET /api/admin/configuration/{parametr}`
- **Parameters:** 
  - `parametr` (path): Parameter name
- **Response:** Single configuration object
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

#### 4.4 Create New Configuration Parameter
- **Endpoint:** `POST /api/admin/configuration`
- **Request Body:**
  ```json
  {
    "nazwaParametru": "THEME_MODE",
    "wartoscParametru": "dark",
    "typParametru": "STRING",
    "opis": "Tryb jasny/ciemny",
    "aktywna": true
  }
  ```
- **Status Codes:** 200 (OK), 400 (Bad Request), 500 (Server Error)

#### 4.5 Update Configuration Parameter
- **Endpoint:** `PUT /api/admin/configuration/{id}`
- **Parameters:** 
  - `id` (path): Configuration ID
- **Request Body:** Same as Create
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

#### 4.6 Delete Configuration Parameter
- **Endpoint:** `DELETE /api/admin/configuration/{id}`
- **Parameters:** 
  - `id` (path): Configuration ID
- **Response:** `{"message": "Konfiguracja została usunięta"}`
- **Status Codes:** 200 (OK), 404 (Not Found), 500 (Server Error)

---

### 5. ADMIN DASHBOARD

#### 5.1 Get Dashboard Data
- **Endpoint:** `GET /api/admin/dashboard`
- **Description:** Get comprehensive dashboard data for admin panel
- **Response:**
  ```json
  {
    "liczbaUzytkownikow": 25,
    "liczbaProduktu": 150,
    "liczbaMagazynow": 3,
    "liczbaDostawow": 12,
    "przychodyMiesiac": 15000.50,
    "wydatkiMiesiac": 8500.25,
    "zyskMiesiac": 6500.25,
    "liczbaZamowienWProgress": 8,
    "liczbaZamowienDoRealizacji": 12,
    "liczbaProductowPonizejProgu": 5
  }
  ```
- **Status Codes:** 200 (OK), 500 (Server Error)

---

## Error Handling

All endpoints return appropriate HTTP status codes:

| Status Code | Meaning |
|------------|---------|
| 200 | OK - Request successful |
| 400 | Bad Request - Invalid parameters |
| 404 | Not Found - Resource not found |
| 500 | Server Error - Internal server error |

Error responses include a message:
```json
{
  "message": "Błąd: Szczegółowy opis błędu"
}
```

---

## Configuration Parameters (Examples)

### Alarm Thresholds
- **Parameter:** `MIN_STOCK_LEVEL_{PRODUCT_ID}`
- **Type:** INTEGER
- **Example:** `10`
- **Description:** Minimum stock level for product

### System Settings
- **Parameter:** `THEME_MODE`
- **Type:** STRING
- **Values:** `light`, `dark`
- **Description:** Application theme mode

- **Parameter:** `NOTIFICATION_ENABLED`
- **Type:** BOOLEAN
- **Values:** `true`, `false`
- **Description:** Enable/disable notifications

---

## Example Usage

### Create a new user and assign role
```bash
# 1. Create user
curl -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "imie": "Jan",
    "nazwisko": "Kowalski",
    "email": "jan.kowalski@example.com",
    "telefon": "123456789",
    "rola": 2
  }'

# 2. Get financial report
curl -X GET "http://localhost:8080/api/admin/financial/report?dataPoczatek=2026-05-01T00:00:00&dataKoniec=2026-05-31T23:59:59" \
  -H "Authorization: Bearer {token}"
```

---

## Role Permissions

| Feature | Administrator | Magazynier | Zaopatrzeniowiec | Klient |
|---------|---|---|---|---|
| User Management | ✓ | ✗ | ✗ | ✗ |
| Role Assignment | ✓ | ✗ | ✗ | ✗ |
| Financial Reports | ✓ | ✗ | ✗ | ✗ |
| System Configuration | ✓ | ✗ | ✗ | ✗ |
| Dashboard | ✓ | Limited | Limited | Limited |

---

## Development Notes

### Adding New Configuration Parameters
1. Use POST /api/admin/configuration endpoint
2. Provide unique `nazwaParametru`
3. Set appropriate `typParametru`: STRING, INTEGER, BOOLEAN, DECIMAL
4. Set `aktywna` to true to enable immediately

### Financial Tracking
1. Use POST /api/admin/financial/entry to add financial data
2. Financial data is calculated automatically (profit = revenue - expenses)
3. Use GET /api/admin/financial/report for period analysis

### User Blocking/Unblocking
1. Block users without deletion using POST /api/admin/users/{id}/block
2. Blocked users cannot access any system features
3. Unblock using POST /api/admin/users/{id}/unblock

---

## Integration with Frontend

### Admin Panel Requirements
The frontend admin panel should:
1. Check GET /api/admin/dashboard on load
2. Display user list from GET /api/admin/users
3. Show financial graphs from GET /api/admin/financial/report
4. Allow configuration changes via PUT /api/admin/configuration/{id}
5. Implement user CRUD operations

---

## Database Schema

### Tables Used
- `uzytkownicy` - User accounts
- `dane_finansowe` - Financial data
- `konfiguracja_systemu` - System configuration
- `produkty` - Products
- `zamowienia_klienci` - Client orders
- `stan_magazynu` - Stock levels

---

## Performance Considerations

1. **Pagination:** Consider adding pagination for large user/product lists
2. **Caching:** Cache configuration parameters (they change infrequently)
3. **Indexes:** Ensure database indexes on frequently queried fields
4. **Bulk Operations:** Implement bulk user/product import for large datasets

---

## Future Enhancements

1. **Advanced Reporting:** PDF export of financial reports
2. **Audit Logs:** Track all admin actions for compliance
3. **Two-Factor Authentication:** For admin accounts
4. **API Rate Limiting:** Prevent abuse
5. **Webhook Support:** For real-time notifications
6. **Batch Operations:** Bulk user management
7. **Advanced Analytics:** Predictive analysis of financial trends
