<script setup lang="ts">
import type { Dish } from '@/api/dish'

const props = defineProps<{
  dish: Dish
  selected?: boolean
  multiple?: boolean
  disabled?: boolean
}>()

const emit = defineEmits<{
  select: [dish: Dish]
}>()

function handleClick(): void {
  if (!props.disabled) {
    emit('select', { ...props.dish })
  }
}
</script>

<template>
  <div class="dish-card" :class="{ 'is-selected': selected, 'disabled': disabled }" @click="handleClick">
    <div class="card-check" :class="{ 'check--visible': selected }">
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"
        stroke-linecap="round" stroke-linejoin="round">
        <polyline points="20 6 9 17 4 12" />
      </svg>
    </div>
    <div class="card-content">
      <h3 class="card-name">{{ dish.name }}</h3>
      <p v-if="dish.description" class="card-desc">{{ dish.description }}</p>
    </div>
  </div>
</template>

<style scoped>
.dish-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  border-radius: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.dish-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-1px);
}

.dish-card.is-selected {
  border-color: #1a1a1a;
  background: #1a1a1a;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.dish-card.disabled {
  opacity: 0.6;
  cursor: not-allowed;
  pointer-events: none;
}

.card-check {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: 1.5px solid #d4d4d4;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: transparent;
}

.check--visible {
  border-color: #fff;
  background: #fff;
  color: #1a1a1a;
}

.is-selected .card-check {
  border-color: #fff;
  background: #fff;
  color: #1a1a1a;
}

.card-content {
  flex: 1;
  min-width: 0;
}

.card-name {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
  line-height: 1.4;
  transition: color 0.3s;
}

.is-selected .card-name {
  color: #fff;
}

.card-desc {
  font-size: 13px;
  color: #a3a3a3;
  margin: 4px 0 0;
  line-height: 1.5;
  transition: color 0.3s;
}

.is-selected .card-desc {
  color: rgba(255, 255, 255, 0.7);
}

@media (max-width: 375px) {
  .dish-card {
    padding: 14px;
  }

  .card-name {
    font-size: 14px;
  }
}
</style>
