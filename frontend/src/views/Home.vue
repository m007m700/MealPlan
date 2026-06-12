<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { NavBar as VanNavBar, NoticeBar as VanNoticeBar, showToast, Tabbar as VanTabbar, TabbarItem as VanTabbarItem } from 'vant'
import { useNoticeStore } from '@/stores/notice'
import { useUserStore } from '@/stores/user'
import DishCard from '@/components/DishCard.vue'
import type { Dish } from '@/api/dish'
import { dishApi } from '@/api/dish'
import { mealApi, type MealPlan, type MealType } from '@/api/meal'
import { orderApi, type OrderSubmitRequest } from '@/api/order'
import router from '@/router'

interface MealTypeConfig {
    label: string
    icon: string
}

const mealTypeConfig: Record<MealType, MealTypeConfig> = {
    BREAKFAST: { label: '早餐', icon: 'sun' },
    LUNCH: { label: '午餐', icon: 'clock' },
    DINNER: { label: '晚餐', icon: 'moon' }
}

const noticeStore = useNoticeStore()
const userStore = useUserStore()
const notices = computed(() => noticeStore.notices)
const loading = ref(false)
const activeMealType = ref<MealType>('LUNCH')
const selectedDishes = ref<Record<MealType, number[]>>({
    BREAKFAST: [],
    LUNCH: [],
    DINNER: []
})
const dishes = ref<Dish[]>([])
const mealPlan = ref<MealPlan | null>(null)
const hasSubmitted = ref<Record<MealType, boolean>>({
    BREAKFAST: false,
    LUNCH: false,
    DINNER: false
})

onMounted(async () => {
    await fetchAllDishes()
    await fetchTodayMenu()
    await noticeStore.fetchNotices()
})

async function fetchAllDishes(): Promise<void> {
    loading.value = true
    try {
        dishes.value = await dishApi.list()
    } catch (error) {
        showToast('获取菜品列表失败')
    } finally {
        loading.value = false
    }
}

async function fetchTodayMenu(): Promise<void> {
    try {
        mealPlan.value = await mealApi.today(activeMealType.value)
        checkSubmittedStatus()
    } catch (error) {
        // 没有今日菜单时不报错，继续使用所有菜品
    }
}

async function handleMealTypeChange(type: MealType): void {
    activeMealType.value = type
    selectedDishes.value[type] = []
    await fetchTodayMenu()
}

function handleDishSelect(dish: Dish): void {
    if (hasSubmitted.value[activeMealType.value]) {
        showToast('已提交，不可修改')
        return
    }

    const currentSelection = selectedDishes.value[activeMealType.value]
    const index = currentSelection.indexOf(dish.id)
    if (index > -1) {
        currentSelection.splice(index, 1)
        showToast(`已取消${mealTypeConfig[activeMealType.value].label}: ${dish.name}`)
    } else {
        currentSelection.push(dish.id)
        showToast(`已选择${mealTypeConfig[activeMealType.value].label}: ${dish.name}`)
    }
}

function isSelected(dishId: number): boolean {
    return selectedDishes.value[activeMealType.value].includes(dishId)
}

function getCurrentSelectedCount(): number {
    return selectedDishes.value[activeMealType.value].length
}

function checkSubmittedStatus(): void {
    if (mealPlan.value?.status === 'FINISHED') {
        hasSubmitted.value[activeMealType.value] = true
    }
}

