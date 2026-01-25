-- ============================================
-- MIGRATION SCRIPT: Float to BigDecimal
-- Restaurant Management API v0.0.1-SNAPSHOT
-- Date: January 25, 2026
-- ============================================

-- IMPORTANT: Execute this script BEFORE deploying the new version
-- This migration changes Float columns to DECIMAL for monetary precision

USE restaurant_management;

-- Backup recommendations (execute before running this script):
-- mysqldump -u root -p restaurant_management > backup_before_migration_$(date +%Y%m%d_%H%M%S).sql

-- ============================================
-- 1. Modify Dish table - price column
-- ============================================
ALTER TABLE dish
MODIFY COLUMN price DECIMAL(19,2) NOT NULL
COMMENT 'Price in decimal format for precision';

-- ============================================
-- 2. Modify Orders table - total_price column
-- ============================================
ALTER TABLE orders
MODIFY COLUMN total_price DECIMAL(19,2)
COMMENT 'Total price in decimal format for precision';

-- ============================================
-- 3. Verify changes
-- ============================================
DESCRIBE dish;
DESCRIBE orders;

-- ============================================
-- 4. Update any NULL values to 0.00
-- ============================================
UPDATE orders SET total_price = 0.00 WHERE total_price IS NULL;

-- ============================================
-- ROLLBACK (if needed)
-- ============================================
-- In case you need to rollback, uncomment and execute:

-- ALTER TABLE dish MODIFY COLUMN price FLOAT;
-- ALTER TABLE orders MODIFY COLUMN total_price FLOAT;

-- ============================================
-- Verification Queries
-- ============================================
-- Run these after migration to verify:

SELECT
    'dish' as table_name,
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'restaurant_management'
  AND TABLE_NAME = 'dish'
  AND COLUMN_NAME = 'price';

SELECT
    'orders' as table_name,
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'restaurant_management'
  AND TABLE_NAME = 'orders'
  AND COLUMN_NAME = 'total_price';

-- ============================================
-- Test with sample data
-- ============================================
-- Verify that decimal precision works correctly:
-- SELECT id, name, price FROM dish LIMIT 5;
-- SELECT id, total_price FROM orders LIMIT 5;

-- ============================================
-- END OF MIGRATION SCRIPT
-- ============================================
