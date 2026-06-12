# 家庭每日点餐系统 - 前端实施文档

## 目录
1. [项目概述](#1-项目概述)
2. [技术栈](#2-技术栈)
3. [目录结构](#3-目录结构)
4. [页面设计](#4-页面设计)
5. [组件设计](#5-组件设计)
6. [API封装](#6-api封装)
7. [状态管理](#7-状态管理)
8. [路由配置](#8-路由配置)
9. [WebSocket实现](#9-websocket实现)
10. [部署方案](#10-部署方案)

---

## 1. 项目概述

### 1.1 项目目标
实现一个基于Vue3的移动端家庭点餐系统前端应用，包含用户端和管理员端功能。

### 1.2 功能范围
| 用户端 | 管理员端 |
|--------|----------|
| 用户登录 | 菜品管理（CRUD） |
| 查看今日菜单 | 菜单发布 |
| 提交点餐 | 点餐统计 |
| 查看通知 | 菜单确认 |
| 查看历史记录 | 通知发布 |

---

## 2. 技术栈

| 分类 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 框架 | Vue | 3.4+ | 前端框架 |
| 构建工具 | Vite | 6.0+ | 构建工具 |
| 语言 | TypeScript | 5.0+ | 编程语言 |
| 路由 | Vue Router | 4.0+ | 路由管理 |
| 状态管理 | Pinia | 2.0+ | 状态管理 |
| UI组件 | Vant | 4.0+ | 移动端UI组件库 |
| HTTP请求 | Axios | 1.6+ | HTTP客户端 |
| 样式 | SCSS | - | CSS预处理器 |

---

## 3. 目录结构

```
frontend/
├── src/
│   ├── api/                    # API封装
│   │   ├── auth.ts             # 认证接口
│   │   ├── dish.ts             # 菜品接口
│   │   ├── meal.ts             # 菜单接口
│   │   ├── order.ts            # 点餐接口
│   │   └── notice.ts           # 通知接口
│   ├── assets/                 # 静态资源
│   │   ├── images/             # 图片资源
│   │   └── styles/             # 全局样式
│   ├── components/             # 公共组件
│   │   ├── NavBar.vue          # 导航栏
│   │   ├── DishCard.vue        # 菜品卡片
│   │   ├── OrderItem.vue       # 点餐项
│   │   └── NoticeItem.vue      # 通知项
│   ├── layouts/                # 布局组件
│   │   ├── UserLayout.vue      # 用户端布局
│   │   └── AdminLayout.vue     # 管理员端布局
│   ├── router/                 # 路由配置
│   │   └── index.ts            # 路由定义
│   ├── stores/                 # 状态管理
│   │   ├── user.ts             # 用户状态
│   │   ├── dish.ts             # 菜品状态
│   │   ├── meal.ts             # 菜单状态
│   │   └── notice.ts           # 通知状态
│   ├── utils/                  # 工具函数
│   │   ├── axios.ts            # Axios封装
│   │   ├── websocket.ts        # WebSocket封装
│   │   └── storage.ts          # 本地存储
│   ├── views/                  # 页面视图
│   │   ├── Login.vue           # 登录页
│   │   ├── Home.vue            # 用户首页
│   │   ├── Profile.vue         # 用户个人页
│   │   ├── AdminOrder.vue      # 管理员点餐统计页
│   │   ├── AdminDish.vue       # 管理员菜品管理页
│   │   └── AdminProfile.vue    # 管理员个人页
│   ├── App.vue                 # 根组件
│   ├── main.ts                 # 入口文件
│   └── style.scss              # 全局样式
├── index.html                  # HTML模板
├── package.json                # 依赖配置
├── vite.config.ts              # Vite配置
├── tsconfig.json               # TypeScript配置
└── .gitignore                  # Git忽略文件
```

---

## 4. 页面设计

### 4.1 登录页（Login.vue）

**功能**：用户登录验证

**组件结构**：
| 组件 | 说明 |
|------|------|
| VanForm | 表单容器 |
| VanField | 用户名输入框 |
| VanField | 密码输入框 |
| VanButton | 登录按钮 |

**交互流程**：
1. 用户输入用户名和密码
2. 点击登录按钮
3. 调用登录API
4. 成功后保存Token并跳转首页

---

### 4.2 用户首页（Home.vue）

**功能**：查看今日菜单、提交点餐、查看通知

**组件结构**：
| 组件 | 说明 |
|------|------|
| VanNavBar | 导航栏 |
| VanCard | 菜单区域 |
| DishCard | 菜品卡片（多选） |
| VanButton | 提交点餐按钮 |
| VanNoticeBar | 通知横幅 |

**交互流程**：
1. 加载今日菜单
2. 用户选择菜品（可多选）
3. 点击提交按钮
4. 调用点餐API
5. 显示成功提示

---

### 4.3 用户个人页（Profile.vue）

**功能**：个人信息、历史记录、退出登录

**组件结构**：
| 组件 | 说明 |
|------|------|
| VanCell | 用户信息展示 |
| VanCell | 历史点餐入口 |
| VanCell | 退出登录 |
| VanActionSheet | 退出确认弹窗 |

---

### 4.4 管理员点餐统计页（AdminOrder.vue）

**功能**：查看点餐统计、确认菜单

**组件结构**：
| 组件 | 说明 |
|------|------|
| VanNavBar | 导航栏 |
| VanCard | 统计卡片 |
| VanList | 用户点餐列表 |
| VanButton | 确认菜单按钮 |
| VanButton | 发送通知按钮 |

**交互流程**：
1. 加载点餐统计数据
2. 展示各用户点餐情况
3. 展示菜品票数统计
4. 管理员确认最终菜单
5. 调用确认API并推送通知

---

### 4.5 管理员菜品管理页（AdminDish.vue）

**功能**：菜品CRUD操作

**组件结构**：
| 组件 | 说明 |
|------|------|
| VanNavBar | 导航栏 |
| VanButton | 新增菜品按钮 |
| VanList | 菜品列表 |
| VanPopup | 新增/编辑弹窗 |
| VanField | 菜品名称输入 |
| VanField | 菜品描述输入 |

**交互流程**：
1. 加载菜品列表
2. 点击新增/编辑/删除
3. 调用对应API
4. 刷新列表

---

### 4.6 管理员个人页（AdminProfile.vue）

**功能**：管理员资料、家庭成员管理、退出登录

**组件结构**：
| 组件 | 说明 |
|------|------|
| VanCell | 管理员信息 |
| VanCell | 家庭成员管理入口 |
| VanCell | 退出登录 |

---

## 5. 组件设计

### 5.1 NavBar.vue

**功能**：通用导航栏组件

**Props**：
| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| title | string | '' | 标题 |
| showBack | boolean | false | 是否显示返回 |

**Emits**：
| 事件 | 参数 | 说明 |
|------|------|------|
| back | - | 返回上一页 |

---

### 5.2 DishCard.vue

**功能**：菜品卡片组件

**Props**：
| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| dish | Dish | - | 菜品信息 |
| selected | boolean | false | 是否选中 |
| multiple | boolean | true | 是否支持多选 |

**Emits**：
| 事件 | 参数 | 说明 |
|------|------|------|
| select | dish | 选中菜品 |

---

### 5.3 OrderItem.vue

**功能**：点餐项组件

**Props**：
| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| order | OrderRecord | - | 点餐记录 |
| userName | string | '' | 用户名 |

---

### 5.4 NoticeItem.vue

**功能**：通知项组件

**Props**：
| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| notice | Notice | - | 通知信息 |

---

## 6. API封装

### 6.1 Axios封装（utils/axios.ts）

**配置说明**：
- baseURL：后端API地址
- timeout：10000ms
- 请求拦截：添加Token到请求头
- 响应拦截：统一处理错误

**代码结构**：
```typescript
import axios from 'axios'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000
})

// 请求拦截器
instance.interceptors.request.use(...)

// 响应拦截器
instance.interceptors.response.use(...)

export default instance
```

---

### 6.2 认证接口（api/auth.ts）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |

**请求参数**：
```typescript
interface LoginRequest {
  username: string
  password: string
}
```

**响应参数**：
```typescript
interface LoginResponse {
  token: string
  role: 'ADMIN' | 'USER'
  nickname: string
}
```

---

### 6.3 菜品接口（api/dish.ts）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/dish/list | 获取菜品列表 |
| POST | /api/dish/add | 新增菜品 |
| PUT | /api/dish/update | 修改菜品 |
| DELETE | /api/dish/delete/{id} | 删除菜品 |

**数据结构**：
```typescript
interface Dish {
  id: number
  name: string
  imageUrl?: string
  description?: string
  status: number
  createTime: string
}
```

---

### 6.4 菜单接口（api/meal.ts）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/meal/today | 获取今日菜单 |
| POST | /api/meal/publish | 发布菜单 |
| GET | /api/meal/list | 获取历史菜单 |

**数据结构**：
```typescript
interface MealPlan {
  id: number
  mealDate: string
  mealType: 'BREAKFAST' | 'LUNCH' | 'DINNER'
  status: 'DRAFT' | 'PUBLISHED' | 'FINISHED'
  dishes: Dish[]
}
```

---

### 6.5 点餐接口（api/order.ts）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/order/submit | 提交点餐 |
| GET | /api/admin/statistics | 获取统计结果 |
| POST | /api/admin/confirm | 确认菜单 |

**数据结构**：
```typescript
interface OrderRecord {
  id: number
  mealPlanId: number
  userId: number
  dishId: number
  createTime: string
}

interface OrderStatistics {
  dishId: number
  dishName: string
  count: number
}
```

---

### 6.6 通知接口（api/notice.ts）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/notice/list | 获取通知列表 |
| POST | /api/notice/add | 发布通知 |

**数据结构**：
```typescript
interface Notice {
  id: number
  title: string
  content: string
  senderId: number
  createTime: string
}
```

---

## 7. 状态管理

### 7.1 用户状态（stores/user.ts）

**状态**：
| 属性 | 类型 | 说明 |
|------|------|------|
| token | string | 用户Token |
| role | string | 用户角色 |
| nickname | string | 用户昵称 |
| isLoggedIn | boolean | 是否登录 |

**Actions**：
| 方法 | 说明 |
|------|------|
| login | 用户登录 |
| logout | 用户退出 |
| setUserInfo | 设置用户信息 |

---

### 7.2 菜品状态（stores/dish.ts）

**状态**：
| 属性 | 类型 | 说明 |
|------|------|------|
| dishes | Dish[] | 菜品列表 |
| loading | boolean | 加载状态 |

**Actions**：
| 方法 | 说明 |
|------|------|
| fetchDishes | 获取菜品列表 |
| addDish | 新增菜品 |
| updateDish | 修改菜品 |
| deleteDish | 删除菜品 |

---

### 7.3 菜单状态（stores/meal.ts）

**状态**：
| 属性 | 类型 | 说明 |
|------|------|------|
| todayMeal | MealPlan | 今日菜单 |
| selectedDishes | number[] | 用户选中的菜品ID |
| statistics | OrderStatistics[] | 点餐统计 |

**Actions**：
| 方法 | 说明 |
|------|------|
| fetchTodayMeal | 获取今日菜单 |
| submitOrder | 提交点餐 |
| fetchStatistics | 获取统计结果 |
| confirmMenu | 确认菜单 |

---

### 7.4 通知状态（stores/notice.ts）

**状态**：
| 属性 | 类型 | 说明 |
|------|------|------|
| notices | Notice[] | 通知列表 |
| unreadCount | number | 未读数量 |

**Actions**：
| 方法 | 说明 |
|------|------|
| fetchNotices | 获取通知列表 |
| addNotice | 添加通知 |
| markAllRead | 标记全部已读 |

---

## 8. 路由配置

### 8.1 路由定义（router/index.ts）

**用户端路由**：
| 路径 | 组件 | 名称 | 权限 |
|------|------|------|------|
| /login | Login.vue | login | 无 |
| / | Home.vue | home | USER |
| /profile | Profile.vue | profile | USER |

**管理员端路由**：
| 路径 | 组件 | 名称 | 权限 |
|------|------|------|------|
| /admin/order | AdminOrder.vue | admin-order | ADMIN |
| /admin/dish | AdminDish.vue | admin-dish | ADMIN |
| /admin/profile | AdminProfile.vue | admin-profile | ADMIN |

### 8.2 路由守卫

**全局守卫**：
- 未登录用户访问需要权限的页面，重定向到登录页
- 根据用户角色限制访问对应页面

---

## 9. WebSocket实现

### 9.1 封装（utils/websocket.ts）

**功能**：
- 建立WebSocket连接
- 自动重连机制
- 消息监听与分发

**消息类型**：
| 类型 | 说明 |
|------|------|
| ORDER_SUBMIT | 用户提交点餐 |
| MENU_CONFIRM | 管理员确认菜单 |
| NOTICE | 系统通知 |

**代码结构**：
```typescript
class WebSocketClient {
  private ws: WebSocket | null = null
  private url: string = ''
  private reconnectDelay: number = 3000

  connect(url: string): void
  disconnect(): void
  sendMessage(type: string, data: any): void
  onMessage(callback: (type: string, data: any) => void): void
}
```

---

## 10. 部署方案

### 10.1 构建命令

```bash
npm run build
```

### 10.2 部署目标

**GitHub Pages**：
- 将构建产物部署到GitHub Pages
- 配置自定义域名（可选）

### 10.3 环境变量

**开发环境**：
```env
VITE_API_URL=http://localhost:8080
```

**生产环境**：
```env
VITE_API_URL=https://your-backend-url
```

---

## 附录：类型定义

```typescript
// 用户角色
type UserRole = 'ADMIN' | 'USER'

// 餐型
type MealType = 'BREAKFAST' | 'LUNCH' | 'DINNER'

// 菜单状态
type MealStatus = 'DRAFT' | 'PUBLISHED' | 'FINISHED'
```
