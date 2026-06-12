import axios from 'axios'
import router from '@/router'
import { storage } from '@/utils/storage'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.request.use(
  (config) => {
    const token = storage.getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

instance.interceptors.response.use(
  (response) => {
    const res = response.data as ApiResponse
    if (res.code !== 200) {
      // 业务错误处理
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res.data
  },
  (error) => {
    if (error.response?.status === 401) {
      storage.clearAll()
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default instance
