import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import Cookies from 'js-cookie'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

import '@/assets/styles/index.scss' // global css



import App from './App.vue'
import router from './router'
// import './permission.js'
import store from '@/store/index.js'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(store)
app.use(ElementPlus, {
  locale: locale,
  // 支持 large、default、small
  size: Cookies.get('size') || 'default'
})

app.mount('#app')
