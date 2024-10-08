import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/video',
      name: 'video',
      component: () => import('../views/Video.vue'),
      props: true
    },
    {
      path: '/videohls',
      name: 'videohls',
      component: () => import('../views/VideoHls.vue'),
      props: true
    },
    {
      path: '/artifact-list',
      name: 'video List',
      component: () => import('../views/ArtifactList.vue')

    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/register.vue')
    },
    {
      path: "/socket",
      name: 'socket',
      component: () => import('../views/SocketView.vue')
    },
    {
      path: "/websocket",
      name: 'websocket',
      component: () => import('../views/WebsocketView.vue')
    }
  ]
})

export default router