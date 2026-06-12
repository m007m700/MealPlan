<script setup lang="ts">
import { ref } from 'vue'
import { NavBar as VanNavBar, ActionSheet as VanActionSheet, showToast, Tabbar as VanTabbar, TabbarItem as VanTabbarItem } from 'vant'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const userStore = useUserStore()
const showLogout = ref(false)

function handleLogout(): void {
  showLogout.value = false
  userStore.logout()
  showToast('已退出登录')
  router.push('/login')
}

function handleTabChange(name: string): void {
  if (name === 'home') {
    router.push('/')
  } else if (name === 'history') {
    router.push('/order-history')
  }
}

function goToAdmin(): void {
  if (userStore.isAdmin) {
    router.push('/admin/order')
  }
}
</script>

<template>
  <div class="profile-page">
    <van-nav-bar title="我的" class="navbar" />

    <div class="profile-content">
      <div class="user-section">
        <div class="user-card">
          <div class="avatar-wrapper">
            <div class="avatar">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                stroke-linecap="round" stroke-linejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </div>
          </div>
          <div class="user-info">
            <h2 class="nickname">{{ userStore.nickname }}</h2>
            <p class="role">{{ userStore.role === 'ADMIN' ? '管理员' : '家庭成员' }}</p>
          </div>
          <div v-if="userStore.isAdmin" class="admin-badge" @click="goToAdmin">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path
                d="M9 19v-6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2zm0 0V9a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v10m-6 0a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2m0 0V5a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-2a2 2 0 0 1-2-2z" />
            </svg>
            <span>管理后台</span>
          </div>
        </div>
      </div>

      <div class="menu-section">
        <div class="menu-item" @click="goToAdmin" v-if="userStore.isAdmin">
          <div class="menu-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path
                d="M9 19v-6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2zm0 0V9a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v10m-6 0a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2m0 0V5a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-2a2 2 0 0 1-2-2z" />
            </svg>
          </div>
          <span class="menu-text">投票管理</span>
          <svg class="menu-arrow" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
            stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M5 12h14M12 5l7 7-7 7" />
          </svg>
        </div>

        <div class="menu-item">
          <div class="menu-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 20h9" />
              <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7.5 19l-4 1 1-4L16.5 3.5z" />
            </svg>
          </div>
          <span class="menu-text">关于我们</span>
          <svg class="menu-arrow" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
            stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M5 12h14M12 5l7 7-7 7" />
          </svg>
        </div>

        <div class="menu-item logout-item" @click="showLogout = true">
          <div class="menu-icon logout-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
              <polyline points="16 17 21 12 16 7" />
              <line x1="21" y1="12" x2="9" y2="12" />
            </svg>
          </div>
          <span class="menu-text">退出登录</span>
        </div>
      </div>
    </div>

    <van-tabbar active="profile" @change="handleTabChange" class="tabbar">
      <van-tabbar-item name="home" class="tab-item">
        <template #icon>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V9z" />
            <polyline points="9 22 9 12 15 12 15 22" />
          </svg>
        </template>
        <span class="tab-text">首页</span>
      </van-tabbar-item>
      <van-tabbar-item name="history" class="tab-item">
        <template #icon>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14 2z" />
            <polyline points="14 2 14 8 20 8" />
            <line x1="16" y1="13" x2="8" y2="13" />
            <line x1="16" y1="17" x2="8" y2="17" />
          </svg>
        </template>
        <span class="tab-text">记录</span>
      </van-tabbar-item>
      <van-tabbar-item name="profile" class="tab-item">
        <template #icon>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
            <circle cx="12" cy="7" r="4" />
          </svg>
        </template>
        <span class="tab-text">我的</span>
      </van-tabbar-item>
    </van-tabbar>

    <van-action-sheet v-model:show="showLogout" :actions="[{ name: '确认退出', color: '#DC2626' }]" cancel-text="取消"
      @select="handleLogout" class="logout-sheet" />
  </div>
</template>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: var(--background-color);
  padding-bottom: 80px;
}

.navbar {
  background: var(--surface-color);
  box-shadow: var(--shadow-sm);
}

.navbar :deep(.van-nav-bar__title) {
  font-family: 'Cormorant', Georgia, serif;
  font-size: 19px;
  font-weight: 600;
  color: var(--primary-color);
}

.profile-content {
  padding: 20px 16px;
}

.user-section {
  margin-bottom: 20px;
}

.user-card {
  background: var(--surface-color);
  border-radius: var(--radius-xl);
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: var(--shadow-sm);
}

.avatar-wrapper {
  flex-shrink: 0;
}

.avatar {
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.user-info {
  flex: 1;
}

.nickname {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-color);
  margin: 0 0 4px;
}

.role {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0;
}

.admin-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(202, 138, 4, 0.1);
  padding: 8px 12px;
  border-radius: 20px;
  color: var(--cta-color);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.admin-badge:hover {
  background: rgba(202, 138, 4, 0.15);
}

.menu-section {
  background: var(--surface-color);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 18px 20px;
  border-bottom: 1px solid var(--border-light);
  cursor: pointer;
  transition: background var(--transition-fast);
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background: var(--background-color);
}

.menu-icon {
  width: 40px;
  height: 40px;
  background: rgba(28, 25, 23, 0.05);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  margin-right: 14px;
  flex-shrink: 0;
}

.logout-icon {
  background: rgba(220, 38, 38, 0.08);
  color: var(--error-color);
}

.menu-text {
  flex: 1;
  font-size: 15px;
  color: var(--text-color);
  font-weight: 500;
}

.menu-arrow {
  color: var(--text-light);
}

.tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: var(--surface-color);
  box-shadow: 0 -2px 20px rgba(0, 0, 0, 0.04);
  padding-bottom: env(safe-area-inset-bottom);
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.tab-text {
  font-size: 11px;
  color: var(--text-light);
}

.tabbar :deep(.van-tabbar-item--active) .tab-text {
  color: var(--primary-color);
}

.tabbar :deep(.van-tabbar-item--active) svg {
  color: var(--primary-color);
}

@media (max-width: 375px) {
  .user-card {
    padding: 20px;
  }

  .avatar {
    width: 64px;
    height: 64px;
  }

  .nickname {
    font-size: 18px;
  }
}
</style>
