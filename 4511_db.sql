-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2025-04-26 19:52:16
-- 伺服器版本： 10.4.32-MariaDB
-- PHP 版本： 8.1.25

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
-- 資料表結構 `borrow_records`
--

CREATE TABLE `borrow_records` (
  `id` int(11) NOT NULL,
  `borrow_branch` varchar(50) NOT NULL,
  `lender_branch` varchar(50) NOT NULL,
  `fruit_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `borrow_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` varchar(20) DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `borrow_records`
--

INSERT INTO `borrow_records` (`id`, `borrow_branch`, `lender_branch`, `fruit_id`, `quantity`, `borrow_date`, `status`) VALUES
(3, 'TKY1', 'TKY2', 1, 5, '2025-04-18 14:03:21', 'REJECTED'),
(8, 'TKY1', 'TKY2', 1, 4, '2025-04-18 15:46:46', 'DELIVERY'),
(9, 'TKY1', 'TKY2', 1, 4, '2025-04-18 15:47:14', 'PENDING'),
(10, 'TKY1', 'TKY2', 1, 5, '2025-04-19 15:11:53', 'PENDING'),
(11, 'TKY1', 'TKY2', 11, 5, '2025-04-19 16:15:40', 'PENDING'),
(12, 'TKY1', 'TKY2', 11, 13, '2025-04-20 13:51:16', 'DELIVERY'),
(13, 'TKY2', 'TKY1', 1, 7, '2025-04-20 13:54:29', 'REJECTED'),
(14, 'TKY1', 'TKY2', 11, 2, '2025-04-26 14:16:05', 'PENDING'),
(15, 'TKY2', 'TKY1', 1, 3, '2025-04-26 14:42:07', 'PENDING');

-- --------------------------------------------------------

--
-- 資料表結構 `branch_inventory`
--

CREATE TABLE `branch_inventory` (
  `branch` varchar(50) NOT NULL,
  `fruit_name` varchar(100) NOT NULL,
  `stock_level` int(11) NOT NULL DEFAULT 0,
  `source_city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `branch_inventory`
--

INSERT INTO `branch_inventory` (`branch`, `fruit_name`, `stock_level`, `source_city`, `country`) VALUES
('HKG1', 'Grape', 220, 'Hong Kong', 'China'),
('HKG1', 'Strawberry', 200, 'Hong Kong', 'China'),
('LAX1', 'Date', 120, 'Los Angeles', 'USA'),
('LAX1', 'Pineapple', 130, 'Los Angeles', 'USA'),
('NYC1', 'Elderberry', 160, 'New York', 'USA'),
('NYC1', 'Watermelon', 180, 'New York', 'USA'),
('OSA1', 'Banana', 100, 'Osaka', 'Japan'),
('TKY1', 'Apple', 54, 'Tokyo', 'Japan'),
('TKY1', 'Grape', 1, 'Hong Kong', 'China'),
('TKY1', 'Orange', 68, 'Tokyo', 'Japan'),
('TKY2', 'Apple', 8, 'Tokyo', 'Japan'),
('TKY2', 'Orange', 22, 'Tokyo', 'Japan');

-- --------------------------------------------------------

--
-- 資料表結構 `employees`
--

CREATE TABLE `employees` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('shopStaff','warehouseStaff','seniorManager') NOT NULL,
  `branch` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `employees`
--

INSERT INTO `employees` (`id`, `name`, `email`, `password`, `role`, `branch`) VALUES
(1, 'Alice', 'alice@4511.com', 'password1', 'shopStaff', 'TKY1'),
(2, 'Bob', 'bob@4511.com', 'password2', 'shopStaff', 'TKY2'),
(3, 'Charlie', 'charlie@4511.com', 'password3', 'shopStaff', 'OSA1'),
(4, 'David', 'david@4511.com', 'password4', 'seniorManager', NULL),
(5, 'Eve', 'eve@4511.com', 'password5', 'shopStaff', 'LAX1'),
(6, 'Frank', 'frank@4511.com', 'password6', 'warehouseStaff', NULL),
(7, 'qqq', 'd@d', 'a123', 'shopStaff', 'TKY1'),
(8, 'YIP', 'ok22@gmail.com', '123456', 'shopStaff', 'TKY2'),
(9, 'YIP', '852@gmail.com', '123', 'shopStaff', 'TKY2');

-- --------------------------------------------------------

--
-- 資料表結構 `fruits`
--

CREATE TABLE `fruits` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `source_city` varchar(100) NOT NULL,
  `stock_level` int(11) NOT NULL DEFAULT 0,
  `country` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `fruits`
--

INSERT INTO `fruits` (`id`, `name`, `source_city`, `stock_level`, `country`) VALUES
(1, 'Apple', 'Tokyo', 483, 'Japan'),
(2, 'Banana', 'Osaka', 150, 'Japan'),
(3, 'Cherry', 'Kyoto', 200, 'Japan'),
(4, 'Date', 'Los Angeles', 250, 'USA'),
(5, 'Elderberry', 'New York', 300, 'USA'),
(6, 'Fig', 'Chicago', 350, 'USA'),
(7, 'Grape', 'Hong Kong', 400, 'China'),
(8, 'Honeydew', 'Kowloon', 450, 'China'),
(9, 'Kiwi', 'New Territories', 500, 'China'),
(10, 'Lemon', 'San Francisco', 550, 'USA'),
(11, 'Orange', 'Tokyo', 120, 'Japan'),
(12, 'Peach', 'Tokyo', 180, 'Japan'),
(13, 'Pineapple', 'Los Angeles', 270, 'USA'),
(14, 'Strawberry', 'Hong Kong', 420, 'China'),
(15, 'Watermelon', 'New York', 360, 'USA');

-- --------------------------------------------------------

--
-- 資料表結構 `reserve_records`
--

CREATE TABLE `reserve_records` (
  `id` int(11) NOT NULL,
  `branch` varchar(50) NOT NULL,
  `fruit_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `source_city` varchar(50) NOT NULL,
  `reserve_date` datetime NOT NULL,
  `status` varchar(20) DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `reserve_records`
--

INSERT INTO `reserve_records` (`id`, `branch`, `fruit_id`, `quantity`, `source_city`, `reserve_date`, `status`) VALUES
(1, 'TKY1', 1, 5, 'Tokyo', '2025-04-20 12:17:16', 'PENDING'),
(2, 'TKY1', 1, 5, 'Tokyo', '2025-04-20 12:17:23', 'PENDING'),
(3, 'TKY1', 1, 2, 'Tokyo', '2025-04-20 12:18:10', 'PENDING'),
(4, 'TKY1', 2, 15, 'Osaka', '2025-04-20 21:52:09', 'PENDING'),
(5, 'TKY1', 1, 55, 'Tokyo', '2025-04-25 12:25:41', 'PENDING'),
(6, 'TKY1', 1, 4, 'Tokyo', '2025-04-26 22:10:44', 'PENDING');

-- --------------------------------------------------------

--
-- 資料表結構 `shops`
--

CREATE TABLE `shops` (
  `branch` varchar(50) NOT NULL,
  `source_city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `shops`
--

INSERT INTO `shops` (`branch`, `source_city`, `country`) VALUES
('CHI1', 'Chicago', 'USA'),
('HKG1', 'Hong Kong', 'China'),
('HKG2', 'Hong Kong', 'China'),
('KWN1', 'Kowloon', 'China'),
('KYO1', 'Kyoto', 'Japan'),
('LAX1', 'Los Angeles', 'USA'),
('LAX2', 'Los Angeles', 'USA'),
('NWT1', 'New Territories', 'China'),
('NYC1', 'New York', 'USA'),
('NYC2', 'New York', 'USA'),
('OSA1', 'Osaka', 'Japan'),
('SFO1', 'San Francisco', 'USA'),
('TKY1', 'Tokyo', 'Japan'),
('TKY2', 'Tokyo', 'Japan'),
('TKY3', 'Tokyo', 'Japan');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `borrow_records`
--
ALTER TABLE `borrow_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `borrow_branch` (`borrow_branch`),
  ADD KEY `lender_branch` (`lender_branch`),
  ADD KEY `fruit_id` (`fruit_id`);

--
-- 資料表索引 `branch_inventory`
--
ALTER TABLE `branch_inventory`
  ADD PRIMARY KEY (`branch`,`fruit_name`);

--
-- 資料表索引 `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`),
  ADD KEY `branch` (`branch`);

--
-- 資料表索引 `fruits`
--
ALTER TABLE `fruits`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `reserve_records`
--
ALTER TABLE `reserve_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fruit_id` (`fruit_id`);

--
-- 資料表索引 `shops`
--
ALTER TABLE `shops`
  ADD PRIMARY KEY (`branch`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `borrow_records`
--
ALTER TABLE `borrow_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `employees`
--
ALTER TABLE `employees`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `fruits`
--
ALTER TABLE `fruits`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `reserve_records`
--
ALTER TABLE `reserve_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `borrow_records`
--
ALTER TABLE `borrow_records`
  ADD CONSTRAINT `borrow_records_ibfk_1` FOREIGN KEY (`borrow_branch`) REFERENCES `shops` (`branch`),
  ADD CONSTRAINT `borrow_records_ibfk_2` FOREIGN KEY (`lender_branch`) REFERENCES `shops` (`branch`),
  ADD CONSTRAINT `borrow_records_ibfk_3` FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`id`);

--
-- 資料表的限制式 `branch_inventory`
--
ALTER TABLE `branch_inventory`
  ADD CONSTRAINT `branch_inventory_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `shops` (`branch`);

--
-- 資料表的限制式 `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `shops` (`branch`);

--
-- 資料表的限制式 `reserve_records`
--
ALTER TABLE `reserve_records`
  ADD CONSTRAINT `reserve_records_ibfk_1` FOREIGN KEY (`fruit_id`) REFERENCES `fruits` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
