<template>
  <div>
    <h1>Socket.io Test</h1>
    <div><p>{{ status }}</p></div>
    <div><p>{{ message }}</p></div>
    <button @click="connect">Connect</button>
    <button @click="disconnect">Disconnect</button>
    <button @click="send">Send Message</button>
  </div>
</template>

<script>
import io from 'socket.io-client';

export default {
  name: 'SocketIOTest',
  data() {
    return {
      socket: null,
      status: 'Waiting for input',
      message: 'hello world!',
      firstConnect: true
    }
  },
  methods: {
    connect() {
      if (this.firstConnect) {
        this.socket = io.connect("http://localhost:9092?mac=2");
        this.setupSocketListeners();
        this.firstConnect = false;
      } else {
        this.socket.connect();
      }
    },
    disconnect() {
      this.socket.disconnect();
    },
    send() {
      console.log("Sending message to server");
      const msg = "我很好的,是的．";
      this.socket.emit('messageevent', { msgContent: msg });
    },
    setupSocketListeners() {
      this.socket.on('connect', () => {
        this.status = "Connected to Server";
      });
      this.socket.on('disconnect', () => {
        this.status = "Disconnected from Server";
      });
      this.socket.on('messageevent', (data) => {
        this.message = "Server says: " + data;
      });
    }
  },
  beforeUnmount() {
    if (this.socket) {
      this.socket.disconnect();
    }
  }
}
</script>