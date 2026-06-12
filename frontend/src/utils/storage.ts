const TOKEN_KEY = 'meal_plan_token'
const USER_INFO_KEY = 'meal_plan_user_info'

export const storage = {
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY)
  },

  setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token)
  },

  removeToken(): void {
    localStorage.removeItem(TOKEN_KEY)
  },

  getUserInfo(): Record<string, unknown> | null {
    const info = localStorage.getItem(USER_INFO_KEY)
    return info ? JSON.parse(info) : null
  },

  setUserInfo(info: Record<string, unknown>): void {
    localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
  },

  removeUserInfo(): void {
    localStorage.removeItem(USER_INFO_KEY)
  },

  clearAll(): void {
    this.removeToken()
    this.removeUserInfo()
  }
}
