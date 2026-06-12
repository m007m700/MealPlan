import { defineStore } from 'pinia'
import { ref } from 'vue'
import { orderApi, type OrderHistory } from '@/api/order'

export const useOrderStore = defineStore('order', () => {
  const orderHistory = ref<OrderHistory[]>([])
  const loading = ref(false)

  function fetchOrderHistory(): Promise<void> {
    loading.value = true
    return orderApi.history().then((data) => {
      orderHistory.value = data
    }).finally(() => {
      loading.value = false
    })
  }

  return {
    orderHistory,
    loading,
    fetchOrderHistory
  }
})
