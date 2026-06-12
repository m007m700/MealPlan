<script setup lang="ts">
import { ref } from 'vue'
import { NavBar as VanNavBar, Cell as VanCell, CellGroup as VanCellGroup, ActionSheet as VanActionSheet, showToast } from 'vant'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const userStore = useUserStore()
const showActionSheet = ref(false)

function handleLogout(): void {
  showActionSheet.value = true
}

async function confirmLogout(): Promise<void> {
  userStore.logout()
  showActionSheet.value = false
  showToast('已退出登录')
  router.push('/login')
}

function handleBack(): void {
  router.back()
}
</script>

<template>
  <div class="admin-profile-page">
    <van-nav-bar title="我的" left-text="返回" left-arrow @click-left="handleBack" />

    <div class="user-info">
      <div class="avatar">👤</div>
      <div class="nickname">{{ userStore.nickname }}</div>
      <div class="role">管理员</div>
    </div>

    <van-cell-group>
      <van-cell title="家庭成员管理" icon="users" />
      <van-cell title="发布菜单" icon="plus" />
      <van-cell title="系统设置" icon="setting-o" />
      <van-cell title="退出登录" icon="logout" @click="handleLogout" />
    </van-cell-group>

    <van-action-sheet v-model:show="showActionSheet" title="确认退出" description="确定要退出登录吗？"
      :actions="[{ name: '取消' }, { name: '退出登录', color: '#ee0a24' }]" cancel-text=""
      @select="(action) => action.name === '退出登录' && confirmLogout()" />
  </div>
</template>

<style scoped>
.admin-profile-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.user-info {
  background: linear-gradient(180deg, #07c160 0%, #95de64 100%);
  padding: 40px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
}

.nickname {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin-top: 12px;
}

.role {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 4px;
}

.van-cell-group {
  margin-top: 16px;
}
</style>
