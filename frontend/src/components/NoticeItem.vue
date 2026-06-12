<script setup lang="ts">
import type { Notice } from '@/api/note'

defineProps<{
  notice: Notice
}>()
</script>

<template>
  <div class="notice-item">
    <div class="notice-title">{{ notice.title }}</div>
    <div class="notice-content">{{ notice.content }}</div>
    <div class="notice-time">{{ notice.createTime }}</div>
  </div>
</template>

<style scoped>
.notice-item {
  padding: 12px 16px;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 8px;
}

.notice-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.notice-content {
  font-size: 13px;
  color: #666;
  margin-top: 4px;
  white-space: pre-wrap;
}

.notice-time {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}
</style>
