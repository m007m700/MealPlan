USE mealplan;

-- 清空现有测试数据（可选）
-- DELETE FROM meal_plan_dish;
-- DELETE FROM meal_plan;
-- DELETE FROM dish;

-- 插入菜品数据
INSERT IGNORE INTO `dish` (`id`, `name`, `image_url`, `description`) VALUES
(1, '宫保鸡丁', 'https://placehold.co/400x300', '经典川菜，鸡肉配花生'),
(2, '番茄炒蛋', 'https://placehold.co/400x300', '家常美味，营养健康'),
(3, '红烧排骨', 'https://placehold.co/400x300', '鲜嫩入味，色泽红亮'),
(4, '清炒时蔬', 'https://placehold.co/400x300', '新鲜时令蔬菜'),
(5, '麻婆豆腐', 'https://placehold.co/400x300', '麻辣鲜香，下饭神器');

-- 插入今日午餐菜单（使用 CURDATE() 确保是今天）
INSERT INTO `meal_plan` (`meal_date`, `meal_type`, `status`) VALUES
(CURDATE(), 'LUNCH', 'PUBLISHED')
ON DUPLICATE KEY UPDATE status = 'PUBLISHED';

-- 关联今日菜单与所有菜品
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`)
SELECT mp.id, d.id 
FROM meal_plan mp 
CROSS JOIN dish d 
WHERE mp.meal_date = CURDATE() 
  AND mp.meal_type = 'LUNCH'
  AND NOT EXISTS (
    SELECT 1 FROM meal_plan_dish mpd 
    WHERE mpd.meal_plan_id = mp.id AND mpd.dish_id = d.id
  );
