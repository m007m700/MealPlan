import axios from '@/utils/axios'

export interface OrderRecord {
  id: number
  mealPlanId: number
  userId: number
  dishId: number
  createTime: string
}

export interface OrderStatistics {
  dishId: number
  dishName: string
  count: number
}

export interface OrderSubmitRequest {
  mealPlanId: number
  dishIds: number[]
}

export interface OrderHistory {
  id: number
  mealPlanId: number
  mealDate: string
  mealType: 'LUNCH' | 'DINNER'
  dishes: {
    id: number
    name: string
    description?: string
  }[]
  status: 'PENDING' | 'FINISHED'
  createTime: string
}

export const orderApi = {
  submit(data: OrderSubmitRequest): Promise<void> {
    return axios.post('/order/submit', data)
  },

  statistics(mealPlanId?: number): Promise<OrderStatistics[]> {
    const params = mealPlanId ? { mealPlanId } : {}
    return axios.get('/admin/statistics', { params })
  },

  confirm(mealPlanId: number, dishIds: number[], reply?: string): Promise<void> {
    return axios.post('/admin/confirm', { dishIds, reply }, { params: { mealPlanId } })
  },

  history(): Promise<OrderHistory[]> {
    return axios.get('/order/history')
  }
}
