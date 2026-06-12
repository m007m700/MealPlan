# 家庭每日点餐系统（Family Meal Planner）

## 1. 项目概述

### 1.1 项目背景

家庭成员每天都会面临一个共同的问题：

> 今天吃什么？

做饭的人通常不知道家人想吃什么，而家庭成员也不知道今天准备做什么菜。

本项目旨在打造一个轻量级家庭点餐系统，让家庭成员提前选择想吃的菜品，做饭的人实时查看点餐情况，并确定最终菜单，从而减少沟通成本，提高家庭用餐效率。

---

### 1.2 项目目标

建立一个适用于家庭场景的移动端点餐系统，实现：

* 家庭成员提前点餐
* 做饭人员查看需求
* 自动统计热门菜品
* 确认最终菜单
* 实时通知家庭成员

---

### 1.3 MVP目标

V1.0只实现核心功能：

* 用户登录
* 菜品管理
* 每日菜单发布
* 用户点餐
* 点餐统计
* 菜单确认
* 消息通知

避免过度设计。

---

# 2. 技术架构

## 2.1 前端

| 技术         | 说明       |
| ---------- | -------- |
| Vue3       | 前端框架     |
| Vite       | 构建工具     |
| Vue Router | 路由管理     |
| Pinia      | 状态管理     |
| Axios      | HTTP请求   |
| Vant       | 移动端UI组件库 |

---

## 2.2 后端

| 技术              | 说明      |
| --------------- | ------- |
| Spring Boot 3   | 主框架     |
| Spring Security | 权限认证    |
| JWT             | Token认证 |
| MyBatis Plus    | ORM框架   |
| WebSocket       | 实时通知    |

---

## 2.3 数据库

MySQL 8.0

---

## 2.4 部署方案

### 前端

GitHub Pages

### 后端

Render

### 数据库

MySQL

---

# 3. 用户角色

## 3.1 普通用户

家庭成员：

* 爸爸
* 妈妈
* 儿子
* 女儿

权限：

* 查看菜单
* 选择菜品
* 提交点餐
* 查看通知
* 查看历史记录

---

## 3.2 管理员

通常为做饭人员。

权限：

* 菜品管理
* 菜单发布
* 查看点餐统计
* 确认最终菜单
* 发布通知

支持多个管理员。

---

# 4. 业务流程

## 4.1 点餐流程

```text
管理员创建今日菜单

↓

用户查看菜单

↓

用户选择菜品

↓

提交点餐

↓

系统保存记录

↓

管理员收到通知

↓

管理员查看统计结果

↓

管理员确认最终菜单

↓

系统通知所有用户
```

---

## 4.2 菜单确认流程

```text
用户A → 红烧肉

用户B → 红烧肉

用户C → 宫保鸡丁

↓

系统统计

红烧肉（2票）

宫保鸡丁（1票）

↓

管理员确认

↓

生成最终菜单

↓

通知所有用户
```

---

# 5. 页面设计

## 5.1 用户端

### 页面：首页

功能：

* 查看今日菜单
* 选择菜品
* 提交点餐
* 查看通知

示例：

```text
今天午餐

□ 红烧肉
□ 番茄炒蛋
□ 鱼香肉丝
□ 宫保鸡丁

【提交选择】

----------------

管理员通知：

今晚18:30开饭

最终菜单：

✓ 红烧肉
✓ 番茄炒蛋
```

---

### 页面：我的

功能：

* 个人信息
* 历史点餐记录
* 退出登录

示例：

```text
头像

昵称

历史点餐

退出登录
```

---

### 用户端导航

```text
首页 | 我的
```

---

## 5.2 管理员端

### 页面：点餐统计

功能：

* 查看所有用户点餐
* 自动统计票数
* 确认菜单

示例：

```text
今日午餐

小明

✓ 红烧肉
✓ 番茄炒蛋

小红

✓ 红烧肉

爸爸

✓ 宫保鸡丁
```

统计结果：

```text
红烧肉      2票

番茄炒蛋    1票

宫保鸡丁    1票
```

操作：

```text
【确认菜单】

【发送通知】
```

---

### 页面：菜品管理

功能：

* 新增菜品
* 编辑菜品
* 删除菜品

示例：

```text
红烧肉

番茄炒蛋

鱼香肉丝

【新增菜品】
```

---

### 页面：我的

功能：

* 管理员资料
* 家庭成员管理
* 退出登录

---

### 管理员导航

```text
点餐 | 菜品 | 我的
```

---

# 6. 数据库设计

## 6.1 用户表（user）

