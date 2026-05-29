-- Initial Configuration Data for Admin System
-- This SQL script populates the konfiguracja_systemu table with default parameters

BEGIN;

-- Theme Configuration
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('THEME_MODE', 'light', 'STRING', 'Application theme: light or dark', true),
    ('LANGUAGE', 'pl', 'STRING', 'Default application language (pl, en, de)', true);

-- Notification Settings
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('NOTIFICATIONS_ENABLED', 'true', 'BOOLEAN', 'Enable/disable system notifications', true),
    ('EMAIL_NOTIFICATIONS', 'true', 'BOOLEAN', 'Enable/disable email notifications', true),
    ('SMS_NOTIFICATIONS', 'false', 'BOOLEAN', 'Enable/disable SMS notifications', true);

-- Stock Management
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('DEFAULT_MIN_STOCK_LEVEL', '10', 'INTEGER', 'Default minimum stock level for new products', true),
    ('STOCK_WARNING_THRESHOLD', '20', 'INTEGER', 'Stock warning threshold percentage', true),
    ('AUTO_REORDER_ENABLED', 'true', 'BOOLEAN', 'Automatic reordering when stock falls below threshold', true);

-- Financial Settings
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('CURRENCY', 'PLN', 'STRING', 'Currency for financial reports', true),
    ('DECIMAL_PLACES', '2', 'INTEGER', 'Number of decimal places for financial data', true),
    ('TAX_RATE', '23.00', 'DECIMAL', 'Default tax rate in percentage', true);

-- Report Settings
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('REPORT_RETENTION_DAYS', '365', 'INTEGER', 'Number of days to retain financial reports', true),
    ('AUTO_REPORT_GENERATION', 'true', 'BOOLEAN', 'Automatically generate monthly reports', true),
    ('REPORT_FORMAT', 'PDF', 'STRING', 'Default report format: PDF, EXCEL, CSV', true);

-- Security Settings
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('SESSION_TIMEOUT_MINUTES', '30', 'INTEGER', 'Session timeout in minutes', true),
    ('PASSWORD_MIN_LENGTH', '8', 'INTEGER', 'Minimum password length', true),
    ('PASSWORD_REQUIRE_SPECIAL_CHARS', 'true', 'BOOLEAN', 'Require special characters in passwords', true),
    ('MAX_LOGIN_ATTEMPTS', '5', 'INTEGER', 'Maximum login attempts before account lock', true);

-- Integration Settings
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('API_RATE_LIMIT', '1000', 'INTEGER', 'API requests per hour limit', true),
    ('WEBHOOK_ENABLED', 'false', 'BOOLEAN', 'Enable/disable webhook notifications', true),
    ('CORS_ENABLED', 'true', 'BOOLEAN', 'Enable/disable CORS for API', true);

-- System Maintenance
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('MAINTENANCE_MODE', 'false', 'BOOLEAN', 'Put system in maintenance mode', true),
    ('AUTO_BACKUP_ENABLED', 'true', 'BOOLEAN', 'Enable automatic database backups', true),
    ('BACKUP_SCHEDULE', '02:00', 'STRING', 'Time for automatic backups (HH:MM format)', true);

-- Warehouse Configuration
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('WAREHOUSE_NAME', 'Main Warehouse', 'STRING', 'Default warehouse name', true),
    ('ALLOW_NEGATIVE_STOCK', 'false', 'BOOLEAN', 'Allow orders when stock is negative', true),
    ('REQUIRE_BARCODE_SCAN', 'true', 'BOOLEAN', 'Require barcode scanning for all operations', true);

-- Email Configuration
INSERT INTO konfiguracja_systemu (nazwa_parametru, wartosc_parametru, typ_parametru, opis, aktywna)
VALUES 
    ('SMTP_SERVER', 'smtp.gmail.com', 'STRING', 'SMTP server address for email notifications', false),
    ('SMTP_PORT', '587', 'INTEGER', 'SMTP port', false),
    ('SENDER_EMAIL', 'noreply@magazyn.local', 'STRING', 'Email sender address', false);

COMMIT;

-- Sample Initial Financial Data
BEGIN;

-- Insert sample financial entries for current month
INSERT INTO dane_finansowe (data, przychody, wydatki, typ)
VALUES 
    (NOW() - INTERVAL '20 days', 5000.00, 2500.00, 'SPRZEDAZ'),
    (NOW() - INTERVAL '15 days', 3500.00, 1800.00, 'SPRZEDAZ'),
    (NOW() - INTERVAL '10 days', 4200.00, 2100.00, 'SPRZEDAZ'),
    (NOW() - INTERVAL '5 days', 6000.00, 3000.00, 'SPRZEDAZ');

COMMIT;
