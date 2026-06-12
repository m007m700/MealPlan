# 家庭每日点餐系统 - 后端实施文档

## 目录
1. [项目概述](#1-项目概述)
2. [技术栈](#2-技术栈)
3. [目录结构](#3-目录结构)
4. [数据库设计](#4-数据库设计)
5. [实体类设计](#5-实体类设计)
6. [Mapper层设计](#6-mapper层设计)
7. [Service层设计](#7-service层设计)
8. [Controller层设计](#8-controller层设计)
9. [WebSocket设计](#9-websocket设计)
10. [安全与权限](#10-安全与权限)
11. [部署方案](#11-部署方案)

---

## 1. 项目概述

### 1.1 项目目标
实现一个基于Spring Boot 3的后端服务，提供用户认证、菜品管理、菜单管理、点餐统计等功能，并支持WebSocket实时通知。

### 1.2 功能范围
| 模块 | 功能 |
|------|------|
| 用户认证 | JWT登录、Token验证 |
| 菜品管理 | 菜品CRUD |
| 菜单管理 | 菜单发布、查询 |
| 点餐管理 | 点餐提交、统计、确认 |
| 通知管理 | 系统通知发布、查询 |
| 实时通信 | WebSocket消息推送 |

---

## 2. 技术栈

| 分类 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 框架 | Spring Boot | 3.2+ | 后端框架 |
| 语言 | Java | 21 | 编程语言 |
| 数据库 | MySQL | 8.0+ | 关系型数据库 |
| ORM | MyBatis Plus | 3.5+ | ORM框架 |
| 认证 | JWT | - | Token认证 |
| 实时通信 | WebSocket | - | 实时消息推送 |
| JSON处理 | Jackson | - | JSON序列化 |

---

## 3. 目录结构

```
backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/mealplan/
│       │       ├── MealPlanApplication.java  # 启动类
│       │       ├── controller/               # 控制层
│       │       │   ├── AuthController.java
│       │       │   ├── DishController.java
│       │       │   ├── MealController.java
│       │       │   ├── OrderController.java
│       │       │   └── NoticeController.java
│       │       ├── service/                  # 服务层接口
│       │       │   ├── UserService.java
│       │       │   ├── DishService.java
│       │       │   ├── MealPlanService.java
│       │       │   ├── OrderService.java
│       │       │   └── NoticeService.java
│       │       ├── service/impl/             # 服务层实现
│       │       │   ├── UserServiceImpl.java
│       │       │   ├── DishServiceImpl.java
│       │       │   ├── MealPlanServiceImpl.java
│       │       │   ├── OrderServiceImpl.java
│       │       │   └── NoticeServiceImpl.java
│       │       ├── mapper/                   # 数据访问层
│       │       │   ├── UserMapper.java
│       │       │   ├── DishMapper.java
│       │       │   ├── MealPlanMapper.java
│       │       │   ├── MealPlanDishMapper.java
│       │       │   ├── OrderRecordMapper.java
│       │       │   └── NoticeMapper.java
│       │       ├── entity/                   # 实体类
│       │       │   ├── User.java
│       │       │   ├── Dish.java
│       │       │   ├── MealPlan.java
│       │       │   ├── MealPlanDish.java
│       │       │   ├── OrderRecord.java
│       │       │   └── Notice.java
│       │       ├── dto/                      # 数据传输对象
│       │       │   ├── request/              # 请求DTO
│       │       │   │   ├── LoginRequest.java
│       │       │   │   ├── DishAddRequest.java
│       │       │   │   ├── MealPublishRequest.java
│       │       │   │   ├── OrderSubmitRequest.java
│       │       │   │   └── NoticeAddRequest.java
│       │       │   └── response/             # 响应DTO
│       │       │       ├── LoginResponse.java
│       │       │       ├── DishResponse.java
│       │       │       ├── MealPlanResponse.java
│       │       │       ├── OrderStatisticsResponse.java
│       │       │       └── NoticeResponse.java
│       │       ├── config/                   # 配置类
│       │       │   ├── WebSocketConfig.java
│       │       │   ├── SecurityConfig.java
│       │       │   ├── CorsConfig.java
│       │       │   └── MyBatisPlusConfig.java
│       │       ├── filter/                   # 过滤器
│       │       │   └── JwtAuthenticationFilter.java
│       │       ├── handler/                  # 异常处理
│       │       │   └── GlobalExceptionHandler.java
│       │       ├── websocket/                # WebSocket
│       │       │   ├── WebSocketServer.java
│       │       │   ├── WebSocketHandler.java
│       │       │   └── WebSocketMessage.java
│       │       ├── common/                   # 公共类
│       │       │   ├── Result.java           # 统一响应封装
│       │       │   ├── ResultCode.java       # 响应码枚举
│       │       │   └── UserRole.java         # 用户角色枚举
│       │       └── utils/                    # 工具类
│       │           ├── JwtUtil.java          # JWT工具类
│       │           └── PasswordUtil.java     # 密码工具类
│       └── resources/
│           ├── application.yml               # 应用配置
│           └── schema.sql                    # 数据库初始化脚本
└── pom.xml                                   # Maven配置
```

---

## 4. 数据库设计

### 4.1 用户表（user）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | NOT NULL UNIQUE | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码（加密） |
| nickname | VARCHAR(50) | - | 昵称 |
| avatar | VARCHAR(255) | - | 头像URL |
| role | ENUM('ADMIN','USER') | DEFAULT 'USER' | 角色 |
| create_time | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 4.2 菜品表（dish）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY AUTO_INCREMENT | 菜品ID |
| name | VARCHAR(100) | NOT NULL | 菜品名称 |
| image_url | VARCHAR(255) | - | 图片URL |
| description | VARCHAR(500) | - | 描述 |
| status | TINYINT | DEFAULT 1 | 状态（1启用，0禁用） |
| create_time | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 4.3 每日菜单表（meal_plan）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY AUTO_INCREMENT | 菜单ID |
| meal_date | DATE | NOT NULL | 日期 |
| meal_type | ENUM('BREAKFAST','LUNCH','DINNER') | NOT NULL | 餐型 |
| status | ENUM('DRAFT','PUBLISHED','FINISHED') | DEFAULT 'DRAFT' | 状态 |
| create_time | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 4.4 菜单菜品关联表（meal_plan_dish）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY AUTO_INCREMENT | 关联ID |
| meal_plan_id | BIGINT | NOT NULL | 菜单ID |
| dish_id | BIGINT | NOT NULL | 菜品ID |

### 4.5 点餐记录表（order_record）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY AUTO_INCREMENT | 记录ID |
| meal_plan_id | BIGINT | NOT NULL | 菜单ID |
| user_id | BIGINT | NOT NULL | 用户ID |
| dish_id | BIGINT | NOT NULL | 菜品ID |
| create_time | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 4.6 通知表（notice）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY AUTO_INCREMENT | 通知ID |
| title | VARCHAR(200) | NOT NULL | 标题 |
| content | TEXT | NOT NULL | 内容 |
| sender_id | BIGINT | NOT NULL | 发送者ID |
| create_time | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 5. 实体类设计

### 5.1 User.java

```java
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("nickname")
    private String nickname;
    
    @TableField("avatar")
    private String avatar;
    
    @TableField("role")
    private UserRole role;
    
    @TableField("create_time")
    private LocalDateTime createTime;
}
```

### 5.2 Dish.java

```java
@Data
@TableName("dish")
public class Dish {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("name")
    private String name;
    
    @TableField("image_url")
    private String imageUrl;
    
    @TableField("description")
    private String description;
    
    @TableField("status")
    private Integer status;
    
    @TableField("create_time")
    private LocalDateTime createTime;
}
```

### 5.3 MealPlan.java

```java
@Data
@TableName("meal_plan")
public class MealPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("meal_date")
    private LocalDate mealDate;
    
    @TableField("meal_type")
    private MealType mealType;
    
    @TableField("status")
    private MealStatus status;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField(exist = false)
    private List<Dish> dishes;
}
```

### 5.4 MealPlanDish.java

```java
@Data
@TableName("meal_plan_dish")
public class MealPlanDish {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("meal_plan_id")
    private Long mealPlanId;
    
    @TableField("dish_id")
    private Long dishId;
}
```

### 5.5 OrderRecord.java

```java
@Data
@TableName("order_record")
public class OrderRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("meal_plan_id")
    private Long mealPlanId;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("dish_id")
    private Long dishId;
    
    @TableField("create_time")
    private LocalDateTime createTime;
}
```

### 5.6 Notice.java

```java
@Data
@TableName("notice")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("title")
    private String title;
    
    @TableField("content")
    private String content;
    
    @TableField("sender_id")
    private Long senderId;
    
    @TableField("create_time")
    private LocalDateTime createTime;
}
```

---

## 6. Mapper层设计

### 6.1 UserMapper.java

```java
public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(String username);
}
```

### 6.2 DishMapper.java

```java
public interface DishMapper extends BaseMapper<Dish> {
    List<Dish> selectAllEnabled();
}
```

### 6.3 MealPlanMapper.java

```java
public interface MealPlanMapper extends BaseMapper<MealPlan> {
    MealPlan selectTodayMeal(LocalDate date, MealType mealType);
    List<MealPlan> selectRecentMeals(int limit);
}
```

### 6.4 MealPlanDishMapper.java

```java
public interface MealPlanDishMapper extends BaseMapper<MealPlanDish> {
    List<Long> selectDishIdsByMealPlanId(Long mealPlanId);
    void deleteByMealPlanId(Long mealPlanId);
}
```

### 6.5 OrderRecordMapper.java

```java
public interface OrderRecordMapper extends BaseMapper<OrderRecord> {
    List<OrderRecord> selectByMealPlanId(Long mealPlanId);
    List<OrderStatistics> selectStatisticsByMealPlanId(Long mealPlanId);
    void deleteByMealPlanId(Long mealPlanId);
}
```

### 6.6 NoticeMapper.java

```java
public interface NoticeMapper extends BaseMapper<Notice> {
    List<Notice> selectRecentNotices(int limit);
}
```

---

## 7. Service层设计

### 7.1 UserService.java

```java
public interface UserService {
    LoginResponse login(LoginRequest request);
    User getUserById(Long id);
    User getUserByUsername(String username);
}
```

### 7.2 DishService.java

```java
public interface DishService {
    List<DishResponse> getAllDishes();
    DishResponse getDishById(Long id);
    DishResponse addDish(DishAddRequest request);
    DishResponse updateDish(Long id, DishAddRequest request);
    void deleteDish(Long id);
}
```

### 7.3 MealPlanService.java

```java
public interface MealPlanService {
    MealPlanResponse getTodayMeal(MealType mealType);
    MealPlanResponse publishMeal(MealPublishRequest request);
    List<MealPlanResponse> getRecentMeals(int limit);
}
```

### 7.4 OrderService.java

```java
public interface OrderService {
    void submitOrder(Long userId, OrderSubmitRequest request);
    List<OrderStatisticsResponse> getStatistics(Long mealPlanId);
    void confirmMenu(Long mealPlanId, List<Long> dishIds);
}
```

### 7.5 NoticeService.java

```java
public interface NoticeService {
    List<NoticeResponse> getRecentNotices(int limit);
    NoticeResponse addNotice(Long senderId, NoticeAddRequest request);
}
```

---

## 8. Controller层设计

### 8.1 AuthController.java

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |

### 8.2 DishController.java

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/dish/list | 获取菜品列表 |
| GET | /api/dish/{id} | 获取菜品详情 |
| POST | /api/dish/add | 新增菜品 |
| PUT | /api/dish/update/{id} | 修改菜品 |
| DELETE | /api/dish/delete/{id} | 删除菜品 |

### 8.3 MealController.java

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/meal/today | 获取今日菜单 |
| GET | /api/meal/list | 获取历史菜单 |
| POST | /api/meal/publish | 发布菜单 |

### 8.4 OrderController.java

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/order/submit | 提交点餐 |
| GET | /api/admin/statistics | 获取点餐统计 |
| POST | /api/admin/confirm | 确认菜单 |

### 8.5 NoticeController.java

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/notice/list | 获取通知列表 |
| POST | /api/notice/add | 发布通知 |

---

## 9. WebSocket设计

### 9.1 连接地址

```
ws://localhost:8080/ws
```

### 9.2 消息类型

| 类型 | 说明 | 数据结构 |
|------|------|----------|
| ORDER_SUBMIT | 用户提交点餐 | `{ userId, dishIds }` |
| MENU_CONFIRM | 管理员确认菜单 | `{ menu, dishIds }` |
| NOTICE | 系统通知 | `{ content }` |

### 9.3 WebSocketServer.java

```java
@Component
public class WebSocketServer {
    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }
    
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        // 处理客户端消息
    }
    
    public static void broadcast(String message) {
        // 广播消息给所有客户端
    }
}
```

---

## 10. 安全与权限

### 10.1 JWT配置

**Token结构**：
- Header：`{"alg": "HS256", "typ": "JWT"}`
- Payload：`{"userId": 1, "role": "USER", "exp": 过期时间}`
- Signature：HMAC-SHA256签名

**Token有效期**：
- Access Token：2小时
- Refresh Token：7天

### 10.2 权限控制

**注解使用**：
- `@PreAuthorize("hasRole('ADMIN')")`：管理员权限
- `@PreAuthorize("hasAnyRole('ADMIN', 'USER')")`：登录用户权限

**权限配置**：
| 路径 | 权限 |
|------|------|
| /api/auth/login | 公开 |
| /api/dish/list | 登录用户 |
| /api/dish/* | 管理员 |
| /api/meal/* | 登录用户 |
| /api/order/submit | 用户 |
| /api/admin/* | 管理员 |
| /api/notice/* | 登录用户 |

---

## 11. 部署方案

### 11.1 构建命令

```bash
mvn clean package
```

### 11.2 运行命令

```bash
java -jar target/mealplan-1.0.0.jar
```

### 11.3 环境变量

| 变量名 | 说明 | 示例 |
|--------|------|------|
| DB_HOST | 数据库主机 | localhost |
| DB_PORT | 数据库端口 | 3306 |
| DB_NAME | 数据库名称 | mealplan |
| DB_USERNAME | 数据库用户名 | admin |
| DB_PASSWORD | 数据库密码 | password |
| JWT_SECRET | JWT密钥 | secret-key |

### 11.4 部署目标

**Render**：
- 使用Render部署Spring Boot应用
- 配置环境变量
- 设置自动部署

---

## 附录：DTO定义

### 请求DTO

```java
// LoginRequest.java
public class LoginRequest {
    private String username;
    private String password;
}

// DishAddRequest.java
public class DishAddRequest {
    private String name;
    private String imageUrl;
    private String description;
}

// MealPublishRequest.java
public class MealPublishRequest {
    private LocalDate mealDate;
    private MealType mealType;
    private List<Long> dishIds;
}

// OrderSubmitRequest.java
public class OrderSubmitRequest {
    private Long mealPlanId;
    private List<Long> dishIds;
}

// NoticeAddRequest.java
public class NoticeAddRequest {
    private String title;
    private String content;
}
```

### 响应DTO

```java
// LoginResponse.java
public class LoginResponse {
    private String token;
    private UserRole role;
    private String nickname;
}

// DishResponse.java
public class DishResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
}

// MealPlanResponse.java
public class MealPlanResponse {
    private Long id;
    private LocalDate mealDate;
    private MealType mealType;
    private MealStatus status;
    private List<DishResponse> dishes;
}

// OrderStatisticsResponse.java
public class OrderStatisticsResponse {
    private Long dishId;
    private String dishName;
    private Integer count;
}

// NoticeResponse.java
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private String senderName;
    private LocalDateTime createTime;
}
```

### 统一响应封装

```java
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    
    public static <T> Result<T> success(T data) { ... }
    public static <T> Result<T> success(String message, T data) { ... }
    public static <T> Result<T> error(Integer code, String message) { ... }
    public static <T> Result<T> error(String message) { ... }
}
```
