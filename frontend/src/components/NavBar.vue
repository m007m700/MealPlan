<script setup lang="ts">
import { NavBar as VanNavBar } from 'vant'

defineProps<{
  title?: string
  showBack?: boolean
}>()

const emit = defineEmits<{
  back: []
}>()

function handleBack(): void {
  emit('back')
}
</script>

<template>
  <van-nav-bar
    :title="title || ''"
    left-text="返回"
    :left-arrow="showBack"
    @click-left="handleBack"
  />
</template>
