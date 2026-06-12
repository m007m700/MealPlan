<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { OrderRecord } from '@/api/order'
import { dishApi, type Dish } from '@/api/dish'

const props = defineProps<{
  order: OrderRecord
  userName: string
}>()

const dish = ref<Dish | null>(null)

onMounted(() => {
  dishApi.list().then((dishes) => {
    dish.value = dishes.find(d => d.id === props.order.dishId) || null
  })
})
</script>

<template>
  <div class="order-item">
    <div class="user-name">{{ userName }}</div>
    <div class="dish-name">{{ dish?.name || '未知菜品' }}</div>
    <div class="order-time">{{ order.createTime }}</div>
  </div>
</template>

<style scoped>
.order-item {
  padding: 12px 16px;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 8px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.dish-name {
  font-size: 13px;
  color: #666;
  margin-top: 4px;
}

.order-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}
</style>
