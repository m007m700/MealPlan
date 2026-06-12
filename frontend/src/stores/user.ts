import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi, type LoginRequest, type LoginResponse } from '@/api/auth'
import { storage } from '@/utils/storage'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>('')
  const role = ref<'ADMIN' | 'USER' | ''>('')
  const nickname = ref<string>('')

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => role.value === 'ADMIN')

  function login(request: LoginRequest): Promise<void> {
    return authApi.login(request).then((response: LoginResponse) => {
      token.value = response.token
      role.value = response.role
      nickname.value = response.nickname
      storage.setToken(response.token)
      storage.setUserInfo({ role: response.role, nickname: response.nickname })
    })
  }

  function logout(): void {
    token.value = ''
    role.value = ''
    nickname.value = ''
    storage.clearAll()
  }

  function setUserInfo(info: { role: string; nickname: string }): void {
    role.value = info.role as 'ADMIN' | 'USER'
    nickname.value = info.nickname
  }

  function init(): void {
    const savedToken = storage.getToken()
    const savedInfo = storage.getUserInfo()
    if (savedToken && savedInfo) {
      token.value = savedToken
      role.value = savedInfo.role as 'ADMIN' | 'USER'
      nickname.value = savedInfo.nickname as string
    }
  }

  return {
    token,
    role,
    nickname,
    isLoggedIn,
    isAdmin,
    login,
    logout,
    setUserInfo,
    init
  }
})
