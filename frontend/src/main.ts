import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from '@/router'
import App from '@/App.vue'
import { useUserStore } from '@/stores/user'
import '@/style.scss'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)

// 初始化用户状态（从 localStorage 恢复）
const userStore = useUserStore()
userStore.init()

app.use(router)

app.mount('#app')
