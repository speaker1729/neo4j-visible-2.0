import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/about.vue'),
  },
  {
    path: '/',
    redirect: '/home',
    component: () => import('../components/HomeView.vue'),
    children: [
      {
        path: 'home',
        name: 'home',
        component: () => import('../views/home.vue'),
      },
      {
        path: 'map',
        name: 'map',
        component: () => import('../views/map.vue'),
      },
      {
        path: 'statistics',
        name: 'statistics',
        component: () => import('../views/statistics.vue'),
      },
      {
        path: 'china',
        name: 'china',
        component: () => import('../views/china.vue'),
      },
      {
        path: 'data',
        name: 'data',
        component: () => import('../views/data.vue'),
      },
      {
        path: 'circle',
        name: 'circle',
        component: () => import('../views/circle.vue'),
      },
    ],
  },
];

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes,
});

export default router;