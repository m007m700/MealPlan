import axios from '@/utils/axios'
import type { Dish } from './dish'

export type MealType = 'BREAKFAST' | 'LUNCH' | 'DINNER'
export type MealStatus = 'DRAFT' | 'PUBLISHED' | 'FINISHED'

export interface MealPlan {
  id: number
  mealDate: string
  mealType: MealType
  status: MealStatus
  adminReply?: string
  dishes: Dish[]
}

export interface MealPublishRequest {
  mealDate: string
  mealType: MealType
  dishIds: number[]
}

export const mealApi = {
  today(mealType: MealType = 'LUNCH'): Promise<MealPlan> {
    return axios.get('/meal/today', { params: { mealType } })
  },

  publish(data: MealPublishRequest): Promise<MealPlan> {
    return axios.post('/meal/publish', data)
  },

  list(): Promise<MealPlan[]> {
    return axios.get('/meal/list')
  }
}
