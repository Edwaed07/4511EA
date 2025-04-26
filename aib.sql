-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2025-04-17 14:55:09
-- 伺服器版本： 10.4.32-MariaDB
-- PHP 版本： 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `4511_db`
--

-- --------------------------------------------------------

--
-- 資料表結構 `employees`
--

CREATE TABLE `employees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL UNIQUE,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('Shop Staff','Warehouse Staff','Senior Management') NOT NULL,
  `branch` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `shops`
--

CREATE TABLE `shops` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branch` varchar(50) NOT NULL UNIQUE,
  `name` varchar(100) NOT NULL,
  `source_city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `warehouses`
--

CREATE TABLE `warehouses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `type` enum('Source','Central','Local') NOT NULL,
  `city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `fruits`
--

CREATE TABLE `fruits` (
  `fruit_id` int(11) NOT NULL AUTO_INCREMENT,
  `fruit_name` varchar(100) NOT NULL,
  `source_location` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`fruit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `branch_inventory`
--

CREATE TABLE `branch_inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branch` varchar(50) NOT NULL,
  `fruit_name` varchar(100) NOT NULL,
  `source_city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `stock_level` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `branch_fruit` (`branch`, `fruit_name`, `source_city`),
  FOREIGN KEY (`branch`) REFERENCES `shops` (`branch`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `warehouse_inventory`
--

CREATE TABLE `warehouse_inventory` (
  `inventory_id` int(11) NOT NULL AUTO_INCREMENT,
  `fruit_id` int(11) NOT NULL,
  `warehouse_type` enum('Source','Central','Local') NOT NULL,
  `stock_level` int(11) NOT NULL DEFAULT 0,
  `last_updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`inventory_id`),
  UNIQUE KEY `fruit_warehouse` (`fruit_id`, `warehouse_type`),
  FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`fruit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `borrow_records`
--

CREATE TABLE `borrow_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `borrow_branch` varchar(50) NOT NULL,
  `lender_branch` varchar(50) NOT NULL,
  `fruit_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `status` enum('PENDING','DELIVERY','COMPLETED','REJECTED') NOT NULL DEFAULT 'PENDING',
  `borrow_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`borrow_branch`) REFERENCES `shops` (`branch`),
  FOREIGN KEY (`lender_branch`) REFERENCES `shops` (`branch`),
  FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`fruit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `reserve_records`
--

CREATE TABLE `reserve_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branch` varchar(50) NOT NULL,
  `fruit_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `source_city` varchar(100) NOT NULL,
  `status` enum('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
  `reserve_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`branch`) REFERENCES `shops` (`branch`),
  FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`fruit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `country_needs`
--

CREATE TABLE `country_needs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(100) NOT NULL,
  `fruit_id` int(11) NOT NULL,
  `total_quantity` int(11) NOT NULL,
  `request_count` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `status` enum('Pending','Approved','Rejected') NOT NULL DEFAULT 'Pending',
  `delivery_status` enum('Not Started','Shipped','Delivered') NOT NULL DEFAULT 'Not Started',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`fruit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `shop_requests`
--

CREATE TABLE `shop_requests` (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `fruit_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `need_id` int(11) NOT NULL,
  `request_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`request_id`),
  FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
  FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`fruit_id`),
  FOREIGN KEY (`need_id`) REFERENCES `country_needs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 資料表結構 `deliveries`
--

CREATE TABLE `deliveries` (
  `delivery_id` int(11) NOT NULL AUTO_INCREMENT,
  `source_location` varchar(100) NOT NULL,
  `destination_location` varchar(100) NOT NULL,
  `fruit_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `need_id` int(11) DEFAULT NULL,
  `status` enum('Pending','In Transit','Delivered') NOT NULL DEFAULT 'Pending',
  `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `ship_date` timestamp NULL DEFAULT NULL,
  `delivery_date` timestamp NULL DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `created_by` int(11) NOT NULL,
  PRIMARY KEY (`delivery_id`),
  FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`fruit_id`),
  FOREIGN KEY (`need_id`) REFERENCES `country_needs` (`id`),
  FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 添加測試數據
--

-- 添加商店
INSERT INTO `shops` (`branch`, `name`, `source_city`, `country`) VALUES
('Branch1', 'Shop Branch 1', 'Tokyo', 'Japan'),
('Branch2', 'Shop Branch 2', 'New York', 'USA'),
('Branch3', 'Shop Branch 3', 'Hong Kong', 'Hong Kong');

-- 添加倉庫
INSERT INTO `warehouses` (`name`, `type`, `city`, `country`) VALUES
('Tokyo Source Warehouse', 'Source', 'Tokyo', 'Japan'),
('New York Source Warehouse', 'Source', 'New York', 'USA'),
('Hong Kong Source Warehouse', 'Source', 'Hong Kong', 'Hong Kong'),
('Tokyo Central Warehouse', 'Central', 'Tokyo', 'Japan'),
('New York Central Warehouse', 'Central', 'New York', 'USA'),
('Hong Kong Central Warehouse', 'Central', 'Hong Kong', 'Hong Kong');

-- 添加員工
INSERT INTO `employees` (`email`, `name`, `password`, `role`, `branch`) VALUES
('shop1@test.com', 'Shop Staff 1', 'password123', 'Shop Staff', 'Branch1'),
('shop2@test.com', 'Shop Staff 2', 'password123', 'Shop Staff', 'Branch2'),
('shop3@test.com', 'Shop Staff 3', 'password123', 'Shop Staff', 'Branch3'),
('warehouse1@test.com', 'Warehouse Staff 1', 'password123', 'Warehouse Staff', NULL),
('warehouse2@test.com', 'Warehouse Staff 2', 'password123', 'Warehouse Staff', NULL),
('admin@test.com', 'Admin User', 'password123', 'Senior Management', NULL);

-- 添加水果
INSERT INTO `fruits` (`fruit_name`, `source_location`, `country`) VALUES
('Apple', 'Tokyo', 'Japan'),
('Orange', 'New York', 'USA'),
('Banana', 'Hong Kong', 'Hong Kong');

-- 添加分店庫存
INSERT INTO `branch_inventory` (`branch`, `fruit_name`, `source_city`, `country`, `stock_level`) VALUES
('Branch1', 'Apple', 'Tokyo', 'Japan', 100),
('Branch1', 'Orange', 'New York', 'USA', 50),
('Branch2', 'Orange', 'New York', 'USA', 75),
('Branch2', 'Banana', 'Hong Kong', 'Hong Kong', 60),
('Branch3', 'Banana', 'Hong Kong', 'Hong Kong', 80),
('Branch3', 'Apple', 'Tokyo', 'Japan', 40);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
