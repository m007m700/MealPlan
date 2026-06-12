import axios from '@/utils/axios'

export interface Notice {
  id: number
  title: string
  content: string
  senderId: number
  createTime: string
}

export interface NoticeAddRequest {
  title: string
  content: string
}

export const noticeApi = {
  list(): Promise<Notice[]> {
    return axios.get('/notice/list')
  },

  add(data: NoticeAddRequest): Promise<Notice> {
    return axios.post('/notice/add', data)
  }
}