async function handleSubmitOrder(): Promise<void> {
    if (!userStore.isLoggedIn) {
        showToast('请先登录')
        router.push('/login')
        return
    }

    const currentSelection = selectedDishes.value[activeMealType.value]
    if (currentSelection.length === 0) {
        showToast('请先选择菜品')
        return
    }

    loading.value = true
    try {
        let mealPlanId = mealPlan.value?.id

        // 如果没有今日菜单，先创建一个
        if (!mealPlanId) {
            const today = new Date().toISOString().split('T')[0]
            const publishResponse = await mealApi.publish({
                mealDate: today,
                mealType: activeMealType.value,
                dishIds: currentSelection
            })
            mealPlanId = publishResponse.id
        }

        const request: OrderSubmitRequest = {
            mealPlanId: mealPlanId,
            dishIds: currentSelection
        }

        await orderApi.submit(request)

        hasSubmitted.value[activeMealType.value] = true
        showToast(`${mealTypeConfig[activeMealType.value].label}提交成功！`)
        selectedDishes.value[activeMealType.value] = []

        // 更新菜单状态
        mealPlan.value = await mealApi.today(activeMealType.value)
    } catch (error: any) {
        const message = error?.response?.data?.message || '提交失败，请重试'
        showToast(message)
    } finally {
        loading.value = false
    }
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
</script>

<template>
    <div class="home-page">
        <div class="page-bg"></div>

        <van-nav-bar title="今日菜单" class="navbar" />

        <van-notice-bar v-if="notices.length > 0" :text="notices[0]?.content" scrollable class="notice-bar" />

        <!-- Hero Section -->
        <div class="hero-section">
            <div class="hero-date">
                <span class="weekday">{{ new Date().toLocaleDateString('zh-CN', { weekday: 'long' }) }}</span>
                <span class="full-date">{{ new Date().toLocaleDateString('zh-CN', {
                    year: 'numeric', month: 'long', day: 'numeric'
                }) }}</span>
            </div>
            <h1 class="hero-title">今日菜单</h1>
        </div>

        <!-- Meal Type Tabs -->
        <div class="meal-type-tabs">
            <button v-for="(config, type) in mealTypeConfig" :key="type" class="meal-type-tab"
                :class="{ 'meal-type-tab--active': activeMealType === type }"
                @click="handleMealTypeChange(type as MealType)">
                <div class="tab-icon-wrapper">
                    <svg v-if="type === 'BREAKFAST'" width="22" height="22" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                        <circle cx="12" cy="12" r="5" />
                        <line x1="12" y1="1" x2="12" y2="3" />
                        <line x1="4.22" y1="4.22" x2="5.64" y2="5.64" />
                        <line x1="1" y1="12" x2="3" y2="12" />
                        <line x1="4.22" y1="19.78" x2="5.64" y2="18.36" />
                        <line x1="12" y1="21" x2="12" y2="23" />
                        <line x1="19.78" y1="19.78" x2="18.36" y2="18.36" />
                        <line x1="23" y1="12" x2="21" y2="12" />
                        <line x1="19.78" y1="4.22" x2="18.36" y2="5.64" />
                    </svg>
                    <svg v-else-if="type === 'LUNCH'" width="22" height="22" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                        <circle cx="12" cy="12" r="4" />
                        <path d="M12 2v2" />
                        <path d="M12 20v2" />
                        <path d="m4.93 4.93 1.41 1.41" />
                        <path d="m17.66 17.66 1.41 1.41" />
                        <path d="M2 12h2" />
                        <path d="M20 12h2" />
                        <path d="m6.34 17.66-1.41 1.41" />
                        <path d="m19.07 4.93-1.41 1.41" />
                    </svg>
                    <svg v-else width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                        stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
                    </svg>
                </div>
                <span class="tab-label">{{ config.label }}</span>
            </button>
        </div>

        <!-- Content -->
        <div class="content">
            <!-- Loading -->
            <div v-if="loading" class="loading-state">
                <div class="loading-shimmer"></div>
                <div class="loading-shimmer"></div>
                <div class="loading-shimmer"></div>
            </div>

            <!-- Empty -->
            <div v-else-if="dishes.length === 0" class="empty-state">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1"
                    stroke-linecap="round" stroke-linejoin="round">
                    <path
                        d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" />
                </svg>
                <h3>暂无菜品</h3>
                <p>菜品尚未添加，请联系管理员</p>
            </div>

            <!-- Dish List -->
            <div v-else class="dish-section">
                <!-- Status Badge -->
                <div class="status-bar" v-if="mealPlan?.status">
                    <span class="status-badge" :class="`status-badge--${mealPlan.status.toLowerCase()}`">
                        {{ mealPlan.status === 'DRAFT' ? '草稿' : mealPlan.status === 'PUBLISHED' ? '进行中' : '已结束' }}
                    </span>
                    <span v-if="hasSubmitted[activeMealType]" class="submitted-badge">✓ 已提交</span>
                </div>

                <p class="section-hint">选择你想吃的{{ mealTypeConfig[activeMealType].label }}</p>

                <div class="dish-grid">
                    <DishCard v-for="(dish, index) in dishes" :key="dish.id" :dish="dish"
                        :selected="isSelected(dish.id)" :disabled="hasSubmitted[activeMealType]"
                        @select="handleDishSelect" :style="{ animationDelay: `${index * 80}ms` }" />
                </div>

                <!-- Admin Reply -->
                <div v-if="mealPlan?.adminReply" class="admin-reply">
                    <div class="reply-header">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                            stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M8 10h.01" />
                            <path d="M12 10h.01" />
                            <path d="M16 10h.01" />
                            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
                        </svg>
                        <span class="reply-label">管理员回复</span>
                    </div>
                    <p class="reply-content">{{ mealPlan.adminReply }}</p>
                </div>

                <!-- Submit Area -->
                <div class="submit-area" v-if="!hasSubmitted[activeMealType] && getCurrentSelectedCount() > 0">
                    <button class="submit-btn" @click="handleSubmitOrder">
                        <span class="btn-label">提交{{ mealTypeConfig[activeMealType].label }}</span>
                        <span class="btn-badge">已选 {{ getCurrentSelectedCount() }}</span>
                    </button>
                </div>

                <!-- Submitted Area -->
                <div class="submit-area submit-area--submitted" v-if="hasSubmitted[activeMealType]">
                    <button class="submit-btn submit-btn--disabled" disabled>
                        <span class="btn-label">✓ {{ mealTypeConfig[activeMealType].label }}已提交</span>
                    </button>
                </div>
            </div>
        </div>

        <!-- Tabbar -->
        <van-tabbar active="home" @change="handleTabChange" class="tabbar">
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
.home-page {
    min-height: 100vh;
    background: #fafaf9;
    padding-bottom: 80px;
    position: relative;
    overflow: hidden;
}

.page-bg {
    position: fixed;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle at 30% 20%, rgba(202, 138, 4, 0.04) 0%, transparent 50%),
        radial-gradient(circle at 80% 80%, rgba(107, 65, 60, 0.03) 0%, transparent 50%);
    pointer-events: none;
    z-index: 0;
}

