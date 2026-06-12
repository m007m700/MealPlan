import { defineStore } from 'pinia'
import { ref } from 'vue'
import { mealApi, type MealPlan, type MealPublishRequest } from '@/api/meal'
import { orderApi, type OrderStatistics, type OrderSubmitRequest } from '@/api/order'

export const useMealStore = defineStore('meal', () => {
  const todayMeal = ref<MealPlan | null>(null)
  const selectedDishes = ref<number[]>([])
  const statistics = ref<OrderStatistics[]>([])
  const loading = ref(false)

  function fetchTodayMeal(mealType?: import('@/api/meal').MealType): Promise<void> {
    loading.value = true
    return mealApi.today(mealType).then((data) => {
      todayMeal.value = data
      selectedDishes.value = []
    }).finally(() => {
      loading.value = false
    })
  }

  function submitOrder(request: OrderSubmitRequest): Promise<void> {
    return orderApi.submit(request).then(() => {
      selectedDishes.value = request.dishIds
    })
  }

  function fetchStatistics(mealPlanId?: number): Promise<void> {
    loading.value = true
    return orderApi.statistics(mealPlanId).then((data) => {
      statistics.value = data
    }).finally(() => {
      loading.value = false
    })
  }

  function confirmMenu(mealPlanId: number, dishIds: number[], reply?: string): Promise<void> {
    return orderApi.confirm(mealPlanId, dishIds, reply)
  }

  function selectDish(dishId: number): void {
    const index = selectedDishes.value.indexOf(dishId)
    if (index === -1) {
      selectedDishes.value.push(dishId)
    } else {
      selectedDishes.value.splice(index, 1)
    }
  }

  function isSelected(dishId: number): boolean {
    return selectedDishes.value.includes(dishId)
  }

  function clearSelection(): void {
    selectedDishes.value = []
  }

  function publishMeal(request: MealPublishRequest): Promise<MealPlan> {
    return mealApi.publish(request).then((data) => {
      todayMeal.value = data
      return data
    })
  }

  return {
    todayMeal,
    selectedDishes,
    statistics,
    loading,
    fetchTodayMeal,
    submitOrder,
    fetchStatistics,
    confirmMenu,
    selectDish,
    isSelected,
    clearSelection,
    publishMeal
  }
})
