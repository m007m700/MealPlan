CREATE DATABASE IF NOT EXISTS mealplan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mealplan;

CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(50),
  `avatar` VARCHAR(255),
  `role` ENUM('ADMIN', 'USER') DEFAULT 'USER',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `image_url` VARCHAR(255),
  `description` VARCHAR(500),
  `status` TINYINT DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `meal_plan` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `meal_date` DATE NOT NULL,
  `meal_type` ENUM('BREAKFAST', 'LUNCH', 'DINNER') NOT NULL,
  `status` ENUM('DRAFT', 'PUBLISHED', 'FINISHED') DEFAULT 'DRAFT',
  `admin_reply` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_meal_date_type` (`meal_date`, `meal_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `meal_plan_dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `meal_plan_id` BIGINT NOT NULL,
  `dish_id` BIGINT NOT NULL,
  FOREIGN KEY (`meal_plan_id`) REFERENCES `meal_plan`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`dish_id`) REFERENCES `dish`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `order_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `meal_plan_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `dish_id` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`meal_plan_id`) REFERENCES `meal_plan`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`dish_id`) REFERENCES `dish`(`id`) ON DELETE CASCADE,
  UNIQUE KEY `uk_user_meal_dish` (`user_id`, `meal_plan_id`, `dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `notice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `content` TEXT NOT NULL,
  `sender_id` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '管理员', 'ADMIN'),
('user', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '普通用户', 'USER');

-- 今日菜单测试数据
INSERT INTO `dish` (`name`, `image_url`, `description`) VALUES
('宫保鸡丁', 'https://placehold.co/400x300', '经典川菜，鸡肉配花生'),
('番茄炒蛋', 'https://placehold.co/400x300', '家常美味，营养健康'),
('红烧排骨', 'https://placehold.co/400x300', '鲜嫩入味，色泽红亮'),
('清炒时蔬', 'https://placehold.co/400x300', '新鲜时令蔬菜'),
('麻婆豆腐', 'https://placehold.co/400x300', '麻辣鲜香，下饭神器');

-- 今日午餐菜单
INSERT INTO `meal_plan` (`meal_date`, `meal_type`, `status`) VALUES
(CURDATE(), 'LUNCH', 'PUBLISHED');

-- 关联今日菜单与菜品
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`)
SELECT mp.id, d.id FROM meal_plan mp, dish d WHERE mp.meal_date = CURDATE() AND mp.meal_type = 'LUNCH';