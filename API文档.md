# 家庭每日点餐系统 - API 接口文档

## 基本信息

| 项目 | 说明 |
|------|------|
| **Base URL** | `http://localhost:8080/api` |
| **内容类型** | `application/json` |
| **认证方式** | JWT Bearer Token |
| **时区** | Asia/Shanghai (UTC+8) |

---

## 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码（200成功，其他失败） |
| message | String | 提示信息 |
| data | Object | 响应数据 |

---

## 1. 认证接口

### 1.1 用户登录

**POST** `/auth/login`

无需认证。

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**请求示例：**
```json
{
  "username": "admin",
  "password": "password"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "role": "ADMIN",
    "nickname": "管理员",
    "userId": 1
  }
}
```

---

## 2. 菜品管理

### 2.1 获取菜品列表

**GET** `/dish/list`

需要认证。

**请求头：**
```
Authorization: Bearer {token}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "宫保鸡丁",
      "imageUrl": "https://example.com/dish1.jpg",
      "description": "经典川菜"
    }
  ]
}
```

### 2.2 获取菜品详情

**GET** `/dish/{id}`

需要认证。

**路径参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 菜品ID |

### 2.3 添加菜品

**POST** `/dish/add`

需要管理员权限。

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 菜品名称 |
| imageUrl | String | 否 | 图片地址 |
| description | String | 否 | 描述 |

### 2.4 更新菜品

**PUT** `/dish/update/{id}`

需要管理员权限。

### 2.5 删除菜品

**DELETE** `/dish/delete/{id}`

需要管理员权限（逻辑删除，status设为0）。

---

## 3. 菜单管理

### 3.1 获取今日菜单

**GET** `/meal/today`

需要认证。

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| mealType | String | 是 | 餐型：BREAKFAST/LUNCH/DINNER |

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "mealDate": "2026-06-11",
    "mealType": "LUNCH",
    "status": "PUBLISHED",
    "dishes": [
      {
        "id": 1,
        "name": "宫保鸡丁",
        "imageUrl": "...",
        "description": "..."
      }
    ]
  }
}
```

### 3.2 获取近期菜单

**GET** `/meal/list`

需要认证。

**查询参数：**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| limit | Integer | 否 | 10 | 返回数量 |

### 3.3 发布菜单

**POST** `/meal/publish`

需要管理员权限。

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| mealDate | Date | 是 | 日期（yyyy-MM-dd） |
| mealType | String | 是 | 餐型：BREAKFAST/LUNCH/DINNER |
| dishIds | Long[] | 是 | 菜品ID列表 |

**请求示例：**
```json
{
  "mealDate": "2026-06-11",
  "mealType": "LUNCH",
  "dishIds": [1, 2, 3]
}
```

---

## 4. 点餐接口

### 4.1 提交点餐

**POST** `/order/submit`

需要普通用户权限。

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| mealPlanId | Long | 是 | 菜单ID |
| dishIds | Long[] | 是 | 选择的菜品ID列表 |

**请求示例：**
```json
{
  "mealPlanId": 1,
  "dishIds": [1, 2]
}
```

---

## 5. 管理员接口

### 5.1 查看点餐统计

**GET** `/admin/statistics`

需要管理员权限。

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| mealPlanId | Long | 是 | 菜单ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "dishId": 1,
      "dishName": "宫保鸡丁",
      "count": 5
    }
  ]
}
```

### 5.2 确认菜单

**POST** `/admin/confirm`

需要管理员权限。

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| mealPlanId | Long | 是 | 菜单ID |
| body | Long[] | 是 | 确认的菜品ID列表 |

---

## 6. 通知接口

### 6.1 获取通知列表

**GET** `/notice/list`

需要认证。

**查询参数：**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| limit | Integer | 否 | 10 | 返回数量 |

### 6.2 添加通知

**POST** `/notice/add`

需要认证。

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 是 | 通知标题 |
| content | String | 是 | 通知内容 |

---

## 枚举值说明

### 角色枚举 (UserRole)

| 值 | 说明 |
|------|------|
| ADMIN | 管理员 |
| USER | 普通用户 |

### 餐型枚举 (MealType)

| 值 | 说明 |
|------|------|
| BREAKFAST | 早餐 |
| LUNCH | 午餐 |
| DINNER | 晚餐 |

### 菜单状态枚举 (MealStatus)

| 值 | 说明 |
|------|------|
| DRAFT | 草稿 |
| PUBLISHED | 已发布 |
| FINISHED | 已完成 |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 500 | 系统错误 |
| 1001 | 用户不存在 |
| 1002 | 用户已存在 |
| 1003 | 密码错误 |
| 1004 | Token无效 |
| 1005 | Token已过期 |
| 2001 | 菜品不存在 |
| 3001 | 菜单不存在 |
| 3002 | 菜单已发布 |
| 4001 | 已提交点餐 |

---

## 预置测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | password | 管理员 |
| user | password | 普通用户 |

---

## WebSocket 接口

**地址：** `ws://localhost:8080/api/ws`

### 消息类型

| 类型 | 说明 |
|------|------|
| ORDER_SUBMIT | 用户提交点餐通知 |
| MENU_CONFIRM | 管理员确认菜单通知 |
| NOTICE | 系统通知 |

---

**文档版本：** v1.0.0  
**更新日期：** 2026-06-11
