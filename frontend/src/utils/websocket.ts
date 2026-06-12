class WebSocketClient {
  private ws: WebSocket | null = null
  private url: string = ''
  private reconnectDelay: number = 3000
  private messageCallback: ((type: string, data: unknown) => void) | null = null

  connect(url: string): void {
    this.url = url
    this.ws = new WebSocket(url)

    this.ws.onopen = () => {
      console.log('WebSocket connected')
    }

    this.ws.onmessage = (event) => {
      try {
        const message = JSON.parse(event.data)
        if (this.messageCallback) {
          this.messageCallback(message.type, message.data)
        }
      } catch (error) {
        console.error('WebSocket message parse error:', error)
      }
    }

    this.ws.onerror = (error) => {
      console.error('WebSocket error:', error)
    }

    this.ws.onclose = () => {
      console.log('WebSocket disconnected, reconnecting...')
      setTimeout(() => this.connect(this.url), this.reconnectDelay)
    }
  }

  disconnect(): void {
    if (this.ws) {
      this.ws.close()
      this.ws = null
    }
  }

  sendMessage(type: string, data: unknown): void {
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify({ type, data }))
    }
  }

  onMessage(callback: (type: string, data: unknown) => void): void {
    this.messageCallback = callback
  }
}

export const wsClient = new WebSocketClient()