```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    role ENUM('ADMIN','USER') DEFAULT 'USER',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 6.2 菜品表（dish）

```sql
CREATE TABLE dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    image_url VARCHAR(255),
    description VARCHAR(500),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 6.3 每日菜单表（meal_plan）

```sql
CREATE TABLE meal_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    meal_date DATE,
    meal_type ENUM(
        'BREAKFAST',
        'LUNCH',
        'DINNER'
    ),
    status ENUM(
        'DRAFT',
        'PUBLISHED',
        'FINISHED'
    ),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 6.4 菜单菜品关联表（meal_plan_dish）

```sql
CREATE TABLE meal_plan_dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    meal_plan_id BIGINT,
    dish_id BIGINT
);
```

---

## 6.5 点餐记录表（order_record）

```sql
CREATE TABLE order_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    meal_plan_id BIGINT,
    user_id BIGINT,
    dish_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 6.6 通知表（notice）

```sql
CREATE TABLE notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200),
    content TEXT,
    sender_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

# 7. API接口设计

## 用户认证

### 登录

```http
POST /api/auth/login
```

请求：

```json
{
  "username":"admin",
  "password":"123456"
}
```

响应：

```json
{
  "token":"xxxxxx",
  "role":"ADMIN"
}
```

---

# 菜品管理

## 获取菜品列表

```http
GET /api/dish/list
```

---

## 新增菜品

```http
POST /api/dish/add
```

---

## 修改菜品

```http
PUT /api/dish/update
```

---

## 删除菜品

```http
DELETE /api/dish/delete/{id}
```

---

# 菜单管理

## 获取今日菜单

```http
GET /api/meal/today
```

---

## 发布菜单

```http
POST /api/meal/publish
```

---

# 点餐管理

## 提交点餐

```http
POST /api/order/submit
```

请求：

```json
{
  "mealPlanId":1,
  "dishIds":[1,2]
}
```

---

## 获取统计结果

```http
GET /api/admin/statistics
```

---

## 确认菜单

```http
POST /api/admin/confirm
```

---

# 8. WebSocket设计

## 连接地址

```text
/ws
```

---

## 用户提交点餐

```json
{
  "type":"ORDER_SUBMIT",
  "userId":1,
  "dishIds":[1,2]
}
```

---

## 管理员确认菜单

```json
{
  "type":"MENU_CONFIRM",
  "menu":"红烧肉、番茄炒蛋"
}
```

---

## 系统通知

```json
{
  "type":"NOTICE",
  "content":"今晚18:30开饭"
}
```

---

# 9. 项目目录结构

## 前端

```text
frontend
├── src
│
├── api
├── assets
├── components
├── layouts
├── router
├── store
├── utils
│
├── views
│   ├── login
│   ├── home
│   ├── profile
│   ├── admin-order
│   └── dish-manage
│
├── App.vue
└── main.js
```

---

## 后端

```text
backend
│
├── controller
├── service
├── serviceImpl
├── mapper
├── entity
├── dto
├── vo
├── websocket
├── config
├── common
└── utils
```

---

# 10. 权限设计

## USER

可访问：

* 首页
* 提交点餐
* 我的

---

## ADMIN

可访问：

* 点餐统计
* 菜品管理
* 菜单发布
* 通知发布
* 我的

---

# 11. 开发计划

## 第一阶段

项目初始化

内容：

* Vue3初始化
* Spring Boot初始化
* MySQL配置

工期：

2天

---

## 第二阶段

登录权限模块

内容：

* JWT认证
* 用户角色

工期：

1天

---

## 第三阶段

菜品管理模块

内容：

* CRUD

工期：

2天

---

## 第四阶段

菜单管理模块

内容：

* 发布菜单
* 查看菜单

工期：

2天

---

## 第五阶段

点餐模块

内容：

* 用户点餐
* 数据统计

工期：

2天

---

## 第六阶段

WebSocket模块

内容：

* 实时通知
* 菜单确认通知

工期：

1天

---

## 总工期

10~14天

---

# 12. V2.0规划

## 菜品收藏

收藏喜欢的菜品

---

## 智能推荐

根据历史点餐记录推荐菜品

---

## 食材库存

记录家庭现有食材

---

## AI菜单生成

根据库存生成推荐菜单

---

## 自动购物清单

根据菜单自动生成采购计划

---

# 13. 项目亮点

* 家庭场景真实需求
* Vue3 + Spring Boot 全栈开发
* JWT权限控制
* WebSocket实时通信
* MySQL数据持久化
* GitHub Pages部署
* 可持续扩展AI能力

---

# 项目名称建议

中文：

* 家庭每日点餐系统
* 家庭智慧点餐
* 家庭餐食助手

英文：

* Family Meal Planner
* Home Food Planner
* Family Dinner Assistant
