<script setup lang="ts">import { ref, onMounted, computed } from 'vue';
import { NavBar as VanNavBar, Field as VanField, showToast, Tabbar as VanTabbar, TabbarItem as VanTabbarItem } from 'vant';
import { useMealStore } from '@/stores/meal';
import { useUserStore } from '@/stores/user';
import router from '@/router';
const mealStore = useMealStore();
const userStore = useUserStore();
const statistics = computed(() => mealStore.statistics);
const todayMeal = computed(() => mealStore.todayMeal);
const loading = ref(true);
const adminReply = ref('');
onMounted(async () => {
  await mealStore.fetchTodayMeal();
  if (todayMeal.value) {
    await mealStore.fetchStatistics(todayMeal.value.id);
  }
  loading.value = false;
});
async function handleConfirmMenu(): Promise<void> {
  if (!todayMeal.value) {
    showToast('暂无菜单');
    return;
  }
  try {
    const dishIds = todayMeal.value.dishes.map(d => d.id);
    await mealStore.confirmMenu(todayMeal.value.id, dishIds, adminReply.value || undefined);
    showToast('菜单已确认');
    await mealStore.fetchTodayMeal();
  }
  catch (error) {
    showToast('操作失败');
  }
}
function handleTabChange(name: string): void {
  if (name === 'dish') {
    router.push('/admin/dish');
  }
}
function goBack(): void {
  if (userStore.isAdmin) {
    router.push('/admin/dish');
  }
  else {
    router.push('/');
  }
}
</script>

<template>
  <div class="admin-order-page">
    <van-nav-bar title="投票统计" left-arrow @click-left="goBack" class="navbar" />

    <div class="content">
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <span class="loading-text">加载中...</span>
      </div>

      <div v-else class="stats-section">
        <div class="stats-header">
          <h2 class="stats-title">今日投票统计</h2>
          <p class="stats-date">{{ new Date().toLocaleDateString('zh-CN', {
            month: 'long', day: 'numeric', weekday:
              'long'
          }) }}</p>
        </div>

        <div v-if="statistics.length === 0" class="empty-stats">
          <div class="empty-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"
              stroke-linecap="round" stroke-linejoin="round">
              <path
                d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" />
            </svg>
          </div>
          <p class="empty-text">暂无投票数据</p>
        </div>

        <div v-else class="stats-list">
          <div v-for="(stat, index) in statistics" :key="stat.dishId" class="stat-card"
            :style="{ animationDelay: `${index * 80}ms` }">
            <div class="stat-header">
              <span class="dish-name">{{ stat.dishName }}</span>
              <span class="vote-count">{{ stat.count }}票</span>
            </div>
            <div class="progress-container">
              <div class="progress-bar"
                :style="{ width: `${(stat.count / Math.max(...statistics.map(s => s.count))) * 100}%` }"></div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="todayMeal" class="action-section">
        <div class="meal-info">
          <div class="info-header">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 8v4l3 3" />
              <circle cx="12" cy="12" r="10" />
            </svg>
            <span class="info-title">当前状态</span>
          </div>
          <span class="status-badge" :class="todayMeal.status === 'FINISHED' ? 'status-confirmed' : 'status-pending'">
            {{ todayMeal.status === 'FINISHED' ? '已确认' : '待确认' }}
          </span>
        </div>

        <div class="reply-section">
          <van-field v-model="adminReply" label="回复" type="textarea" placeholder="可选：回复用户，如菜品是否可做等说明" rows="3" autosize
            :disabled="todayMeal.status === 'FINISHED'" class="reply-field" />
        </div>

        <div class="action-buttons">
          <van-button type="primary" block @click="handleConfirmMenu" :disabled="todayMeal.status === 'FINISHED'"
            class="action-btn confirm-btn">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <polyline points="20 6 9 17 4 12" />
            </svg>
            <span>确认菜单</span>
          </van-button>
        </div>
      </div>
    </div>

    <van-tabbar active="order" @change="handleTabChange" class="tabbar">
      <van-tabbar-item name="order" class="tab-item">
        <template #icon>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
          </svg>
        </template>
        <span class="tab-text">投票</span>
      </van-tabbar-item>
      <van-tabbar-item name="dish" class="tab-item">
        <template #icon>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <path
              d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" />
          </svg>
        </template>
        <span class="tab-text">菜品</span>
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style scoped>
.admin-order-page {
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

.content {
  padding: 20px 16px;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 0;
}

.spinner {
  width: 36px;
  height: 36px;
  border: 2px solid var(--border-color);
  border-top-color: var(--cta-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-text {
  margin-top: 16px;
  color: var(--text-muted);
  font-size: 14px;
}

.stats-section {
  background: var(--surface-color);
  border-radius: var(--radius-xl);
  padding: 24px;
  box-shadow: var(--shadow-sm);
  margin-bottom: 20px;
}

.stats-header {
  margin-bottom: 24px;
}

.stats-title {
  font-family: 'Cormorant', Georgia, serif;
  font-size: 22px;
  font-weight: 600;
  color: var(--text-color);
  margin: 0 0 6px;
}

.stats-date {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
}

.empty-stats {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
}

.empty-icon {
  color: var(--text-light);
  margin-bottom: 12px;
}

.empty-text {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0;
}

.stats-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stat-card {
  animation: fadeInUp 0.4s ease-out backwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.dish-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-color);
}

.vote-count {
  font-size: 14px;
  font-weight: 700;
  color: var(--cta-color);
}

.progress-container {
  height: 8px;
  background: var(--border-light);
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, var(--primary-color) 0%, var(--cta-color) 100%);
  border-radius: 4px;
  transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.action-section {
  background: var(--surface-color);
  border-radius: var(--radius-xl);
  padding: 20px;
  box-shadow: var(--shadow-sm);
}

.meal-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border-light);
  margin-bottom: 16px;
}

.info-header {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--text-muted);
}

.info-title {
  font-size: 14px;
}

.status-badge {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
}

.status-pending {
  background: rgba(202, 138, 4, 0.1);
  color: var(--cta-color);
}

.status-confirmed {
  background: rgba(22, 163, 74, 0.1);
  color: var(--success-color);
}

.reply-section {
  margin-bottom: 16px;
}

.reply-field :deep(.van-field__control) {
  font-size: 14px;
  color: var(--text-color);
}

.reply-field :deep(.van-field__placeholder) {
  color: var(--text-muted);
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-btn {
  height: 48px;
  border-radius: var(--radius-lg);
  font-size: 15px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all var(--transition-normal);
}

.confirm-btn {
  background: var(--primary-color);
  border: none;
  color: #fff;
}

.confirm-btn:active {
  background: var(--secondary-color);
}

.confirm-btn:disabled {
  background: var(--border-color);
  opacity: 0.6;
  color: #fff;
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
  .stats-title {
    font-size: 20px;
  }

  .action-btn {
    height: 44px;
    font-size: 14px;
  }
}
</style>
