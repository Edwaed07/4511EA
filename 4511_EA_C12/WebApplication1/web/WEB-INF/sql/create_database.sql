-- 创建数据库
CREATE DATABASE IF NOT EXISTS aib_bakery CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE aib_bakery;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL, -- "warehouse", "shop", "management"
  location VARCHAR(100) NOT NULL, -- 国家/城市
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建水果表
CREATE TABLE IF NOT EXISTS fruits (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  source_country VARCHAR(50) NOT NULL,
  description TEXT,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建库存表
CREATE TABLE IF NOT EXISTS inventory (
  id INT AUTO_INCREMENT PRIMARY KEY,
  fruit_id INT NOT NULL,
  location_type VARCHAR(50) NOT NULL, -- "sourceWarehouse", "centralWarehouse", "shop"
  location_name VARCHAR(100) NOT NULL, -- 国家/城市/商店名称
  quantity INT NOT NULL DEFAULT 0,
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (fruit_id) REFERENCES fruits(id),
  UNIQUE KEY unique_location (fruit_id, location_type, location_name)
);

-- 创建预订表
CREATE TABLE IF NOT EXISTS reserves (
  id INT AUTO_INCREMENT PRIMARY KEY,
  fruit_id INT NOT NULL,
  user_id INT NOT NULL,
  shop_name VARCHAR(100) NOT NULL,
  city VARCHAR(50) NOT NULL,
  country VARCHAR(50) NOT NULL,
  quantity INT NOT NULL,
  reserve_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  delivery_date TIMESTAMP NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending', -- "pending", "approved", "in-transit", "delivered", "cancelled"
  FOREIGN KEY (fruit_id) REFERENCES fruits(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 创建交付表
CREATE TABLE IF NOT EXISTS deliveries (
  id INT AUTO_INCREMENT PRIMARY KEY,
  fruit_id INT NOT NULL,
  source_location VARCHAR(100) NOT NULL,
  destination_location VARCHAR(100) NOT NULL,
  destination_type VARCHAR(50) NOT NULL, -- "centralWarehouse", "shop"
  quantity INT NOT NULL,
  delivery_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) NOT NULL DEFAULT 'pending', -- "pending", "in-transit", "delivered"
  reserve_id INT DEFAULT 0, -- 0表示不关联预订
  FOREIGN KEY (fruit_id) REFERENCES fruits(id)
);

-- 插入初始管理员用户
INSERT INTO users (username, password, role, location, active)
VALUES ('admin', 'admin123', 'management', 'Global', TRUE);

-- 插入三个国家的仓库用户
INSERT INTO users (username, password, role, location, active)
VALUES ('japan_warehouse', 'japan123', 'warehouse', 'Japan', TRUE),
       ('usa_warehouse', 'usa123', 'warehouse', 'USA', TRUE),
       ('hk_warehouse', 'hk123', 'warehouse', 'Hong Kong', TRUE);

-- 插入样本水果数据
INSERT INTO fruits (name, source_country, description, active)
VALUES 
('苹果', 'Japan', '日本富士苹果，口感脆甜', TRUE),
('香蕉', 'USA', '美国有机香蕉，营养丰富', TRUE),
('橙子', 'USA', '美国纽荷尔橙子，汁多味甜', TRUE),
('草莓', 'Japan', '日本奈良草莓，香甜多汁', TRUE),
('蓝莓', 'USA', '美国野生蓝莓，抗氧化', TRUE),
('芒果', 'Hong Kong', '香港本地芒果，热带风味', TRUE),
('西瓜', 'Japan', '日本小玉西瓜，清爽可口', TRUE);

-- 初始化源仓库库存
INSERT INTO inventory (fruit_id, location_type, location_name, quantity)
VALUES 
(1, 'sourceWarehouse', 'Japan', 1000), -- 苹果在日本源仓库
(2, 'sourceWarehouse', 'USA', 1500),   -- 香蕉在美国源仓库
(3, 'sourceWarehouse', 'USA', 1200),   -- 橙子在美国源仓库
(4, 'sourceWarehouse', 'Japan', 800),  -- 草莓在日本源仓库
(5, 'sourceWarehouse', 'USA', 900),    -- 蓝莓在美国源仓库
(6, 'sourceWarehouse', 'Hong Kong', 600), -- 芒果在香港源仓库
(7, 'sourceWarehouse', 'Japan', 400);  -- 西瓜在日本源仓库

-- 初始化中央仓库库存
INSERT INTO inventory (fruit_id, location_type, location_name, quantity)
VALUES 
(1, 'centralWarehouse', 'Japan', 200),      -- 苹果在日本中央仓库
(1, 'centralWarehouse', 'USA', 150),        -- 苹果在美国中央仓库
(1, 'centralWarehouse', 'Hong Kong', 100),  -- 苹果在香港中央仓库
(2, 'centralWarehouse', 'Japan', 100),      -- 香蕉在日本中央仓库
(2, 'centralWarehouse', 'USA', 300),        -- 香蕉在美国中央仓库
(2, 'centralWarehouse', 'Hong Kong', 150),  -- 香蕉在香港中央仓库
(3, 'centralWarehouse', 'Japan', 80),       -- 橙子在日本中央仓库
(3, 'centralWarehouse', 'USA', 250),        -- 橙子在美国中央仓库
(3, 'centralWarehouse', 'Hong Kong', 120);  -- 橙子在香港中央仓库 