.navbar {
    position: relative;
    z-index: 10;
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

.notice-bar {
    position: relative;
    z-index: 10;
    margin: 8px 16px 0;
    background: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(12px);
    border-radius: 12px;
    padding: 8px 12px;
    border: 1px solid rgba(202, 138, 4, 0.1);
}

.notice-bar :deep(.van-notice-bar__content) {
    color: #1a1a1a;
    font-size: 13px;
}

/* Hero Section */
.hero-section {
    position: relative;
    z-index: 10;
    padding: 24px 20px 20px;
}

.hero-date {
    display: flex;
    align-items: baseline;
    gap: 10px;
    margin-bottom: 8px;
}

.weekday {
    font-size: 13px;
    font-weight: 500;
    color: #a3a3a3;
    text-transform: uppercase;
    letter-spacing: 0.05em;
}

.full-date {
    font-size: 11px;
    color: #d4d4d4;
}

.hero-title {
    font-family: 'Cormorant', Georgia, serif;
    font-size: 32px;
    font-weight: 700;
    color: #1a1a1a;
    margin: 0;
    letter-spacing: -0.02em;
    line-height: 1.1;
}

/* Meal Type Tabs */
.meal-type-tabs {
    position: relative;
    z-index: 10;
    display: flex;
    gap: 8px;
    padding: 0 20px;
    margin-bottom: 20px;
}

.meal-type-tab {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    padding: 14px 8px;
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(12px);
    border-radius: 16px;
    border: 1px solid rgba(0, 0, 0, 0.06);
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    color: #a3a3a3;
}

.meal-type-tab--active {
    background: #1a1a1a;
    border-color: #1a1a1a;
    color: #fff;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    transform: translateY(-2px);
}

.tab-icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.meal-type-tab--active .tab-icon-wrapper {
    transform: scale(1.1);
}

.tab-label {
    font-size: 12px;
    font-weight: 600;
    letter-spacing: 0.02em;
}

/* Content */
.content {
    position: relative;
    z-index: 10;
    padding: 0 20px;
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
    padding: 48px 20px;
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

/* Dish Section */
.dish-section {
    padding-bottom: 100px;
}

.section-hint {
    font-size: 13px;
    color: #a3a3a3;
    margin: 0 0 12px;
    font-style: italic;
}

.dish-grid {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.dish-grid>* {
    animation: fadeInUp 0.5s cubic-bezier(0.4, 0, 0.2, 1) backwards;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(12px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* Status Bar */
.status-bar {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;
}

.status-badge {
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
}

.status-badge--draft {
    background: #fef3c7;
    color: #d97706;
}

.status-badge--published {
    background: #dcfce7;
    color: #16a34a;
}

.status-badge--finished {
    background: #f3f4f6;
    color: #6b7280;
}

.submitted-badge {
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
    background: #dcfce7;
    color: #16a34a;
}

/* Admin Reply */
.admin-reply {
    margin-top: 16px;
    padding: 16px;
    background: rgba(202, 138, 4, 0.05);
    border-radius: 12px;
    border: 1px solid rgba(202, 138, 4, 0.1);
}

.reply-header {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 8px;
}

.reply-header svg {
    color: #ca8a04;
}

.reply-label {
    font-size: 12px;
    font-weight: 600;
    color: #ca8a04;
}

.reply-content {
    font-size: 14px;
    color: #525252;
    margin: 0;
    line-height: 1.6;
}

/* Submit Area */
.submit-area {
    position: fixed;
    bottom: 80px;
    left: 0;
    right: 0;
    padding: 16px 20px;
    background: linear-gradient(to top, rgba(250, 250, 249, 0.95) 0%, rgba(250, 250, 249, 0) 100%);
    z-index: 50;
}

.submit-area--submitted {
    background: linear-gradient(to top, rgba(250, 250, 249, 0.98) 0%, rgba(250, 250, 249, 0) 100%);
}

.submit-btn {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 24px;
    background: #1a1a1a;
    color: #fff;
    border: none;
    border-radius: 16px;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.submit-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
}

.submit-btn:active {
    transform: translateY(0);
}

.submit-btn--disabled {
    background: #e5e5e5;
    color: #a3a3a3;
    cursor: not-allowed;
    box-shadow: none;
}

.submit-btn--disabled:hover {
    transform: none;
    box-shadow: none;
}

.btn-label {
    font-size: 16px;
    font-weight: 600;
}

.btn-badge {
    background: rgba(255, 255, 255, 0.2);
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
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

/* Responsive */
@media (max-width: 375px) {
    .hero-title {
        font-size: 28px;
    }

    .content {
        padding: 0 16px;
    }
}
</style>