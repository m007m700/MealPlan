<script setup lang="ts">import { ref, onMounted } from 'vue';
import { NavBar as VanNavBar, Button as VanButton, showToast, Tabbar as VanTabbar, TabbarItem as VanTabbarItem, Popup as VanPopup, Form as VanForm, Field as VanField } from 'vant';
import { useDishStore } from '@/stores/dish';
import router from '@/router';
import type { Dish } from '@/api/dish';
const dishStore = useDishStore();
const showAddModal = ref(false);
const showEditModal = ref(false);
const editDish = ref<Dish | null>(null);
const formData = ref({
  name: '',
  description: ''
});
onMounted(async () => {
  await dishStore.fetchDishes();
});
function handleAddDish(): void {
  showAddModal.value = true;
  formData.value = { name: '', description: '' };
}
function handleEditDish(dish: Dish): void {
  editDish.value = dish;
  formData.value = { name: dish.name, description: dish.description || '' };
  showEditModal.value = true;
}
async function handleSubmitAdd(): Promise<void> {
  if (!formData.value.name.trim()) {
    showToast('请输入菜品名称');
    return;
  }
  try {
    await dishStore.addDish({
      name: formData.value.name,
      description: formData.value.description
    });
    showAddModal.value = false;
    showToast('添加成功');
  }
  catch (error) {
    showToast('添加失败');
  }
}
async function handleSubmitEdit(): Promise<void> {
  if (!formData.value.name.trim()) {
    showToast('请输入菜品名称');
    return;
  }
  if (!editDish.value)
    return;
  try {
    await dishStore.updateDish(editDish.value.id, {
      name: formData.value.name,
      description: formData.value.description
    });
    showEditModal.value = false;
    showToast('修改成功');
  }
  catch (error) {
    showToast('修改失败');
  }
}
async function handleDeleteDish(dish: Dish): Promise<void> {
  try {
    await dishStore.deleteDish(dish.id);
    showToast('删除成功');
  }
  catch (error) {
    showToast('删除失败');
  }
}
function handleTabChange(name: string): void {
  if (name === 'order') {
    router.push('/admin/order');
  }
}
function goBack(): void {
  router.push('/admin/order');
}
</script>

<template>
  <div class="admin-dish-page">
    <van-nav-bar title="菜品管理" left-arrow @click-left="goBack" class="navbar" />

    <div class="content">
      <div class="add-section">
        <van-button type="primary" block @click="handleAddDish" class="add-btn">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 5v14M5 12h14" />
          </svg>
          <span>添加菜品</span>
        </van-button>
      </div>

      <div class="dish-section">
        <div class="section-header">
          <h2 class="section-title">菜品列表</h2>
          <span class="item-count">{{ dishStore.dishes.length }} 个菜品</span>
        </div>

        <div v-if="dishStore.dishes.length === 0" class="empty-state">
          <div class="empty-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"
              stroke-linecap="round" stroke-linejoin="round">
              <path
                d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" />
            </svg>
          </div>
          <p class="empty-text">暂无菜品</p>
        </div>

        <div v-else class="dish-grid">
          <div v-for="(dish, index) in dishStore.dishes" :key="dish.id" class="dish-card"
            :style="{ animationDelay: `${index * 50}ms` }">
            <div class="dish-info">
              <h3 class="dish-name">{{ dish.name }}</h3>
              <p v-if="dish.description" class="dish-desc">{{ dish.description }}</p>
            </div>
            <div class="dish-actions">
              <button class="action-btn edit-btn" @click="handleEditDish(dish)">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round">
                  <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z" />
                  <path d="m15 5 4 4" />
                </svg>
              </button>
              <button class="action-btn delete-btn" @click="handleDeleteDish(dish)">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round">
                  <path d="M3 6h18" />
                  <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
                  <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <van-tabbar active="dish" @change="handleTabChange" class="tabbar">
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

    <van-popup v-model:show="showAddModal" position="bottom" class="modal-popup">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title">添加菜品</h3>
          <button class="close-btn" @click="showAddModal = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path d="M18 6 6 18M6 6l12 12" />
            </svg>
          </button>
        </div>
        <van-form @submit="handleSubmitAdd" class="modal-form">
          <van-field v-model="formData.name" label="菜品名称" placeholder="请输入菜品名称" required class="form-field" />
          <van-field v-model="formData.description" label="菜品描述" placeholder="请输入菜品描述（可选）" type="textarea" rows="2"
            class="form-field" />
          <van-button type="primary" native-type="submit" block class="submit-btn">
            确认添加
          </van-button>
        </van-form>
      </div>
    </van-popup>

    <van-popup v-model:show="showEditModal" position="bottom" class="modal-popup">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title">编辑菜品</h3>
          <button class="close-btn" @click="showEditModal = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path d="M18 6 6 18M6 6l12 12" />
            </svg>
          </button>
        </div>
        <van-form @submit="handleSubmitEdit" class="modal-form">
          <van-field v-model="formData.name" label="菜品名称" placeholder="请输入菜品名称" required class="form-field" />
          <van-field v-model="formData.description" label="菜品描述" placeholder="请输入菜品描述（可选）" type="textarea" rows="2"
            class="form-field" />
          <van-button type="primary" native-type="submit" block class="submit-btn">
            确认修改
          </van-button>
        </van-form>
      </div>
    </van-popup>
  </div>
</template>

<style scoped>
.admin-dish-page {
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

.add-section {
  margin-bottom: 20px;
}

.add-btn {
  background: var(--primary-color);
  border: none;
  border-radius: var(--radius-lg);
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all var(--transition-normal);
}

.add-btn:active {
  background: var(--secondary-color);
}

.dish-section {
  background: var(--surface-color);
  border-radius: var(--radius-xl);
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-family: 'Cormorant', Georgia, serif;
  font-size: 20px;
  font-weight: 600;
  color: var(--text-color);
  margin: 0;
}

.item-count {
  font-size: 13px;
  color: var(--text-muted);
}

.empty-state {
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

.dish-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.dish-card {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  background: var(--background-color);
  border-radius: var(--radius-lg);
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

.dish-info {
  flex: 1;
}

.dish-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-color);
  margin: 0 0 4px;
}

.dish-desc {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
}

.dish-actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.edit-btn {
  background: rgba(202, 138, 4, 0.08);
  color: var(--cta-color);
}

.edit-btn:hover {
  background: rgba(202, 138, 4, 0.15);
}

.delete-btn {
  background: rgba(220, 38, 38, 0.08);
  color: var(--error-color);
}

.delete-btn:hover {
  background: rgba(220, 38, 38, 0.15);
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

.modal-popup :deep(.van-popup__content) {
  border-radius: var(--radius-xl) var(--radius-xl) 0 0;
  padding: 0;
}

.modal-content {
  padding: 24px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.modal-title {
  font-family: 'Cormorant', Georgia, serif;
  font-size: 20px;
  font-weight: 600;
  color: var(--text-color);
  margin: 0;
}

.close-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--background-color);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.close-btn:hover {
  background: var(--border-light);
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-field {
  background: var(--background-color);
  border-radius: var(--radius-md);
}

.form-field :deep(.van-field__control) {
  font-size: 14px;
}

.submit-btn {
  background: var(--primary-color);
  border: none;
  border-radius: var(--radius-lg);
  height: 48px;
  font-size: 15px;
  font-weight: 600;
}

@media (max-width: 375px) {
  .section-title {
    font-size: 18px;
  }

  .add-btn {
    height: 48px;
    font-size: 15px;
  }

  .modal-content {
    padding: 20px;
  }
}
</style>
