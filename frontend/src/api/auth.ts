import axios from '@/utils/axios'

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  role: 'ADMIN' | 'USER'
  nickname: string
  userId: number
}

export const authApi = {
  login(data: LoginRequest): Promise<LoginResponse> {
    return axios.post('/auth/login', data)
  }
}
