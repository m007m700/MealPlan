import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/Home.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/Profile.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/order-history',
      name: 'order-history',
      component: () => import('@/views/OrderHistory.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/order',
      name: 'admin-order',
      component: () => import('@/views/AdminOrder.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/dish',
      name: 'admin-dish',
      component: () => import('@/views/AdminDish.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/profile',
      name: 'admin-profile',
      component: () => import('@/views/AdminProfile.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    }
  ]
})

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()

  // 未登录且访问需要认证的页面
  if (!userStore.isLoggedIn && to.meta.requiresAuth) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }

  // 已登录访问登录页，根据角色重定向
  if (userStore.isLoggedIn && to.path === '/login') {
    next(userStore.isAdmin ? '/admin/order' : '/')
    return
  }

  // 非管理员访问管理员页面
  if (to.meta.requiresAdmin && !userStore.isAdmin) {
    next('/')
    return
  }

  // 管理员访问普通用户页面，重定向到管理页
  if (userStore.isAdmin && !to.meta.requiresAdmin && to.path !== '/login') {
    next('/admin/order')
    return
  }

  next()
})

export default router
