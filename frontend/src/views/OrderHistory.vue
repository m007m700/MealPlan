<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { NavBar as VanNavBar, showToast, Tabbar as VanTabbar, TabbarItem as VanTabbarItem } from 'vant'
import { orderApi, type OrderHistory } from '@/api/order'
import router from '@/router'

const loading = ref(false)
const orderHistory = ref<OrderHistory[]>([])

const mealTypeLabels: Record<string, string> = {
    BREAKFAST: '早餐',
    LUNCH: '午餐',
    DINNER: '晚餐'
}

const statusLabels: Record<string, string> = {
    PENDING: '待确认',
    FINISHED: '已完成'
}

onMounted(async () => {
    await fetchOrderHistory()
})

async function fetchOrderHistory(): Promise<void> {
    loading.value = true
    try {
        orderHistory.value = await orderApi.history()
    } catch (error) {
        showToast('获取订单历史失败')
    } finally {
        loading.value = false
    }
}

function formatDate(dateStr: string): string {
    const date = new Date(dateStr)
    return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
    })
}

function formatTime(timeStr: string): string {
    if (!timeStr) return ''
    const date = new Date(timeStr)
    return date.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit'
    })
}

function handleTabChange(name: string): void {
    if (name === 'home') {
        router.push('/')
    } else if (name === 'history') {
        router.push('/order-history')
    } else {
        router.push('/profile')
    }
}

const groupedOrders = computed(() => {
    const groups: Record<string, OrderHistory[]> = {}
    orderHistory.value.forEach(order => {
        const date = order.mealDate
        if (!groups[date]) {
            groups[date] = []
        }
        groups[date].push(order)
    })
    return groups
})
</script>

<template>
    <div class="order-history-page">
        <van-nav-bar title="点餐记录" class="navbar" />

        <!-- Content -->
        <div class="content">
            <!-- Loading -->
            <div v-if="loading" class="loading-state">
                <div class="loading-shimmer"></div>
                <div class="loading-shimmer"></div>
                <div class="loading-shimmer"></div>
            </div>

            <!-- Empty -->
            <div v-else-if="orderHistory.length === 0" class="empty-state">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1"
                    stroke-linecap="round" stroke-linejoin="round">
                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14 2z" />
                    <polyline points="14 2 14 8 20 8" />
                    <line x1="16" y1="13" x2="8" y2="13" />
                    <line x1="16" y1="17" x2="8" y2="17" />
                </svg>
                <h3>暂无点餐记录</h3>
                <p>快去选择美味的菜品吧</p>
            </div>

            <!-- Order List -->
            <div v-else class="order-list">
                <div v-for="(orders, date) in groupedOrders" :key="date" class="order-group">
                    <div class="group-header">
                        <span class="group-date">{{ formatDate(date) }}</span>
                    </div>

                    <div v-for="order in orders" :key="order.id" class="order-card">
                        <div class="order-header">
                            <div class="order-info">
                                <span class="meal-type">{{ mealTypeLabels[order.mealType] || order.mealType }}</span>
                                <span class="order-status" :class="`order-status--${order.status.toLowerCase()}`">
                                    {{ statusLabels[order.status] || order.status }}
                                </span>
                            </div>
                            <span class="order-time">{{ formatTime(order.createTime) }}</span>
                        </div>

                        <div class="dish-list">
                            <span v-for="(dish, index) in order.dishes" :key="dish.id" class="dish-tag">
                                {{ dish.name }}
                                <span v-if="index < order.dishes.length - 1" class="tag-separator">、</span>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabbar -->
        <van-tabbar active="history" @change="handleTabChange" class="tabbar">
            <van-tabbar-item name="home" class="tab-item">
                <template #icon>
                    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
                        stroke-linecap="round" stroke-linejoin="round">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V9z" />
                        <polyline points="9 22 9 12 15 12 15 22" />
                    </svg>
                </template>
                <span class="tab-text">首页</span>
            </van-tabbar-item>
            <van-tabbar-item name="history" class="tab-item">
                <template #icon>
                    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
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
                    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
                        stroke-linecap="round" stroke-linejoin="round">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                        <circle cx="12" cy="7" r="4" />
                    </svg>
                </template>
                <span class="tab-text">我的</span>
            </van-tabbar-item>
        </van-tabbar>
    </div>
</template>

<style scoped>
.order-history-page {
    min-height: 100vh;
    background: #fafaf9;
    padding-bottom: 80px;
    position: relative;
}

.navbar {
    background: transparent !important;
    box-shadow: none !important;
}

.navbar :deep(.van-nav-bar__title) {
    font-family: 'Cormorant', Georgia, serif;
    font-size: 19px;
    font-weight: 600;
    color: #1a1a1a;
}

.navbar :deep(.van-icon) {
    color: #1a1a1a;
}

.content {
    padding: 20px;
}

/* Loading State */
.loading-state {
    display: flex;
    flex-direction: column;
    gap: 12px;
    padding: 20px 0;
}

.loading-shimmer {
    height: 80px;
    background: linear-gradient(90deg, #f5f5f4 25%, #e7e5e4 50%, #f5f5f4 75%);
    background-size: 200% 100%;
    animation: shimmer 1.5s infinite;
    border-radius: 16px;
}

@keyframes shimmer {
    0% {
        background-position: 200% 0;
    }

    100% {
        background-position: -200% 0;
    }
}

/* Empty State */
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 80px 20px;
    color: #a3a3a3;
}

.empty-state svg {
    margin-bottom: 16px;
    opacity: 0.4;
}

.empty-state h3 {
    font-size: 16px;
    font-weight: 600;
    color: #525252;
    margin: 0 0 6px;
}

.empty-state p {
    font-size: 13px;
    margin: 0;
}

/* Order List */
.order-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.order-group {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.group-header {
    padding: 8px 0;
}

.group-date {
    font-size: 12px;
    font-weight: 500;
    color: #a3a3a3;
    letter-spacing: 0.05em;
}

.order-card {
    background: #fff;
    border-radius: 16px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
}

.order-info {
    display: flex;
    align-items: center;
    gap: 10px;
}

.meal-type {
    font-size: 14px;
    font-weight: 600;
    color: #1a1a1a;
}

.order-status {
    font-size: 11px;
    font-weight: 500;
    padding: 3px 10px;
    border-radius: 12px;
}

.order-status--pending {
    background: #fef3c7;
    color: #d97706;
}

.order-status--finished {
    background: #dcfce7;
    color: #16a34a;
}

.order-time {
    font-size: 12px;
    color: #a3a3a3;
}

.dish-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.dish-tag {
    display: inline-flex;
    align-items: center;
    padding: 4px 12px;
    background: #f5f5f4;
    border-radius: 20px;
    font-size: 13px;
    color: #525252;
}

.tag-separator {
    margin-left: -4px;
    margin-right: 4px;
    color: #d4d4d4;
}

/* Tabbar */
.tabbar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 100;
    background: rgba(255, 255, 255, 0.85) !important;
    backdrop-filter: blur(20px);
    border-top: 1px solid rgba(0, 0, 0, 0.05);
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
    font-size: 10px;
    font-weight: 500;
    color: #a3a3a3;
    letter-spacing: 0.02em;
}

.tabbar :deep(.van-tabbar-item--active) .tab-text {
    color: #1a1a1a;
    font-weight: 600;
}

.tabbar :deep(.van-tabbar-item--active) svg {
    color: #1a1a1a;
}
</style>
