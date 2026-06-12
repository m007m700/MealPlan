import axios from '@/utils/axios'

export interface Dish {
  id: number
  name: string
  imageUrl?: string
  description?: string
  status: number
  createTime: string
}

export interface DishAddRequest {
  name: string
  imageUrl?: string
  description?: string
}

export const dishApi = {
  list(): Promise<Dish[]> {
    return axios.get('/dish/list')
  },

  add(data: DishAddRequest): Promise<Dish> {
    return axios.post('/dish/add', data)
  },

  update(id: number, data: DishAddRequest): Promise<Dish> {
    return axios.put(`/dish/update/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return axios.delete(`/dish/delete/${id}`)
  }
}
