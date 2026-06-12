<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Form as VanForm, Field as VanField, Button as VanButton } from 'vant'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const username = ref('')
const password = ref('')
const loading = ref(false)
const errorMsg = ref('')

async function handleSubmit(): Promise<void> {
  errorMsg.value = ''
  if (!username.value || !password.value) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  try {
    await userStore.login({
      username: username.value,
      password: password.value
    })
    if (userStore.isAdmin) {
      router.push('/admin/order')
    } else {
      router.push('/')
    }
  } catch (error: any) {
    errorMsg.value = error?.message || '登录失败，请检查用户名和密码'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <div class="brand-section">
        <div class="logo">
          <svg width="72" height="72" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"
            stroke-linecap="round" stroke-linejoin="round">
            <path
              d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" />
          </svg>
        </div>
        <h1 class="brand-title">MealPlan</h1>
        <p class="brand-subtitle">家庭每日点餐系统</p>
      </div>

      <div class="form-wrapper">
        <van-form @submit="handleSubmit" class="login-form">
          <div v-if="errorMsg" class="error-message">
            {{ errorMsg }}
          </div>

          <div class="form-group">
            <div class="field-icon">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                stroke-linecap="round" stroke-linejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </div>
            <van-field v-model="username" label="" placeholder="用户名" required class="input-field" />
          </div>

          <div class="form-group">
            <div class="field-icon">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                stroke-linecap="round" stroke-linejoin="round">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                <path d="M7 11V7a5 5 0 0 1 10 0v4" />
              </svg>
            </div>
            <van-field v-model="password" type="password" label="" placeholder="密码" required class="input-field" />
          </div>

          <van-button type="primary" native-type="submit" :loading="loading" block class="login-btn">
            登录
          </van-button>
        </van-form>
      </div>
    </div>

    <div class="bg-decoration"></div>
    <div class="bg-decoration-secondary"></div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  background: var(--background-color);
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.bg-decoration {
  position: absolute;
  top: -150px;
  right: -100px;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(202, 138, 4, 0.06) 0%, transparent 70%);
  border-radius: 50%;
}

.bg-decoration-secondary {
  position: absolute;
  bottom: -100px;
  left: -80px;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(28, 25, 23, 0.04) 0%, transparent 70%);
  border-radius: 50%;
}

.login-container {
  width: 100%;
  max-width: 380px;
  z-index: 1;
  animation: fadeInUp 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.brand-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 88px;
  height: 88px;
  margin: 0 auto 20px;
  color: var(--primary-color);
  opacity: 0.9;
}

.brand-title {
  font-family: 'Cormorant', Georgia, serif;
  font-size: 42px;
  font-weight: 700;
  color: var(--primary-color);
  letter-spacing: -1px;
  margin-bottom: 8px;
}

.brand-subtitle {
  font-size: 14px;
  color: var(--text-muted);
  font-weight: 400;
  letter-spacing: 0.5px;
}

.form-wrapper {
  background: var(--surface-color);
  border-radius: var(--radius-xl);
  padding: 32px;
  box-shadow: var(--shadow-lg);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.error-message {
  background: rgba(239, 68, 68, 0.08);
  color: #dc2626;
  font-size: 13px;
  font-weight: 500;
  padding: 10px 16px;
  border-radius: var(--radius-md);
  border: 1px solid rgba(239, 68, 68, 0.15);
  text-align: center;
}

.form-group {
  display: flex;
  align-items: center;
  background: var(--background-color);
  border-radius: var(--radius-md);
  padding: 0 16px;
  border: 1px solid var(--border-light);
  transition: all var(--transition-normal);
}

.form-group:focus-within {
  border-color: var(--cta-color);
  box-shadow: 0 0 0 3px rgba(202, 138, 4, 0.08);
}

.field-icon {
  color: var(--text-light);
  margin-right: 12px;
  flex-shrink: 0;
  transition: color var(--transition-fast);
}

.form-group:focus-within .field-icon {
  color: var(--cta-color);
}

.input-field :deep(.van-field__control) {
  font-size: 15px;
  color: var(--text-color);
  font-weight: 500;
}

.input-field :deep(.van-field__placeholder) {
  color: var(--text-light);
}

.login-btn {
  margin-top: 8px;
  background: var(--primary-color);
  border: none;
  border-radius: var(--radius-md);
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all var(--transition-normal);
}

.login-btn:active {
  transform: scale(0.98);
  background: var(--secondary-color);
}

@media (max-width: 375px) {
  .login-page {
    padding: 16px;
  }

  .brand-title {
    font-size: 36px;
  }

  .form-wrapper {
    padding: 24px;
  }
}
</style>
