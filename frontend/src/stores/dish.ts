import { defineStore } from 'pinia'
import { ref } from 'vue'
import { dishApi, type Dish, type DishAddRequest } from '@/api/dish'

export const useDishStore = defineStore('dish', () => {
  const dishes = ref<Dish[]>([])
  const loading = ref(false)

  function fetchDishes(): Promise<void> {
    loading.value = true
    return dishApi.list().then((data) => {
      dishes.value = data
    }).finally(() => {
      loading.value = false
    })
  }

  function addDish(data: DishAddRequest): Promise<Dish> {
    return dishApi.add(data).then((dish) => {
      dishes.value.push(dish)
      return dish
    })
  }

  function updateDish(id: number, data: DishAddRequest): Promise<Dish> {
    return dishApi.update(id, data).then((dish) => {
      const index = dishes.value.findIndex(d => d.id === id)
      if (index !== -1) {
        dishes.value[index] = dish
      }
      return dish
    })
  }

  function deleteDish(id: number): Promise<void> {
    return dishApi.delete(id).then(() => {
      dishes.value = dishes.value.filter(d => d.id !== id)
    })
  }

  return {
    dishes,
    loading,
    fetchDishes,
    addDish,
    updateDish,
    deleteDish
  }
})
