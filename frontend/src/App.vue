<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppLayout from './layouts/AppLayout.vue'
import BlankLayout from './layouts/BlankLayout.vue'

const route = useRoute()

// meta.layout vient de router/routes.js : 'app' (sidebar + topbar) ou 'blank' (auth, erreurs)
const layout = computed(() => (route.meta.layout === 'blank' ? BlankLayout : AppLayout))
</script>

<template>
  <component :is="layout">
    <router-view :key="route.fullPath" />
  </component>
</template>
