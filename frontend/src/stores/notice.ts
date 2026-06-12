import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { noticeApi, type Notice, type NoticeAddRequest } from '@/api/note'

export const useNoticeStore = defineStore('notice', () => {
  const notices = ref<Notice[]>([])
  const loading = ref(false)

  const unreadCount = computed(() => notices.value.length)

  function fetchNotices(): Promise<void> {
    loading.value = true
    return noticeApi.list().then((data) => {
      notices.value = data
    }).finally(() => {
      loading.value = false
    })
  }

  function addNotice(data: NoticeAddRequest): Promise<Notice> {
    return noticeApi.add(data).then((notice) => {
      notices.value.unshift(notice)
      return notice
    })
  }

  function markAllRead(): void {
    notices.value = []
  }

  return {
    notices,
    loading,
    unreadCount,
    fetchNotices,
    addNotice,
    markAllRead
  }
})
