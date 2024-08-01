<template>
  <div>
    <h2>WebSocket Test</h2>
    <div>
      <button @click="connect" :disabled="isConnected">Connect</button>
      <button @click="disconnect" :disabled="!isConnected">Disconnect</button>
    </div>
    <div>
      <input v-model="message" @keyup.enter="sendMessage" :disabled="!isConnected" placeholder="Enter message">
      <button @click="sendMessage" :disabled="!isConnected">Send</button>
    </div>
    <div>
      <h3>Messages:</h3>
      <ul>
        <li v-for="(msg, index) in messages" :key="index">{{ msg }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted } from 'vue'

const socket = ref(null)
const isConnected = ref(false)
const message = ref('')
const messages = ref([])

const connect = () => {
  socket.value = new WebSocket('ws://localhost:8080/websocket')
  socket.value.onopen = onOpen
  socket.value.onmessage = onMessage
  socket.value.onclose = onClose
  socket.value.onerror = onError
}

const disconnect = () => {
  if (socket.value) {
    socket.value.close()
  }
}

const sendMessage = () => {
  if (socket.value && isConnected.value && message.value) {
    socket.value.send(message.value)
    message.value = ''
  }
}

const onOpen = () => {
  isConnected.value = true
  messages.value.push('Connected to WebSocket server')
}

const onMessage = (event) => {
  messages.value.push(`Received: ${event.data}`)
}

const onClose = () => {
  isConnected.value = false
  messages.value.push('Disconnected from WebSocket server')
}

const onError = (error) => {
  console.error('WebSocket Error:', error)
  messages.value.push('WebSocket Error: ' + error.message)
}

onUnmounted(() => {
  disconnect()
})
</script>

<style scoped>
button {
  margin: 5px;
}

input {
  margin: 5px;
  width: 300px;
}

ul {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #ccc;
  padding: 10px;
}
</style>